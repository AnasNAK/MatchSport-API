package NAK.MatchSport_API.Dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantResponse {
    private Long id;
    private String email;
    private String fullName;
    private LocalDate dateOfBirth;
    private String location;
    private MediaResponse profileImage;
    private List<SportWithLevelResponse> sports;
}
