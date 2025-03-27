package NAK.MatchSport_API.Entity;

import NAK.MatchSport_API.Entity.Embedded.AvailabilityParticipantIds;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "availabilityParticipant")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class AvailabilityParticipant {

    @EmbeddedId
    private AvailabilityParticipantIds availabilityParticipantIds;

    @ManyToOne
    @MapsId("availabilityId")
    private Availability availability;

    @ManyToOne
    @MapsId("participantId")
    private Participant participant;


}
