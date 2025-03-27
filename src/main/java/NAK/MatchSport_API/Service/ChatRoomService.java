package NAK.MatchSport_API.Service;

import NAK.MatchSport_API.Dto.request.ChatRoomRequest;
import NAK.MatchSport_API.Dto.response.ChatRoomResponse;
import NAK.MatchSport_API.Entity.ChatRoom;
import NAK.MatchSport_API.Entity.Event;
import NAK.MatchSport_API.Entity.Participant;
import NAK.MatchSport_API.Exception.UnauthorizedException;
import NAK.MatchSport_API.Mapper.ChatRoomMapper;
import NAK.MatchSport_API.Repository.ChatRoomRepository;
import NAK.MatchSport_API.Repository.EventRepository;
import NAK.MatchSport_API.Repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;
    private final EventService eventService;
    private final ChatRoomMapper chatRoomMapper;

    public List<ChatRoomResponse> getAllChatRooms() {
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();
        return chatRoomMapper.chatRoomsToChatRoomResponses(chatRooms);
    }

    public ChatRoomResponse getChatRoomById(Long id) {
        ChatRoom chatRoom = chatRoomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chat room not found with id: " + id));

        // Check if user has access to this chat room
        validateChatRoomAccess(chatRoom);

        return chatRoomMapper.chatRoomToChatRoomResponse(chatRoom);
    }

    public ChatRoomResponse getChatRoomByEventId(Long eventId) {
        ChatRoom chatRoom = chatRoomRepository.findByEventId(eventId)
                .orElseThrow(() -> new RuntimeException("Chat room not found for event with id: " + eventId));

        // Check if user has access to this chat room
        validateChatRoomAccess(chatRoom);

        return chatRoomMapper.chatRoomToChatRoomResponse(chatRoom);
    }

    @Transactional
    public ChatRoomResponse createChatRoom(ChatRoomRequest chatRoomRequest) {
        ChatRoom chatRoom = chatRoomMapper.chatRoomRequestToChatRoom(chatRoomRequest);

        // Link to event if provided
        if (chatRoomRequest.getEventId() != null) {
            Event event = eventRepository.findById(chatRoomRequest.getEventId())
                    .orElseThrow(() -> new RuntimeException("Event not found with id: " + chatRoomRequest.getEventId()));
            chatRoom.setEvent(event);
            event.setChatRoom(chatRoom);
        }

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        return chatRoomMapper.chatRoomToChatRoomResponse(savedChatRoom);
    }

    @Transactional
    public ChatRoomResponse updateChatRoom(Long id, ChatRoomRequest chatRoomRequest) {
        ChatRoom chatRoom = chatRoomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chat room not found with id: " + id));

        chatRoomMapper.updateChatRoomFromChatRoomRequest(chatRoomRequest, chatRoom);

        // Update event link if changed
        if (chatRoomRequest.getEventId() != null &&
                (chatRoom.getEvent() == null || !chatRoom.getEvent().getId().equals(chatRoomRequest.getEventId()))) {
            Event event = eventRepository.findById(chatRoomRequest.getEventId())
                    .orElseThrow(() -> new RuntimeException("Event not found with id: " + chatRoomRequest.getEventId()));

            // Remove old event link if exists
            if (chatRoom.getEvent() != null) {
                chatRoom.getEvent().setChatRoom(null);
            }

            chatRoom.setEvent(event);
            event.setChatRoom(chatRoom);
        }

        ChatRoom updatedChatRoom = chatRoomRepository.save(chatRoom);
        return chatRoomMapper.chatRoomToChatRoomResponse(updatedChatRoom);
    }

    @Transactional
    public void deleteChatRoom(Long id) {
        ChatRoom chatRoom = chatRoomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chat room not found with id: " + id));

        // Remove event link if exists
        if (chatRoom.getEvent() != null) {
            chatRoom.getEvent().setChatRoom(null);
        }

        chatRoomRepository.delete(chatRoom);
    }


    private void validateChatRoomAccess(ChatRoom chatRoom) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        boolean isAdminOrSuperAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
                || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));

        if (chatRoom.getEvent() == null) {
            if (!isAdminOrSuperAdmin) {
                throw new UnauthorizedException("You don't have permission to access this chat room");
            }
            return;
        }

        Participant participant = participantRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        if (isAdminOrSuperAdmin) {
            return;
        }

        boolean isParticipant = eventService.isParticipantInEvent(chatRoom.getEvent().getId(), participant.getId());

        if (!isParticipant) {
            throw new UnauthorizedException("You must be a participant in the event to access this chat room");
        }
    }

}

