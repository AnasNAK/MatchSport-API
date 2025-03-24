package NAK.MatchSport_API.Dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaRequest {

    @NotBlank(message = "URL is required")
    private String url;

    @NotBlank(message = "Format is required")
    private String format;

    private Integer width;

    private Integer height;
}

