package NAK.MatchSport_API.Dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SportResponse {
    private Long id;
    private String name;
    private String description;
}

