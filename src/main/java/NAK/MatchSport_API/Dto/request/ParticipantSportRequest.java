package NAK.MatchSport_API.Dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantSportRequest {

    @NotNull(message = "Sport ID is required")
    private Long sportId;

    @NotNull(message = "Participant ID is required")
    private Long participantId;

    @NotNull(message = "Level is required")
    @Min(value = 1, message = "Level must be at least 1")
    @Max(value = 10, message = "Level must be at most 10")
    private Integer level;
}
