package TTCS.TTCS_ThayPhuong.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity(name = "Roles")
@Table(name = "roles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Roles extends AbstractEntity<Long>{
    @Column(name = "name",nullable = false,unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "role")
    private Set<UserHasRole> userHasRoles;
}
