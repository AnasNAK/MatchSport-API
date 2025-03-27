package NAK.MatchSport_API.Service;

import NAK.MatchSport_API.Dto.request.MediaRequest;
import NAK.MatchSport_API.Dto.response.MediaResponse;
import NAK.MatchSport_API.Entity.Media;
import NAK.MatchSport_API.Entity.Participant;
import NAK.MatchSport_API.Entity.Venue;
import NAK.MatchSport_API.Mapper.MediaMapper;
import NAK.MatchSport_API.Repository.MediaRepository;
import NAK.MatchSport_API.Repository.ParticipantRepository;
import NAK.MatchSport_API.Repository.VenueRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MediaService {
    private final MediaRepository mediaRepository;
    private final ParticipantRepository participantRepository;
    private final VenueRepository venueRepository;
    private final MediaMapper mediaMapper;
    private final Cloudinary cloudinary;

    public List<MediaResponse> getAllMedia() {
        List<Media> mediaList = mediaRepository.findAll();
        return mediaMapper.mediasToMediaResponses(mediaList);
    }

    public MediaResponse getMediaById(Long id) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Media not found with id: " + id));
        return mediaMapper.mediaToMediaResponse(media);
    }

    @Transactional
    public MediaResponse uploadMedia(MultipartFile file) throws IOException {
        try {
            // Upload file to Cloudinary
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            // Create media entity
            Media media = new Media();
            media.setUrl((String) uploadResult.get("url"));
            media.setFormat((String) uploadResult.get("format"));
            media.setWidth(((Number) uploadResult.get("width")).intValue());
            media.setHeight(((Number) uploadResult.get("height")).intValue());
            // Don't set venue here since it's now nullable

            Media savedMedia = mediaRepository.save(media);
            return mediaMapper.mediaToMediaResponse(savedMedia);
        } catch (Exception e) {
            // Log the detailed error
            e.printStackTrace();
            throw e; // Re-throw to propagate to controller
        }
    }

    @Transactional
    public MediaResponse updateMedia(Long id, @Valid MediaRequest mediaRequest) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Media not found with id: " + id));

        mediaMapper.updateMediaFromMediaRequest(mediaRequest, media);
        Media updatedMedia = mediaRepository.save(media);
        return mediaMapper.mediaToMediaResponse(updatedMedia);
    }

    @Transactional
    public void deleteMedia(Long id) throws IOException {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Media not found with id: " + id));

        // Extract public_id from URL
        String publicId = extractPublicIdFromUrl(media.getUrl());

        // Delete from Cloudinary
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

        // Delete from database
        mediaRepository.delete(media);
    }

    @Transactional
    public MediaResponse setProfileImage(Long mediaId) {
        try {
            // Get current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserEmail = authentication.getName();

            Participant participant = participantRepository.findByEmail(currentUserEmail)
                    .orElseThrow(() -> new RuntimeException("Current user not found"));

            Media media = mediaRepository.findById(mediaId)
                    .orElseThrow(() -> new RuntimeException("Media not found with id: " + mediaId));

            // Remove old profile image if exists
            if (participant.getProfileImage() != null) {
                Media oldImage = participant.getProfileImage();
                oldImage.setUserImage(null);
                participant.setProfileImage(null);
                mediaRepository.save(oldImage);
            }

            // Set new profile image
            media.setUserImage(participant);
            participant.setProfileImage(media);

            participantRepository.save(participant);
            Media savedMedia = mediaRepository.save(media);
            return mediaMapper.mediaToMediaResponse(savedMedia);
        } catch (Exception e) {
            // Log the detailed error
            e.printStackTrace();
            throw e; // Re-throw to propagate to controller
        }
    }

    @Transactional
    public MediaResponse addMediaToVenue(Long mediaId, Long venueId) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found with id: " + mediaId));

        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Venue not found with id: " + venueId));

        media.setVenue(venue);
        venue.getVenueImagesList().add(media);

        Media savedMedia = mediaRepository.save(media);
        return mediaMapper.mediaToMediaResponse(savedMedia);
    }

    private String extractPublicIdFromUrl(String url) {

        String[] urlParts = url.split("/");
        String fileNameWithExtension = urlParts[urlParts.length - 1];
        return fileNameWithExtension.substring(0, fileNameWithExtension.lastIndexOf('.'));
    }
}

