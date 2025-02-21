package NAK.MatchSport_API.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cloudinaryImage")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class CloudinaryImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long publicId;

    @Column(nullable = false , name = "url")
    private String url;

    @Column(name = "format" ,nullable = false)
    private String format;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @OneToOne(mappedBy = "profileImage")
    private Participant participant;

    @OneToOne(mappedBy = "eventImage")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "venue" ,nullable = false)
    private Venue venue;
}
