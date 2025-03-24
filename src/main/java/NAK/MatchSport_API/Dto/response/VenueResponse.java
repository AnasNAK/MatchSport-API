package NAK.MatchSport_API.Dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VenueResponse {
    private Long id;
    private String name;
    private String address;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<MediaResponse> images;
}
