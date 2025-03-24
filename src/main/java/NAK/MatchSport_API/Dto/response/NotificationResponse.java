package NAK.MatchSport_API.Dto.response;

import NAK.MatchSport_API.Enum.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private boolean read;
    private NotificationType type;
    private ParticipantResponse participant;
}