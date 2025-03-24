package NAK.MatchSport_API.Repository;

import NAK.MatchSport_API.Entity.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SportRepository extends JpaRepository<Sport, Long> {
    boolean existsByName(String name);
}
