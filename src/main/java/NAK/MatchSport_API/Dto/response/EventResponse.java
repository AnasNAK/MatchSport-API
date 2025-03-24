package NAK.MatchSport_API.Dto.response;

import NAK.MatchSport_API.Enum.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {
    private Long id;
    private String name;
    private LocalDateTime date;
    private EventType eventType;
    private Integer maxParticipants;
    private ParticipantResponse eventCreator;
    private VenueResponse venue;
    private MediaResponse eventImage;
    private List<ReservationResponse> reservations;
    private ChatRoomResponse chatRoom;
}