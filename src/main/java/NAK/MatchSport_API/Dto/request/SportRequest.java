package NAK.MatchSport_API.Dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SportRequest {

    @NotBlank(message = "Sport name is required")
    private String name;

    private String description;
}
