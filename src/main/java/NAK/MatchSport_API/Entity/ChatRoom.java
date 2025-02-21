package NAK.MatchSport_API.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chatRoom")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name" , nullable = false)
    private String name;

    @OneToMany(mappedBy = "chatRoom" , cascade = CascadeType.ALL ,orphanRemoval = true)
    private List<Message> messageList = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL ,orphanRemoval = true)
    @JoinColumn(name = "eventId" , referencedColumnName = "id")
    private Event event;


}
