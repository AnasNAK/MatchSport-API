package NAK.MatchSport_API.Service;


import NAK.MatchSport_API.Dto.request.MessageRequest;
import NAK.MatchSport_API.Dto.response.MessageResponse;
import NAK.MatchSport_API.Entity.ChatRoom;
import NAK.MatchSport_API.Entity.Message;
import NAK.MatchSport_API.Entity.Participant;
import NAK.MatchSport_API.Enum.MessageStatus;
import NAK.MatchSport_API.Exception.UnauthorizedException;
import NAK.MatchSport_API.Mapper.MessageMapper;
import NAK.MatchSport_API.Repository.ChatRoomRepository;
import NAK.MatchSport_API.Repository.MessageRepository;
import NAK.MatchSport_API.Repository.ParticipantRepository;
import NAK.MatchSport_API.Util.messaging.RedisMessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ParticipantRepository participantRepository;
    private final EventService eventService;
    private final MessageMapper messageMapper;
    private final RedisMessagePublisher redisMessagePublisher;

    public Page<MessageResponse> getMessagesByChatRoomId(Long chatRoomId, Pageable pageable) {
        // Validate access to chat room
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found with id: " + chatRoomId));

        validateChatRoomAccess(chatRoom);

        Page<Message> messages = messageRepository.findByChatRoomIdOrderByDateDesc(chatRoomId, pageable);
        return messages.map(messageMapper::messageToMessageResponse);
    }

    public MessageResponse getMessageById(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));

        // Validate access to chat room
        validateChatRoomAccess(message.getChatRoom());

        return messageMapper.messageToMessageResponse(message);
    }

    @Transactional
    public MessageResponse createMessage(MessageRequest messageRequest) {
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        Participant sender = participantRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        ChatRoom chatRoom = chatRoomRepository.findById(messageRequest.getChatRoomId())
                .orElseThrow(() -> new RuntimeException("Chat room not found with id: " + messageRequest.getChatRoomId()));

        // Validate access to chat room
        validateChatRoomAccess(chatRoom);

        Message message = messageMapper.messageRequestToMessage(messageRequest);
        message.setSender(sender);
        message.setChatRoom(chatRoom);
        message.setDate(LocalDateTime.now());
        message.setMessageStatus(MessageStatus.SENT);
        message.setEdited(false);

        Message savedMessage = messageRepository.save(message);
        MessageResponse savedMessageResponse = messageMapper.messageToMessageResponse(savedMessage);

        // Publish message to Redis for real-time delivery
        redisMessagePublisher.publishToChatRoom(savedMessageResponse, messageRequest.getChatRoomId());

        return savedMessageResponse;
    }

    @Transactional
    public MessageResponse updateMessage(Long id, MessageRequest messageRequest) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));

        // Validate access to chat room
        validateChatRoomAccess(message.getChatRoom());

        // Check if the current user is the sender of the message
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        if (!message.getSender().getEmail().equals(currentUserEmail) &&
                !authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_SUPER_ADMIN"))) {
            throw new RuntimeException("You are not authorized to update this message");
        }

        message.setContent(messageRequest.getContent());
        message.setEdited(true);

        Message updatedMessage = messageRepository.save(message);
        MessageResponse updatedMessageResponse = messageMapper.messageToMessageResponse(updatedMessage);

        // Publish updated message to Redis
        redisMessagePublisher.publishToChatRoom(updatedMessageResponse, message.getChatRoom().getId());

        return updatedMessageResponse;
    }

    @Transactional
    public void deleteMessage(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));

        // Validate access to chat room
        validateChatRoomAccess(message.getChatRoom());

        // Check if the current user is the sender of the message
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        if (!message.getSender().getEmail().equals(currentUserEmail) &&
                !authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_SUPER_ADMIN"))) {
            throw new RuntimeException("You are not authorized to delete this message");
        }

        messageRepository.delete(message);
    }


    private void validateChatRoomAccess(ChatRoom chatRoom) {
        // If the chat room is not linked to an event, only admins can access it
        if (chatRoom.getEvent() == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
            boolean isSuperAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));

            if (!isAdmin && !isSuperAdmin) {
                throw new UnauthorizedException("You don't have permission to access this chat room");
            }
            return;
        }

        // If the chat room is linked to an event, check if the user is a participant
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        Participant participant = participantRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        boolean isSuperAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));

        // Admins and super admins can access all chat rooms
        if (isAdmin || isSuperAdmin) {
            return;
        }

        // Check if the user is a participant in the event
        boolean isParticipant = eventService.isParticipantInEvent(chatRoom.getEvent().getId(), participant.getId());

        if (!isParticipant) {
            throw new UnauthorizedException("You must be a participant in the event to access this chat room");
        }
    }
}


