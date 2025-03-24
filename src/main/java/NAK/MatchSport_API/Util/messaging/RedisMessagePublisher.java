package NAK.MatchSport_API.Util.messaging;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import NAK.MatchSport_API.Dto.response.MessageResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisMessagePublisher {

    private final RedisTemplate<String, MessageResponse> messageRedisTemplate;
    private final ChannelTopic topic;

    public void publish(MessageResponse message) {
        messageRedisTemplate.convertAndSend(topic.getTopic(), message);
    }

    public void publishToChatRoom(MessageResponse message, Long chatRoomId) {
        messageRedisTemplate.convertAndSend("chatroom:" + chatRoomId, message);
    }
}


