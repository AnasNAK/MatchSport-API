package NAK.MatchSport_API.Dto.request;

import NAK.MatchSport_API.Enum.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {

    @NotBlank(message = "Content cannot be blank")
    private String content;

    private boolean read;

    @NotNull(message = "Notification type cannot be null")
    private NotificationType type;

    @NotNull(message = "Participant ID cannot be null")
    private Long participantId;
}



