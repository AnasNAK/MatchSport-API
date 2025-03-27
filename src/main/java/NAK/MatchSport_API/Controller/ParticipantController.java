package NAK.MatchSport_API.Controller;

import NAK.MatchSport_API.Dto.request.ParticipantSportRequest;
import NAK.MatchSport_API.Dto.request.UserRequest;
import NAK.MatchSport_API.Dto.response.ParticipantResponse;
import NAK.MatchSport_API.Service.ParticipantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/participants")
@RequiredArgsConstructor
public class ParticipantController {
    private final ParticipantService participantService;

    @GetMapping
    public ResponseEntity<List<ParticipantResponse>> getAllParticipants() {
        return ResponseEntity.ok(participantService.getAllParticipants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParticipantResponse> getParticipantById(@PathVariable Long id) {
        return ResponseEntity.ok(participantService.getParticipantById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or @userSecurity.isCurrentUser(#id)")
    public ResponseEntity<ParticipantResponse> updateParticipant(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(participantService.updateParticipant(id, userRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteParticipant(@PathVariable Long id) {
        participantService.deleteParticipant(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/sports")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or @userSecurity.isCurrentUser(#request.participantId)")
    public ResponseEntity<ParticipantResponse> addSportToParticipant(@Valid @RequestBody ParticipantSportRequest request) {
        return ResponseEntity.ok(participantService.addSportToParticipant(request));
    }

    @DeleteMapping("/{participantId}/sports/{sportId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or @userSecurity.isCurrentUser(#participantId)")
    public ResponseEntity<ParticipantResponse> removeSportFromParticipant(@PathVariable Long participantId, @PathVariable Long sportId) {
        return ResponseEntity.ok(participantService.removeSportFromParticipant(participantId, sportId));
    }
}


