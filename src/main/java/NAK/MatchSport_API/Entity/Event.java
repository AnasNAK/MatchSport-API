package NAK.MatchSport_API.Entity;

import NAK.MatchSport_API.Enum.EventType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name= "event")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(nullable = false , name = "date")
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(name = "eventType",nullable = false)
    private EventType eventType;

    @Column(nullable = false , name = "maxParticipants")
    private Integer maxParticipants;

    @ManyToOne
    @JoinColumn(name = "eventCreator")
    private Participant eventCreator;

    @OneToMany(mappedBy = "event" ,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Reservation> reservationList = new ArrayList<>();

    @OneToOne(mappedBy = "event")
    private ChatRoom chatRoom;

    @OneToOne(cascade = CascadeType.ALL , orphanRemoval = true)
    @JoinColumn(name = "venueId",referencedColumnName = "id")
    private Venue venue;

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "eventImage",referencedColumnName = "id")
    private CloudinaryImage eventImage;


}
