package NAK.MatchSport_API.Repository;

import NAK.MatchSport_API.Entity.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    @Query("SELECT a FROM Availability a JOIN a.availabilityParticipantList ap WHERE ap.participant.id = :participantId")
    List<Availability> findByParticipantId(@Param("participantId") Long participantId);
}


