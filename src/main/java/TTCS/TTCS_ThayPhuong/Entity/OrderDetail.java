package TTCS.TTCS_ThayPhuong.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_details")
public class OrderDetail extends AbstractEntity<Long>{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id",nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="supplier_has_ingredient_id", nullable = false)
    private SupplierHasIngredient supplierHasIngredient;

    @Column(name="quantity",nullable = false)
    private Long quantity;

    @Column(name="total_money",nullable = false)
    private Long totalMoneyIngredient;//book.price * quantity(tổng tiền của sách ấy nhân với số luọng

}
