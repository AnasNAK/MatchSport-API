package NAK.MatchSport_API.Service;


import NAK.MatchSport_API.Dto.request.LoginRequest;
import NAK.MatchSport_API.Dto.request.RegisterRequest;
import NAK.MatchSport_API.Dto.response.JwtResponse;
import NAK.MatchSport_API.Dto.response.SimpleMessageResponse;
import NAK.MatchSport_API.Entity.Participant;
import NAK.MatchSport_API.Entity.User;
import NAK.MatchSport_API.Mapper.ParticipantMapper;
import NAK.MatchSport_API.Repository.ParticipantRepository;
import NAK.MatchSport_API.Repository.UserRepository;
import NAK.MatchSport_API.Security.jwt.JwtUtils;
import NAK.MatchSport_API.Security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final ParticipantMapper participantMapper;

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();

        return new JwtResponse(jwt, user.getId(), user.getEmail(), user.getFullName(), roles);
    }

    @Transactional
    public SimpleMessageResponse registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return new SimpleMessageResponse("Error: Email is already in use!");
        }

        // Use mapper to convert RegisterRequest to Participant
        Participant participant = participantMapper.registerRequestToParticipant(registerRequest);

        // Set the password (encrypted)
        participant.setPassword(encoder.encode(registerRequest.getPassword()));

        participantRepository.save(participant);

        return new SimpleMessageResponse("User registered successfully!");
    }

    public SimpleMessageResponse logoutUser() {
        SecurityContextHolder.clearContext();
        return new SimpleMessageResponse("Logout successful!");
    }
}
