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
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

public class AvailabilityParticipantIds implements Serializable {

    @Column(name = "availabilityId")
    private Long availabilityId;

    @Column(name = "participantId")
    private Long participantId;
}
