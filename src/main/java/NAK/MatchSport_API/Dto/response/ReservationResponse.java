package NAK.MatchSport_API.Dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponse {
    private Long eventId;
    private Long participantId;
    private LocalDateTime date;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ParticipantResponse participant;
}
