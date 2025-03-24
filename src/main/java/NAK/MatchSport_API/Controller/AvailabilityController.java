package NAK.MatchSport_API.Controller;

import NAK.MatchSport_API.Dto.request.AvailabilityRequest;
import NAK.MatchSport_API.Dto.response.AvailabilityResponse;
import NAK.MatchSport_API.Service.AvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/availabilities")
@RequiredArgsConstructor
public class AvailabilityController {
    private final AvailabilityService availabilityService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<AvailabilityResponse>> getAllAvailabilities() {
        return ResponseEntity.ok(availabilityService.getAllAvailabilities());
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<AvailabilityResponse>> getAvailabilitiesForCurrentUser() {
        return ResponseEntity.ok(availabilityService.getAvailabilitiesForCurrentUser());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<AvailabilityResponse> getAvailabilityById(@PathVariable Long id) {
        return ResponseEntity.ok(availabilityService.getAvailabilityById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<AvailabilityResponse> createAvailability(@Valid @RequestBody AvailabilityRequest availabilityRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(availabilityService.createAvailability(availabilityRequest));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<AvailabilityResponse> updateAvailability(@PathVariable Long id, @Valid @RequestBody AvailabilityRequest availabilityRequest) {
        return ResponseEntity.ok(availabilityService.updateAvailability(id, availabilityRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteAvailability(@PathVariable Long id) {
        availabilityService.deleteAvailability(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{availabilityId}/participants/{participantId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<AvailabilityResponse> addParticipantToAvailability(@PathVariable Long availabilityId, @PathVariable Long participantId) {
        return ResponseEntity.ok(availabilityService.addParticipantToAvailability(availabilityId, participantId));
    }

    @DeleteMapping("/{availabilityId}/participants/{participantId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<AvailabilityResponse> removeParticipantFromAvailability(@PathVariable Long availabilityId, @PathVariable Long participantId) {
        return ResponseEntity.ok(availabilityService.removeParticipantFromAvailability(availabilityId, participantId));
    }
}


