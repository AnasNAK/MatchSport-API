package NAK.MatchSport_API.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rating")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , name = "score")
    private int score;

    @ManyToOne
    @JoinColumn(name = "raterId", nullable = false)
    private Participant rater;

    @ManyToOne
    @JoinColumn(name = "ratedId" ,nullable = false)
    private Participant rated;
}
