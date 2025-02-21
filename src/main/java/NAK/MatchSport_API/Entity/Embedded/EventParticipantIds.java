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

public class EventParticipantIds implements Serializable {

    @Column(name = "eventId")
    private Long eventId;

    @Column(name = "participantId")
    private Long participantId;

}
