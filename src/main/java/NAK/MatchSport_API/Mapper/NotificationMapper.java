package NAK.MatchSport_API.Mapper;

import NAK.MatchSport_API.Dto.request.NotificationRequest;
import NAK.MatchSport_API.Dto.response.NotificationResponse;
import NAK.MatchSport_API.Entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface NotificationMapper {

    @Mapping(target = "participant", ignore = true)
    Notification notificationRequestToNotification(NotificationRequest notificationRequest);

    NotificationResponse notificationToNotificationResponse(Notification notification);

    // Add method to map lists of notifications
    List<NotificationResponse> notificationsToNotificationResponses(List<Notification> notifications);
}










