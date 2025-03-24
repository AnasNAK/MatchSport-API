package NAK.MatchSport_API.Repository;

import NAK.MatchSport_API.Entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByRatedId(Long ratedId);

    boolean existsByRaterIdAndRatedId(Long raterId, Long ratedId);
}
