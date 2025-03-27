package NAK.MatchSport_API.Controller;

import NAK.MatchSport_API.Dto.request.MediaRequest;
import NAK.MatchSport_API.Dto.response.MediaResponse;
import NAK.MatchSport_API.Service.MediaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {
    private final MediaService mediaService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<MediaResponse>> getAllMedia() {
        return ResponseEntity.ok(mediaService.getAllMedia());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediaResponse> getMediaById(@PathVariable Long id) {
        return ResponseEntity.ok(mediaService.getMediaById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('PARTICIPANT') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<MediaResponse> uploadMedia(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(mediaService.uploadMedia(file));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<MediaResponse> updateMedia(@PathVariable Long id, @Valid @RequestBody MediaRequest mediaRequest) {
        return ResponseEntity.ok(mediaService.updateMedia(id, mediaRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteMedia(@PathVariable Long id) throws IOException {
        mediaService.deleteMedia(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/profile/{mediaId}")
    @PreAuthorize("hasRole('PARTICIPANT') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<MediaResponse> setProfileImage(@PathVariable Long mediaId) {
        return ResponseEntity.ok(mediaService.setProfileImage(mediaId));
    }

    @PostMapping("/{mediaId}/venue/{venueId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<MediaResponse> addMediaToVenue(@PathVariable Long mediaId, @PathVariable Long venueId) {
        return ResponseEntity.ok(mediaService.addMediaToVenue(mediaId, venueId));
    }
}


