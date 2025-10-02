package TTCS.TTCS_ThayPhuong.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart_details")
public class CartDetail extends AbstractEntity<Long>{
    @ManyToOne(fetch = FetchType.LAZY)//nên để lazy bởi đây lag bảng trung gian không cần thiết thì chưa cần tải về
    @JoinColumn(name="cart_id",nullable = false)
    @JsonBackReference
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="supplier_has_ingredient_id", nullable = false)
    private SupplierHasIngredient supplierHasIngredient;

    @Column(name="quantity",nullable = false)
    private Long quantity;

    @Column(name="total_money",nullable = false)
    private Long totalMoneyIngedient;//price * quantity
}
