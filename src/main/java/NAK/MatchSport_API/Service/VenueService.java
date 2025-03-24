package NAK.MatchSport_API.Service;

import NAK.MatchSport_API.Dto.request.VenueRequest;
import NAK.MatchSport_API.Dto.response.VenueResponse;
import NAK.MatchSport_API.Entity.Media;
import NAK.MatchSport_API.Entity.Venue;
import NAK.MatchSport_API.Mapper.VenueMapper;
import NAK.MatchSport_API.Repository.MediaRepository;
import NAK.MatchSport_API.Repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VenueService {
    private final VenueRepository venueRepository;
    private final MediaRepository mediaRepository;
    private final VenueMapper venueMapper;

    public Page<VenueResponse> getAllVenues(Pageable pageable) {
        Page<Venue> venues = venueRepository.findAll(pageable);
        return venues.map(venueMapper::venueToVenueResponse);
    }

    public List<VenueResponse> searchVenues(String keyword) {
        List<Venue> venues = venueRepository.findByNameContainingIgnoreCase(keyword);
        return venueMapper.venuesToVenueResponses(venues);
    }

    public VenueResponse getVenueById(Long id) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venue not found with id: " + id));
        return venueMapper.venueToVenueResponse(venue);
    }

    @Transactional
    public VenueResponse createVenue(VenueRequest venueRequest) {
        Venue venue = venueMapper.venueRequestToVenue(venueRequest);
        Venue savedVenue = venueRepository.save(venue);
        return venueMapper.venueToVenueResponse(savedVenue);
    }

    @Transactional
    public VenueResponse updateVenue(Long id, VenueRequest venueRequest) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venue not found with id: " + id));

        venueMapper.updateVenueFromVenueRequest(venueRequest, venue);
        Venue updatedVenue = venueRepository.save(venue);
        return venueMapper.venueToVenueResponse(updatedVenue);
    }

    @Transactional
    public void deleteVenue(Long id) {
        if (!venueRepository.existsById(id)) {
            throw new RuntimeException("Venue not found with id: " + id);
        }
        venueRepository.deleteById(id);
    }

    @Transactional
    public VenueResponse addImageToVenue(Long venueId, Long imageId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Venue not found with id: " + venueId));

        Media media = mediaRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Media not found with id: " + imageId));

        media.setVenue(venue);
        venue.getVenueImagesList().add(media);

        Venue updatedVenue = venueRepository.save(venue);
        return venueMapper.venueToVenueResponse(updatedVenue);
    }

    @Transactional
    public VenueResponse removeImageFromVenue(Long venueId, Long imageId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Venue not found with id: " + venueId));

        venue.getVenueImagesList().removeIf(image -> image.getId().equals(imageId));

        Venue updatedVenue = venueRepository.save(venue);
        return venueMapper.venueToVenueResponse(updatedVenue);
    }
}

