package NAK.MatchSport_API.Controller;

import NAK.MatchSport_API.Dto.request.NotificationRequest;
import NAK.MatchSport_API.Dto.response.NotificationResponse;
import NAK.MatchSport_API.Service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    @PreAuthorize("hasRole('PARTICIPANT') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Page<NotificationResponse>> getNotificationsForCurrentUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(notificationService.getNotificationsForCurrentUser(pageable));
    }

    @GetMapping("/unread")
    @PreAuthorize("hasRole('PARTICIPANT') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotificationsForCurrentUser() {
        return ResponseEntity.ok(notificationService.getUnreadNotificationsForCurrentUser());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('PARTICIPANT') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<NotificationResponse> getNotificationById(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.getNotificationById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<NotificationResponse> createNotification(@Valid @RequestBody NotificationRequest notificationRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.createNotification(notificationRequest));
    }

    @PutMapping("/{id}/read")
    @PreAuthorize("hasRole('PARTICIPANT') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<NotificationResponse> markNotificationAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markNotificationAsRead(id));
    }

    @PutMapping("/read-all")
    @PreAuthorize("hasRole('PARTICIPANT') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> markAllNotificationsAsRead() {
        notificationService.markAllNotificationsAsRead();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PARTICIPANT') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}


