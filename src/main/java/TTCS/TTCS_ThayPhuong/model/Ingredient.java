package TTCS.TTCS_ThayPhuong.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    String name;

    String description;

    String ingredient_image;

    @CurrentTimestamp
    LocalDateTime created_at;

    @CurrentTimestamp
    LocalDateTime updated_at;

    @OneToMany(mappedBy = "ingredient")
    List<SupplierHasIngredient> supplierHasIngredientList = new ArrayList<>();

    @OneToMany(mappedBy = "ingredient")
    List<DishHasIngredient> dishHasIngredientList = new ArrayList<>();

    @OneToMany(mappedBy = "ingredient")
    List<UserHasIngredient> userHasIngredientList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "ingredient")
    List<OrderDetail> orderDetails = new ArrayList<>();
}
