package NAK.MatchSport_API.Controller;

import NAK.MatchSport_API.Dto.request.SportRequest;
import NAK.MatchSport_API.Dto.response.SportResponse;
import NAK.MatchSport_API.Service.SportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/sports")
@RequiredArgsConstructor
public class SportController {
    private final SportService sportService;

    @GetMapping
    public ResponseEntity<List<SportResponse>> getAllSports() {
        return ResponseEntity.ok(sportService.getAllSports());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SportResponse> getSportById(@PathVariable Long id) {
        return ResponseEntity.ok(sportService.getSportById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<SportResponse> createSport(@Valid @RequestBody SportRequest sportRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sportService.createSport(sportRequest));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<SportResponse> updateSport(@PathVariable Long id, @Valid @RequestBody SportRequest sportRequest) {
        return ResponseEntity.ok(sportService.updateSport(id, sportRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteSport(@PathVariable Long id) {
        sportService.deleteSport(id);
        return ResponseEntity.noContent().build();
    }
}

