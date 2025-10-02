package TTCS.TTCS_ThayPhuong.Entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "DishHasCategory")
@Table(name = "dish_has_category")
public class DishHasCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dish_id",nullable = false)
    private Dish dish;

    @ManyToOne
    @JoinColumn(name = "categories_id",nullable = false)
    private Category category;
}
