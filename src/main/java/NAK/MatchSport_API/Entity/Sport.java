package NAK.MatchSport_API.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sport")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Sport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true ,name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "sport" ,cascade = CascadeType.ALL , orphanRemoval = true)
    private List<ParticipantSport> participantSports = new ArrayList<>();
}
