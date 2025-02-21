package NAK.MatchSport_API.Entity;

import NAK.MatchSport_API.Enum.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , name = "content")
    private String content;

    @Column(nullable = false , name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "isRead")
    private boolean isRead;

    @Column(nullable = false,name = "type")
    private NotificationType type;

    @ManyToOne
    @JoinColumn(name = "participantId", nullable = false)
    private Participant participant;

}
