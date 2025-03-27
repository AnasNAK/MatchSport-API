package NAK.MatchSport_API.Entity;

import NAK.MatchSport_API.Entity.Embedded.EventParticipantIds;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Reservation {

    @EmbeddedId
    private EventParticipantIds eventParticipantIds;

    @Column(name = "date" ,nullable = false)
    private LocalDateTime date;

    @Column(name = "startTime" ,nullable = false)
    private LocalDateTime startTime;

    @Column(name = "endTime" , nullable = false)
    private LocalDateTime endTime;

    @ManyToOne
    @MapsId("eventId")
    private Event event;

    @ManyToOne
    @MapsId("participantId")
    private Participant participant;

}
