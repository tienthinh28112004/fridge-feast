package TTCS.TTCS_ThayPhuong.Entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "categories")
@Entity(name = "Category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

}
