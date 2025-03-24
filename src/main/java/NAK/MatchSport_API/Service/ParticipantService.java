package NAK.MatchSport_API.Service;

import NAK.MatchSport_API.Dto.request.ParticipantSportRequest;
import NAK.MatchSport_API.Dto.request.UserRequest;
import NAK.MatchSport_API.Dto.response.ParticipantResponse;
import NAK.MatchSport_API.Entity.Participant;
import NAK.MatchSport_API.Entity.ParticipantSport;
import NAK.MatchSport_API.Entity.Sport;
import NAK.MatchSport_API.Entity.Embedded.ParticipantSportIds;
import NAK.MatchSport_API.Mapper.ParticipantMapper;
import NAK.MatchSport_API.Repository.ParticipantRepository;
import NAK.MatchSport_API.Repository.SportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final SportRepository sportRepository;
    private final ParticipantMapper participantMapper;
    private final PasswordEncoder passwordEncoder;

    public List<ParticipantResponse> getAllParticipants() {
        List<Participant> participants = participantRepository.findAll();
        return participantMapper.participantsToParticipantResponses(participants);
    }

    public ParticipantResponse getParticipantById(Long id) {
        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Participant not found with id: " + id));
        return participantMapper.participantToParticipantResponse(participant);
    }

    @Transactional
    public ParticipantResponse updateParticipant(Long id, UserRequest userRequest) {
        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Participant not found with id: " + id));

        participantMapper.updateParticipantFromUserRequest(userRequest, participant);

        // If password is provided, encode it
        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            participant.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        Participant updatedParticipant = participantRepository.save(participant);
        return participantMapper.participantToParticipantResponse(updatedParticipant);
    }

    @Transactional
    public void deleteParticipant(Long id) {
        if (!participantRepository.existsById(id)) {
            throw new RuntimeException("Participant not found with id: " + id);
        }
        participantRepository.deleteById(id);
    }

    @Transactional
    public ParticipantResponse addSportToParticipant(ParticipantSportRequest request) {
        Participant participant = participantRepository.findById(request.getParticipantId())
                .orElseThrow(() -> new RuntimeException("Participant not found with id: " + request.getParticipantId()));

        Sport sport = sportRepository.findById(request.getSportId())
                .orElseThrow(() -> new RuntimeException("Sport not found with id: " + request.getSportId()));

        boolean sportExists = participant.getParticipantSports().stream()
                .anyMatch(ps -> ps.getSport().getId().equals(sport.getId()));

        if (sportExists) {
            throw new RuntimeException("Participant already has this sport");
        }

        ParticipantSportIds ids = new ParticipantSportIds();
        ids.setParticipantId(participant.getId());
        ids.setSportId(sport.getId());

        ParticipantSport participantSport = new ParticipantSport();
        participantSport.setParticipantSportIds(ids);
        participantSport.setParticipant(participant);
        participantSport.setSport(sport);
        participantSport.setLevel(request.getLevel());

        participant.getParticipantSports().add(participantSport);
        Participant updatedParticipant = participantRepository.save(participant);

        return participantMapper.participantToParticipantResponse(updatedParticipant);
    }

    @Transactional
    public ParticipantResponse removeSportFromParticipant(Long participantId, Long sportId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new RuntimeException("Participant not found with id: " + participantId));

        participant.getParticipantSports().removeIf(ps -> ps.getSport().getId().equals(sportId));
        Participant updatedParticipant = participantRepository.save(participant);

        return participantMapper.participantToParticipantResponse(updatedParticipant);
    }
}
