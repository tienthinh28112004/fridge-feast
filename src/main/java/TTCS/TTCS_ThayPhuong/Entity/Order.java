package TTCS.TTCS_ThayPhuong.Entity;

import TTCS.TTCS_ThayPhuong.Enums.OrderStatus;
import TTCS.TTCS_ThayPhuong.Enums.PaymentExpression;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order extends AbstractEntity<Long>{
    @Column(name = "fullName",length = 100)
    private String fullName;

    @Column(name = "phone_number",nullable = false,length = 100)
    private String phoneNumber;

    @Column(name = "address",length = 100)
    private String address;

    @Column(name="note",length = 200)
    private String note;

    @Column(name="order_date",nullable = false)
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status",nullable = false)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_expression",nullable = false)
    private PaymentExpression paymentExpression;

    @JoinColumn(name="total_money",nullable = false)
    private long totalMoney;//tổng tiền của tất cả ddiown hàng

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    @JsonBackReference
    // Thêm annotation này để tránh lặp vô hạn khi serialize(cho vào phòng hờ thôi về chủ yếu mình đưa ra userResponse)
    private User user;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)//nếu có thay đổi nó sẽ tự động câập nhật trong csdl và trong cả orderdetail
    private List<OrderDetail> orderDetails;
}
