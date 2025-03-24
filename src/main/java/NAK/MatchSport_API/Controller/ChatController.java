package NAK.MatchSport_API.Controller;

import NAK.MatchSport_API.Dto.request.MessageRequest;
import NAK.MatchSport_API.Dto.response.MessageResponse;
import NAK.MatchSport_API.Service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload MessageRequest messageRequest) {
        // Save message to database and publish to Redis
        messageService.createMessage(messageRequest);
        // No need to manually publish to WebSocket as Redis subscriber will handle it
    }
}




