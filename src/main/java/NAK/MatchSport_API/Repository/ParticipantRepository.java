package NAK.MatchSport_API.Repository;


import NAK.MatchSport_API.Entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    boolean existsByEmail(String email);
    Optional<Participant> findByEmail(String email);
}