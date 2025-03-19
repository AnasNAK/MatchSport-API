package NAK.MatchSport_API.Service;


import NAK.MatchSport_API.Dto.JwtResponse;
import NAK.MatchSport_API.Dto.LoginRequest;
import NAK.MatchSport_API.Dto.MessageResponse;
import NAK.MatchSport_API.Dto.RegisterRequest;
import NAK.MatchSport_API.Entity.Participant;
import NAK.MatchSport_API.Entity.User;
import NAK.MatchSport_API.Repository.ParticipantRepository;
import NAK.MatchSport_API.Repository.UserRepository;
import NAK.MatchSport_API.Security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();

        return new JwtResponse(jwt, user.getId(), user.getEmail(), user.getFullName(), roles);
    }

    @Transactional
    public MessageResponse registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return new MessageResponse("Error: Email is already in use!");
        }

        Participant participant = new Participant();
        participant.setFullName(registerRequest.getName());
        participant.setEmail(registerRequest.getEmail());
        participant.setPassword(encoder.encode(registerRequest.getPassword()));
        participant.setDateOfBirth(registerRequest.getDateOfBirth());
        participant.setLocation(registerRequest.getLocation());

        participantRepository.save(participant);

        return new MessageResponse("User registered successfully!");
    }
}