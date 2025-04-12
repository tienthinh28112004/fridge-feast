package TTCS.TTCS_ThayPhuong.Dto.Response;

import TTCS.TTCS_ThayPhuong.Entity.Order;
import TTCS.TTCS_ThayPhuong.Entity.OrderDetail;
import TTCS.TTCS_ThayPhuong.Enums.OrderStatus;
import TTCS.TTCS_ThayPhuong.Enums.PaymentExpression;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class OrderResponse {
    private Long orderId;

    private String fullName;

    private String phoneNumber;

    private String address;

    private String note;

    private LocalDateTime dateTime;

    private String email;

    private OrderStatus orderStatus;

    private PaymentExpression paymentExpression;

    private Long totalMoney;

    private List<IngredientBySupplierResponse> detailResponse;

    public static OrderResponse convert(Order order){
        Long totalMoney =0L;
        for(OrderDetail orderDetail: order.getOrderDetails()){
            totalMoney+=orderDetail.getOrder().getTotalMoney();
        }
        log.info("tiền{}",totalMoney);
        return OrderResponse.builder()
                .orderId(order.getId())
                .address(order.getAddress())
                .fullName(order.getFullName())
                .note(order.getNote())
                .phoneNumber(order.getPhoneNumber())
                .dateTime(order.getOrderDate())
                .orderStatus(order.getOrderStatus())
                .email(order.getUser().getEmail())
                .paymentExpression(order.getPaymentExpression())
                .totalMoney(totalMoney)
                .detailResponse(order.getOrderDetails().stream().map(IngredientBySupplierResponse::convertOrder)
                        .collect(Collectors.toList()))
                .build();
    }
}

