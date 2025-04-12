package TTCS.TTCS_ThayPhuong.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "supplier_has_ingredient", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"supplier_id", "ingredient_id"})
})
@Entity(name = "SupplierHasIngredient")
public class SupplierHasIngredient extends AbstractEntity<Long>{

    @Column(name = "stock",nullable = false)
    private Long stock;

    @Column(name = "price",nullable = false)
    private Long price;

    @ManyToOne
    @JoinColumn(name = "supplier_id",nullable = false)
    @JsonBackReference
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "ingredient_id",nullable = false)
    @JsonBackReference
    private Ingredient ingredient;
}