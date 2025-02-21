package NAK.MatchSport_API.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "availability")
@Data
@NoArgsConstructor
@AllArgsConstructor


public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,name = "day")
    private DayOfWeek day;

    @Column(nullable = false, name = "startTime")
    private LocalDateTime startTime;

    @Column(nullable = false , name = "endTime")
    private LocalDateTime endTime;

    @OneToMany(mappedBy = "availability" , cascade = CascadeType.ALL ,orphanRemoval = true)
    private List<AvailabilityParticipant> availabilityParticipantList = new ArrayList<>();


}
