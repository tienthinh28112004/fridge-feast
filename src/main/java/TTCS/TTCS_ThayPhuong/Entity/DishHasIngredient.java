package TTCS.TTCS_ThayPhuong.Entity;

import jakarta.persistence.*;
import lombok.*;


@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "DishHasIngredient")
@Table(name = "dish_has_ingredient")
public class DishHasIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dish_id",nullable = false)
    private Dish dish;

    @ManyToOne
    @JoinColumn(name = "ingredient_id",nullable = false)
    private Ingredient ingredient;
}