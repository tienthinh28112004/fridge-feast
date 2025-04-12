package TTCS.TTCS_ThayPhuong.Service;

import TTCS.TTCS_ThayPhuong.Dto.Request.OrderRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.OrderResponse;
import TTCS.TTCS_ThayPhuong.Enums.OrderStatus;
import TTCS.TTCS_ThayPhuong.Enums.PaymentExpression;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest orderRequest);
    List<OrderResponse> findOrderByUser();
    OrderResponse informationOrder(Long orderId);

    void updateOrderStatus(Long orderId, OrderStatus newStatus);
    void updatePaymentExpression(Long orderId, PaymentExpression newPayment);
    List<OrderResponse> orderRecent(int page,int size);
}
