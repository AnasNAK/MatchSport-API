package NAK.MatchSport_API.Entity;

import NAK.MatchSport_API.Enum.MessageStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content",nullable = false)
    private String content;

    @Column(name = "date" ,nullable = false)
    private LocalDateTime date;

    @Column(nullable = false ,name = "messageStatus")
    private MessageStatus messageStatus;

    @Column(nullable = false ,name = "edited")
    private boolean edited;

    @ManyToOne
    @JoinColumn(name = "sender" ,nullable = false)
    private Participant sender;

    @ManyToOne
    @JoinColumn(name = "chatRoom" ,nullable = false)
    private ChatRoom chatRoom;


}
