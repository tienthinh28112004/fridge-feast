package TTCS.TTCS_ThayPhuong.Controller;

import TTCS.TTCS_ThayPhuong.Dto.Request.OrderRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.ApiResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.OrderResponse;
import TTCS.TTCS_ThayPhuong.Enums.OrderStatus;
import TTCS.TTCS_ThayPhuong.Enums.PaymentExpression;
import TTCS.TTCS_ThayPhuong.Service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@Slf4j
@Validated
public class OrderController {
    public final OrderService orderService;

    @PostMapping("/createOrder")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        return ApiResponse.<OrderResponse>builder()
                .message("Create order successfully")
                .result(orderService.createOrder(orderRequest))
                .build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/getOrder/{id}")
    public ApiResponse<OrderResponse> getOrderById(@PathVariable Long id) {
        return ApiResponse.<OrderResponse>builder()
                .message("Detail order successfully")
                .result(orderService.informationOrder(id))
                .build();
    }


    @GetMapping("/getHistoryOrder")
    @PreAuthorize("isAuthenticate() or hasAuthority('ADMIN')")
    public ApiResponse<List<OrderResponse>> getHistoryOrder() {
        return ApiResponse.<List<OrderResponse>>builder()
                .message("All history order")
                .result(orderService.findOrderByUser())
                .build();
    }
    @GetMapping("/getOrderRecent")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<List<OrderResponse>> getOrderRecent(
            @RequestParam(defaultValue = "1",required = false) int page,
            @RequestParam(defaultValue = "10",required = false) int size
    ) {
        return ApiResponse.<List<OrderResponse>>builder()
                .message("List order by createdAt by desc")
                .result(orderService.orderRecent(page,size))
                .build();
    }

    @PatchMapping("/{orderId}/orderStatus")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String newStatus
    ) {
        orderService.updateOrderStatus(orderId, OrderStatus.valueOf(newStatus.toUpperCase()));
        log.info("orderId {}",orderId);
        return ApiResponse.<Void>builder()
                .message("change orderStatus successfully")
                .build();
    }

    @PatchMapping("/{orderId}/paymentStatus")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<?> updateStatusPayment(
            @PathVariable Long orderId,
            @RequestParam String newStatus
    ) {
        orderService.updatePaymentExpression(orderId, PaymentExpression.valueOf(newStatus));
        return ApiResponse.<Void>builder()
                .message("update Status Payment")
                .build();
    }
}
