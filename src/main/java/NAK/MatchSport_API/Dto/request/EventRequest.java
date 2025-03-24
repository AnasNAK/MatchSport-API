package NAK.MatchSport_API.Dto.request;

import NAK.MatchSport_API.Enum.EventType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {

    @NotBlank(message = "Event name is required")
    private String name;

    @NotNull(message = "Event date is required")
    private LocalDateTime date;

    @NotNull(message = "Event type is required")
    private EventType eventType;

    @NotNull(message = "Maximum participants is required")
    @Min(value = 1, message = "Maximum participants must be at least 1")
    private Integer maxParticipants;

    @NotNull(message = "Venue ID is required")
    private Long venueId;

    private Long eventImageId;
}
