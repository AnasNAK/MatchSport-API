package NAK.MatchSport_API.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Table(name="user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true ,nullable = false , name = "email")
    private String email;

    @Column(nullable = false , name = "password")
    private String password;

    @Column(nullable = false , name = "fullName")
    private String fullName;

    @Column(name = "dateOfBirth")
    private LocalDate dateOfBirth;

    @Column(name = "location")
    private String location;

    @OneToOne(cascade = CascadeType.ALL , orphanRemoval = true)
    @JoinColumn(name = "profileImage",referencedColumnName = "id")
    private Media profileImage;

}
