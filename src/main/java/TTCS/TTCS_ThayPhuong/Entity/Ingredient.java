package TTCS.TTCS_ThayPhuong.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "Ingredient")
@Table(name = "ingredients")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Ingredient extends AbstractEntity<Long>{
    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "description",columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "unit")
    private String unit;

    @Column(name = "ingredient_image",nullable = false)
    private String ingredientImage;

    @Column(name = "is_active")
    private boolean isActive;

    @OneToMany(mappedBy = "ingredient",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<SupplierHasIngredient> supplierHasIngredients;
}
