package NAK.MatchSport_API.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "venue")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "adress", nullable = false)
    private String address;

    @Column(name = "startTime", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "endTime", nullable = false)
    private LocalDateTime endTime;

    @OneToOne(mappedBy = "venue")
    private Event event;

    @OneToMany(mappedBy = "venue" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<CloudinaryImage> venueImagesList = new ArrayList<>();
}
