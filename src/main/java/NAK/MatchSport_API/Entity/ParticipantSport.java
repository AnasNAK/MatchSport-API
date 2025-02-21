package NAK.MatchSport_API.Entity;

import NAK.MatchSport_API.Entity.Embedded.ParticipantSportIds;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "participantSport")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ParticipantSport {

    @EmbeddedId
    private ParticipantSportIds participantSportIds;

    @ManyToOne
    @MapsId("participantId")
    @JoinColumn(name = "participantId")
    private Participant participant;


    @ManyToOne
    @MapsId("sportId")
    @JoinColumn(name = "sportId")
    private Sport sport;

    @Column(nullable = false ,name = "level")
    private Integer level;

}
