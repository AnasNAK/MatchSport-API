package NAK.MatchSport_API.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "admin")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper= true)
@ToString(callSuper=true)
@PrimaryKeyJoinColumn(name = "adminId")

public class Admin extends User {
}
