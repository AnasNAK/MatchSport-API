package NAK.MatchSport_API.Repository;

import NAK.MatchSport_API.Entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByParticipantIdOrderByCreatedAtDesc(Long participantId, Pageable pageable);

    List<Notification> findByParticipantIdAndIsReadFalseOrderByCreatedAtDesc(Long participantId);

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.participant.id = :participantId AND n.isRead = false")
    void markAllAsReadForParticipant(@Param("participantId") Long participantId);
}


