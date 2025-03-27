package NAK.MatchSport_API.Controller;


import NAK.MatchSport_API.Dto.request.RatingRequest;
import NAK.MatchSport_API.Dto.response.RatingResponse;
import NAK.MatchSport_API.Service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @GetMapping("/participant/{participantId}")
    public ResponseEntity<List<RatingResponse>> getRatingsByRatedId(@PathVariable Long participantId) {
        return ResponseEntity.ok(ratingService.getRatingsByRatedId(participantId));
    }

    @GetMapping("/participant/{participantId}/average")
    public ResponseEntity<Double> getAverageRatingForParticipant(@PathVariable Long participantId) {
        return ResponseEntity.ok(ratingService.getAverageRatingForParticipant(participantId));
    }

    @PostMapping
    @PreAuthorize("hasRole('PARTICIPANT') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<RatingResponse> rateParticipant(@Valid @RequestBody RatingRequest ratingRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ratingService.rateParticipant(ratingRequest));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PARTICIPANT') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<RatingResponse> updateRating(@PathVariable Long id, @Valid @RequestBody RatingRequest ratingRequest) {
        return ResponseEntity.ok(ratingService.updateRating(id, ratingRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PARTICIPANT') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteRating(@PathVariable Long id) {
        ratingService.deleteRating(id);
        return ResponseEntity.noContent().build();
    }
}


