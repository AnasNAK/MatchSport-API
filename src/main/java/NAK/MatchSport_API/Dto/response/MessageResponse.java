package NAK.MatchSport_API.Dto.response;

import NAK.MatchSport_API.Enum.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private Long id;
    private String content;
    private LocalDateTime date;
    private MessageStatus messageStatus;
    private boolean edited;
    private ParticipantResponse sender;
}