package TTCS.TTCS_ThayPhuong.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "dish_has_category")
@Table(name = "DishHasCategory")
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
