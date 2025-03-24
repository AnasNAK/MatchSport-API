package NAK.MatchSport_API.Service;

import NAK.MatchSport_API.Dto.request.SportRequest;
import NAK.MatchSport_API.Dto.response.SportResponse;
import NAK.MatchSport_API.Entity.Sport;
import NAK.MatchSport_API.Mapper.SportMapper;
import NAK.MatchSport_API.Repository.SportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SportService {
    private final SportRepository sportRepository;
    private final SportMapper sportMapper;

    public List<SportResponse> getAllSports() {
        List<Sport> sports = sportRepository.findAll();
        return sportMapper.sportsToSportResponses(sports);
    }

    public SportResponse getSportById(Long id) {
        Sport sport = sportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sport not found with id: " + id));
        return sportMapper.sportToSportResponse(sport);
    }

    @Transactional
    public SportResponse createSport(SportRequest sportRequest) {
        // Check if sport with the same name already exists
        if (sportRepository.existsByName(sportRequest.getName())) {
            throw new RuntimeException("Sport with name " + sportRequest.getName() + " already exists");
        }

        Sport sport = sportMapper.sportRequestToSport(sportRequest);
        Sport savedSport = sportRepository.save(sport);
        return sportMapper.sportToSportResponse(savedSport);
    }

    @Transactional
    public SportResponse updateSport(Long id, SportRequest sportRequest) {
        Sport sport = sportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sport not found with id: " + id));

        // Check if the new name is already taken by another sport
        if (!sport.getName().equals(sportRequest.getName()) &&
                sportRepository.existsByName(sportRequest.getName())) {
            throw new RuntimeException("Sport with name " + sportRequest.getName() + " already exists");
        }

        sportMapper.updateSportFromSportRequest(sportRequest, sport);
        Sport updatedSport = sportRepository.save(sport);
        return sportMapper.sportToSportResponse(updatedSport);
    }

    @Transactional
    public void deleteSport(Long id) {
        if (!sportRepository.existsById(id)) {
            throw new RuntimeException("Sport not found with id: " + id);
        }
        sportRepository.deleteById(id);
    }
}