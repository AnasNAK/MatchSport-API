package NAK.MatchSport_API.Dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomRequest {

    @NotBlank(message = "Chat room name is required")
    private String name;

    private Long eventId;
}