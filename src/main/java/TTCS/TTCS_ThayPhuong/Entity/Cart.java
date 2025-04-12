package TTCS.TTCS_ThayPhuong.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "Cart")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "carts")
public class Cart extends AbstractEntity<Long>{
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",nullable = false)
    @JsonBackReference
    private User user;

    @Column(name="total_money",nullable = false)
    private Long totalMoney;//price * quantity

    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,orphanRemoval = true)//nếu có thay đổi nó sẽ tự động câập nhật trong csdl và trong cả orderdetail
    @JsonManagedReference
    private List<CartDetail> cartDetails;
}
