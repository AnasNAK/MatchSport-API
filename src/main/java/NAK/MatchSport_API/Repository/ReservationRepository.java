package NAK.MatchSport_API.Repository;

import NAK.MatchSport_API.Entity.Reservation;
import NAK.MatchSport_API.Entity.Embedded.EventParticipantIds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, EventParticipantIds> {
    List<Reservation> findByEventId(Long eventId);
    List<Reservation> findByParticipantId(Long participantId);
    boolean existsByEventIdAndParticipantId(Long eventId, Long participantId);
    Optional<Reservation> findByEventParticipantIds(EventParticipantIds eventParticipantIds);

}
