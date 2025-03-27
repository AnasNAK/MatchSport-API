package NAK.MatchSport_API.Controller;

import NAK.MatchSport_API.Dto.response.ParticipantResponse;
import NAK.MatchSport_API.Dto.response.SimpleMessageResponse;
import NAK.MatchSport_API.Service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<ParticipantResponse>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PostMapping("/promote/{participantId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<SimpleMessageResponse> promoteToAdmin(@PathVariable Long participantId) {
        return ResponseEntity.ok(adminService.promoteToAdmin(participantId));
    }

    @PostMapping("/demote/{adminId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<SimpleMessageResponse> demoteToParticipant(@PathVariable Long adminId) {
        return ResponseEntity.ok(adminService.demoteToParticipant(adminId));
    }
}