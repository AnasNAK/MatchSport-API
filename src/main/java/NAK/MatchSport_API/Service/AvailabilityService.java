package NAK.MatchSport_API.Service;

import NAK.MatchSport_API.Dto.request.AvailabilityRequest;
import NAK.MatchSport_API.Dto.response.AvailabilityResponse;
import NAK.MatchSport_API.Dto.response.ParticipantResponse;
import NAK.MatchSport_API.Entity.Availability;
import NAK.MatchSport_API.Entity.AvailabilityParticipant;
import NAK.MatchSport_API.Entity.Embedded.AvailabilityParticipantIds;
import NAK.MatchSport_API.Entity.Participant;
import NAK.MatchSport_API.Mapper.AvailabilityMapper;
import NAK.MatchSport_API.Mapper.ParticipantMapper;
import NAK.MatchSport_API.Repository.AvailabilityRepository;
import NAK.MatchSport_API.Repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvailabilityService {
    private final AvailabilityRepository availabilityRepository;
    private final ParticipantRepository participantRepository;
    private final AvailabilityMapper availabilityMapper;
    private final ParticipantMapper participantMapper;

    public List<AvailabilityResponse> getAllAvailabilities() {
        List<Availability> availabilities = availabilityRepository.findAll();
        return availabilities.stream()
                .map(availability -> {
                    AvailabilityResponse response = availabilityMapper.availabilityToAvailabilityResponse(availability);
                    List<ParticipantResponse> participants = availability.getAvailabilityParticipantList().stream()
                            .map(ap -> participantMapper.participantToParticipantResponse(ap.getParticipant()))
                            .collect(Collectors.toList());
                    response.setParticipants(participants);
                    return response;
                })
                .collect(Collectors.toList());
    }

    public AvailabilityResponse getAvailabilityById(Long id) {
        Availability availability = availabilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Availability not found with id: " + id));

        AvailabilityResponse response = availabilityMapper.availabilityToAvailabilityResponse(availability);
        List<ParticipantResponse> participants = availability.getAvailabilityParticipantList().stream()
                .map(ap -> participantMapper.participantToParticipantResponse(ap.getParticipant()))
                .collect(Collectors.toList());
        response.setParticipants(participants);

        return response;
    }

    @Transactional
    public AvailabilityResponse createAvailability(AvailabilityRequest availabilityRequest) {
        Availability availability = availabilityMapper.availabilityRequestToAvailability(availabilityRequest);
        Availability savedAvailability = availabilityRepository.save(availability);

        AvailabilityResponse response = availabilityMapper.availabilityToAvailabilityResponse(savedAvailability);
        response.setParticipants(List.of());

        return response;
    }

    @Transactional
    public AvailabilityResponse updateAvailability(Long id, AvailabilityRequest availabilityRequest) {
        Availability availability = availabilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Availability not found with id: " + id));

        availabilityMapper.updateAvailabilityFromAvailabilityRequest(availabilityRequest, availability);
        Availability updatedAvailability = availabilityRepository.save(availability);

        AvailabilityResponse response = availabilityMapper.availabilityToAvailabilityResponse(updatedAvailability);
        List<ParticipantResponse> participants = updatedAvailability.getAvailabilityParticipantList().stream()
                .map(ap -> participantMapper.participantToParticipantResponse(ap.getParticipant()))
                .collect(Collectors.toList());
        response.setParticipants(participants);

        return response;
    }

    @Transactional
    public void deleteAvailability(Long id) {
        if (!availabilityRepository.existsById(id)) {
            throw new RuntimeException("Availability not found with id: " + id);
        }
        availabilityRepository.deleteById(id);
    }

    @Transactional
    public AvailabilityResponse addParticipantToAvailability(Long availabilityId, Long participantId) {
        Availability availability = availabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new RuntimeException("Availability not found with id: " + availabilityId));

        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new RuntimeException("Participant not found with id: " + participantId));

        // Check if the participant is already added to this availability
        boolean alreadyAdded = availability.getAvailabilityParticipantList().stream()
                .anyMatch(ap -> ap.getParticipant().getId().equals(participantId));

        if (alreadyAdded) {
            throw new RuntimeException("Participant is already added to this availability");
        }

        AvailabilityParticipantIds ids = new AvailabilityParticipantIds();
        ids.setAvailabilityId(availabilityId);
        ids.setParticipantId(participantId);

        AvailabilityParticipant availabilityParticipant = new AvailabilityParticipant();
        availabilityParticipant.setAvailabilityParticipantIds(ids);
        availabilityParticipant.setAvailability(availability);
        availabilityParticipant.setParticipant(participant);

        availability.getAvailabilityParticipantList().add(availabilityParticipant);
        participant.getAvailabilityParticipantList().add(availabilityParticipant);

        Availability updatedAvailability = availabilityRepository.save(availability);

        AvailabilityResponse response = availabilityMapper.availabilityToAvailabilityResponse(updatedAvailability);
        List<ParticipantResponse> participants = updatedAvailability.getAvailabilityParticipantList().stream()
                .map(ap -> participantMapper.participantToParticipantResponse(ap.getParticipant()))
                .collect(Collectors.toList());
        response.setParticipants(participants);

        return response;
    }

    @Transactional
    public AvailabilityResponse removeParticipantFromAvailability(Long availabilityId, Long participantId) {
        Availability availability = availabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new RuntimeException("Availability not found with id: " + availabilityId));

        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new RuntimeException("Participant not found with id: " + participantId));

        // Check if the participant is added to this availability
        boolean isAdded = availability.getAvailabilityParticipantList().stream()
                .anyMatch(ap -> ap.getParticipant().getId().equals(participantId));

        if (!isAdded) {
            throw new RuntimeException("Participant is not added to this availability");
        }

        availability.getAvailabilityParticipantList().removeIf(ap -> ap.getParticipant().getId().equals(participantId));
        participant.getAvailabilityParticipantList().removeIf(ap -> ap.getAvailability().getId().equals(availabilityId));

        Availability updatedAvailability = availabilityRepository.save(availability);

        AvailabilityResponse response = availabilityMapper.availabilityToAvailabilityResponse(updatedAvailability);
        List<ParticipantResponse> participants = updatedAvailability.getAvailabilityParticipantList().stream()
                .map(ap -> participantMapper.participantToParticipantResponse(ap.getParticipant()))
                .collect(Collectors.toList());
        response.setParticipants(participants);

        return response;
    }

    public List<AvailabilityResponse> getAvailabilitiesForCurrentUser() {
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        Participant participant = participantRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        List<Availability> availabilities = availabilityRepository.findByParticipantId(participant.getId());

        return availabilities.stream()
                .map(availability -> {
                    AvailabilityResponse response = availabilityMapper.availabilityToAvailabilityResponse(availability);
                    List<ParticipantResponse> participants = availability.getAvailabilityParticipantList().stream()
                            .map(ap -> participantMapper.participantToParticipantResponse(ap.getParticipant()))
                            .collect(Collectors.toList());
                    response.setParticipants(participants);
                    return response;
                })
                .collect(Collectors.toList());
    }
}


