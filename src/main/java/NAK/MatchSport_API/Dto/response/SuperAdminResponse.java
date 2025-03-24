package NAK.MatchSport_API.Dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuperAdminResponse {
    private Long id;
    private String email;
    private String fullName;
    private LocalDate dateOfBirth;
    private String location;
}
