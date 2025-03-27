package NAK.MatchSport_API.Service;

import NAK.MatchSport_API.Dto.response.ParticipantResponse;
import NAK.MatchSport_API.Dto.response.SimpleMessageResponse;
import NAK.MatchSport_API.Entity.Admin;
import NAK.MatchSport_API.Entity.Participant;
import NAK.MatchSport_API.Entity.SuperAdmin;
import NAK.MatchSport_API.Entity.User;
import NAK.MatchSport_API.Mapper.ParticipantMapper;
import NAK.MatchSport_API.Repository.AdminRepository;
import NAK.MatchSport_API.Repository.ParticipantRepository;
import NAK.MatchSport_API.Repository.SuperAdminRepository;
import NAK.MatchSport_API.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final AdminRepository adminRepository;
    private final SuperAdminRepository superAdminRepository;
    private final ParticipantMapper participantMapper;


    public List<ParticipantResponse> getAllUsers() {
        // Check if current user is admin or super admin
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        boolean isSuperAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));

        if (!isAdmin && !isSuperAdmin) {
            throw new AccessDeniedException("You don't have permission to access this resource");
        }

        List<Participant> participants = participantRepository.findAll();
        return participantMapper.participantsToParticipantResponses(participants);
    }

    /**
     * Promote a participant to admin role
     */
    @Transactional
    public SimpleMessageResponse promoteToAdmin(Long participantId) {
        // Only super admin can promote users to admin
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isSuperAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));

        if (!isSuperAdmin) {
            throw new AccessDeniedException("Only super admins can promote users to admin");
        }

        // Find the user by ID
        User user = userRepository.findById(participantId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + participantId));

        // Check if already an admin or super admin
        if (user instanceof Admin || user instanceof SuperAdmin) {
            return new SimpleMessageResponse("User is already an admin or super admin");
        }

        // Check if it's a participant
        if (!(user instanceof Participant)) {
            return new SimpleMessageResponse("User is not a participant");
        }

        Participant participant = (Participant) user;

        // Create a new Admin entity with the participant's data
        Admin admin = new Admin();
        admin.setId(participant.getId());
        admin.setEmail(participant.getEmail());
        admin.setPassword(participant.getPassword());
        admin.setFullName(participant.getFullName());
        admin.setDateOfBirth(participant.getDateOfBirth());
        admin.setLocation(participant.getLocation());
        admin.setProfileImage(participant.getProfileImage());

        // Delete the participant and save as admin
        participantRepository.delete(participant);
        adminRepository.save(admin);

        return new SimpleMessageResponse("User promoted to admin successfully");
    }

    /**
     * Demote an admin to regular participant
     */
    @Transactional
    public SimpleMessageResponse demoteToParticipant(Long adminId) {
        // Only super admin can demote admins
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isSuperAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));

        if (!isSuperAdmin) {
            throw new AccessDeniedException("Only super admins can demote admins");
        }

        // Find the user by ID
        User user = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + adminId));

        // Check if user is an admin
        if (!(user instanceof Admin)) {
            if (user instanceof SuperAdmin) {
                return new SimpleMessageResponse("Cannot demote a super admin");
            }
            return new SimpleMessageResponse("User is not an admin");
        }

        Admin admin = (Admin) user;

        // Create a new Participant entity with the admin's data
        Participant participant = new Participant();
        participant.setId(admin.getId());
        participant.setEmail(admin.getEmail());
        participant.setPassword(admin.getPassword());
        participant.setFullName(admin.getFullName());
        participant.setDateOfBirth(admin.getDateOfBirth());
        participant.setLocation(admin.getLocation());
        participant.setProfileImage(admin.getProfileImage());

        // Initialize collections
        participant.setParticipantSports(null);
        participant.setAvailabilityParticipantList(null);
        participant.setEventList(null);
        participant.setReservationList(null);
        participant.setSendermessageList(null);
        participant.setRaterList(null);
        participant.setRatedList(null);

        // Delete the admin and save as participant
        adminRepository.delete(admin);
        participantRepository.save(participant);

        return new SimpleMessageResponse("Admin demoted to participant successfully");
    }
}

