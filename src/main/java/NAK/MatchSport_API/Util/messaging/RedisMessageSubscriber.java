package NAK.MatchSport_API.Util.messaging;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import NAK.MatchSport_API.Dto.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisMessageSubscriber implements MessageListener {

    private final RedisTemplate<String, MessageResponse> messageRedisTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String channel = new String(message.getChannel());
            MessageResponse chatMessage = (MessageResponse) messageRedisTemplate.getValueSerializer()
                    .deserialize(message.getBody());

            log.info("Received message from channel {}: {}", channel, chatMessage);

            if (channel.startsWith("chatroom:")) {
                String chatRoomId = channel.substring("chatroom:".length());
                messagingTemplate.convertAndSend("/topic/chatroom." + chatRoomId, chatMessage);
            } else {
                messagingTemplate.convertAndSend("/topic/messages", chatMessage);
            }
        } catch (Exception e) {
            log.error("Error processing Redis message", e);
        }
    }
}

