package NAK.MatchSport_API.Controller;


import NAK.MatchSport_API.Dto.request.LoginRequest;
import NAK.MatchSport_API.Dto.request.RegisterRequest;
import NAK.MatchSport_API.Dto.response.JwtResponse;
import NAK.MatchSport_API.Dto.response.MessageResponse;
import NAK.MatchSport_API.Dto.response.SimpleMessageResponse;
import NAK.MatchSport_API.Service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticateUser(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<SimpleMessageResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.registerUser(registerRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<SimpleMessageResponse> logoutUser() {
        return ResponseEntity.ok(authService.logoutUser());
    }
}