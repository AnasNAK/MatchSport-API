package NAK.MatchSport_API.Service;

import NAK.MatchSport_API.Dto.request.RatingRequest;
import NAK.MatchSport_API.Dto.response.RatingResponse;
import NAK.MatchSport_API.Entity.Participant;
import NAK.MatchSport_API.Entity.Rating;
import NAK.MatchSport_API.Mapper.RatingMapper;
import NAK.MatchSport_API.Repository.ParticipantRepository;
import NAK.MatchSport_API.Repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.OptionalDouble;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final ParticipantRepository participantRepository;
    private final RatingMapper ratingMapper;

    public List<RatingResponse> getRatingsByRatedId(Long ratedId) {
        List<Rating> ratings = ratingRepository.findByRatedId(ratedId);
        return ratingMapper.ratingsToRatingResponses(ratings);
    }

    public double getAverageRatingForParticipant(Long participantId) {
        List<Rating> ratings = ratingRepository.findByRatedId(participantId);

        if (ratings.isEmpty()) {
            return 0.0;
        }

        OptionalDouble average = ratings.stream()
                .mapToInt(Rating::getScore)
                .average();

        return average.orElse(0.0);
    }

    @Transactional
    public RatingResponse rateParticipant(RatingRequest ratingRequest) {
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        Participant rater = participantRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        Participant rated = participantRepository.findById(ratingRequest.getRatedId())
                .orElseThrow(() -> new RuntimeException("Rated participant not found with id: " + ratingRequest.getRatedId()));

        // Check if the rater is trying to rate themselves
        if (rater.getId().equals(rated.getId())) {
            throw new RuntimeException("You cannot rate yourself");
        }

        // Check if the rater has already rated this participant
        boolean alreadyRated = ratingRepository.existsByRaterIdAndRatedId(rater.getId(), rated.getId());

        if (alreadyRated) {
            throw new RuntimeException("You have already rated this participant");
        }

        Rating rating = ratingMapper.ratingRequestToRating(ratingRequest);
        rating.setRater(rater);
        rating.setRated(rated);

        Rating savedRating = ratingRepository.save(rating);
        return ratingMapper.ratingToRatingResponse(savedRating);
    }

    @Transactional
    public RatingResponse updateRating(Long ratingId, RatingRequest ratingRequest) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new RuntimeException("Rating not found with id: " + ratingId));

        // Check if the current user is the rater
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        if (!rating.getRater().getEmail().equals(currentUserEmail) &&
                !authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_SUPER_ADMIN"))) {
            throw new RuntimeException("You are not authorized to update this rating");
        }

        rating.setScore(ratingRequest.getScore());

        Rating updatedRating = ratingRepository.save(rating);
        return ratingMapper.ratingToRatingResponse(updatedRating);
    }

    @Transactional
    public void deleteRating(Long ratingId) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new RuntimeException("Rating not found with id: " + ratingId));

        // Check if the current user is the rater
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        if (!rating.getRater().getEmail().equals(currentUserEmail) &&
                !authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_SUPER_ADMIN"))) {
            throw new RuntimeException("You are not authorized to delete this rating");
        }

        ratingRepository.delete(rating);
    }
}


