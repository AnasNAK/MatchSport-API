package NAK.MatchSport_API.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "participant")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@PrimaryKeyJoinColumn(name = "participantId")

public class Participant extends User{

    @OneToMany(mappedBy = "participant" ,cascade = CascadeType.ALL , orphanRemoval = true)
    private List<ParticipantSport> participantSports = new ArrayList<>();

    @OneToMany(mappedBy = "participant" ,cascade = CascadeType.ALL ,orphanRemoval = true)
    private List<AvailabilityParticipant> availabilityParticipantList = new ArrayList<>();

    @OneToMany(mappedBy = "eventCreator" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Event> eventList = new ArrayList<>();

    @OneToMany(mappedBy = "participant" ,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Reservation> reservationList = new ArrayList<>();

    @OneToMany(mappedBy = "sender" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Message> sendermessageList = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL , orphanRemoval = true)
    @JoinColumn(name = "profileImage",referencedColumnName = "id")
    private Media profileImage;

    @OneToMany(mappedBy = "rater" ,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Rating> raterList = new ArrayList<>();

    @OneToMany(mappedBy = "rated" , cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Rating> ratedList = new ArrayList<>();

}
