package NAK.MatchSport_API.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cloudinaryImage")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "url")
    private String url;

    @Column(name = "format", nullable = false)
    private String format;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @OneToOne(mappedBy = "profileImage")
    private User userImage;

    @OneToOne(mappedBy = "eventImage")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "venue", nullable = true)
    private Venue venue;
}

