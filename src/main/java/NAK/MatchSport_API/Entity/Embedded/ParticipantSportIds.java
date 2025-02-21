package NAK.MatchSport_API.Entity.Embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

public class ParticipantSportIds implements Serializable {

    @Column(name = "sportId")
    private Long sportId;

    @Column(name = "participantId")
    private Long participantId;
}
