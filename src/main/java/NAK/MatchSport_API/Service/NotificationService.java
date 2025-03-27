package NAK.MatchSport_API.Service;

import NAK.MatchSport_API.Dto.request.NotificationRequest;
import NAK.MatchSport_API.Dto.response.NotificationResponse;
import NAK.MatchSport_API.Entity.Notification;
import NAK.MatchSport_API.Entity.Participant;
import NAK.MatchSport_API.Mapper.NotificationMapper;
import NAK.MatchSport_API.Repository.NotificationRepository;
import NAK.MatchSport_API.Repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final ParticipantRepository participantRepository;
    private final NotificationMapper notificationMapper;

    public Page<NotificationResponse> getNotificationsForCurrentUser(Pageable pageable) {
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        Participant participant = participantRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        Page<Notification> notifications = notificationRepository.findByParticipantIdOrderByCreatedAtDesc(participant.getId(), pageable);
        return notifications.map(notificationMapper::notificationToNotificationResponse);
    }

    public List<NotificationResponse> getUnreadNotificationsForCurrentUser() {
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        Participant participant = participantRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        List<Notification> notifications = notificationRepository.findByParticipantIdAndReadFalseOrderByCreatedAtDesc(participant.getId());
        return notificationMapper.notificationsToNotificationResponses(notifications);
    }

    public NotificationResponse getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));

        // Check if the current user is the recipient of the notification
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        if (!notification.getParticipant().getEmail().equals(currentUserEmail) &&
                !authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_SUPER_ADMIN"))) {
            throw new RuntimeException("You are not authorized to view this notification");
        }

        return notificationMapper.notificationToNotificationResponse(notification);
    }

    @Transactional
    public NotificationResponse createNotification(NotificationRequest notificationRequest) {
        Participant participant = participantRepository.findById(notificationRequest.getParticipantId())
                .orElseThrow(() -> new RuntimeException("Participant not found with id: " + notificationRequest.getParticipantId()));

        Notification notification = notificationMapper.notificationRequestToNotification(notificationRequest);
        notification.setParticipant(participant);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);

        Notification savedNotification = notificationRepository.save(notification);
        return notificationMapper.notificationToNotificationResponse(savedNotification);
    }

    @Transactional
    public NotificationResponse markNotificationAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));

        // Check if the current user is the recipient of the notification
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        if (!notification.getParticipant().getEmail().equals(currentUserEmail) &&
                !authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_SUPER_ADMIN"))) {
            throw new RuntimeException("You are not authorized to update this notification");
        }

        notification.setRead(true);

        Notification updatedNotification = notificationRepository.save(notification);
        return notificationMapper.notificationToNotificationResponse(updatedNotification);
    }

    @Transactional
    public void markAllNotificationsAsRead() {
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        Participant participant = participantRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        notificationRepository.markAllAsReadForParticipant(participant.getId());
    }

    @Transactional
    public void deleteNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));

        // Check if the current user is the recipient of the notification
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        if (!notification.getParticipant().getEmail().equals(currentUserEmail) &&
                !authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_SUPER_ADMIN"))) {
            throw new RuntimeException("You are not authorized to delete this notification");
        }

        notificationRepository.delete(notification);
    }
}

