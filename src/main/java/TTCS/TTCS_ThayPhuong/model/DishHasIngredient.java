package TTCS.TTCS_ThayPhuong.model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DishHasIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "dish_id", referencedColumnName = "id")
    Dish dish;

    @ManyToOne
    @JoinColumn(name = "ingredient_id", referencedColumnName = "id")
    Ingredient ingredient;

    int quantity;

    String unit;
}
