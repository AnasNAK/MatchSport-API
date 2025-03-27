package NAK.MatchSport_API.Dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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

    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dateOfBirth;
    private String location;
    private MediaResponse profileImage;
    private List<SportWithLevelResponse> sports;
}
