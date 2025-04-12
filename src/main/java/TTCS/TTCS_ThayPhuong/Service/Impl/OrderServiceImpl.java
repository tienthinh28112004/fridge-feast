package TTCS.TTCS_ThayPhuong.Service.Impl;

import TTCS.TTCS_ThayPhuong.Dto.Request.OrderDetailRequest;
import TTCS.TTCS_ThayPhuong.Dto.Request.OrderRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.OrderResponse;
import TTCS.TTCS_ThayPhuong.Entity.Order;
import TTCS.TTCS_ThayPhuong.Entity.OrderDetail;
import TTCS.TTCS_ThayPhuong.Entity.SupplierHasIngredient;
import TTCS.TTCS_ThayPhuong.Entity.User;
import TTCS.TTCS_ThayPhuong.Enums.OrderStatus;
import TTCS.TTCS_ThayPhuong.Enums.PaymentExpression;
import TTCS.TTCS_ThayPhuong.Exception.BadRequestException;
import TTCS.TTCS_ThayPhuong.Exception.NotFoundException;
import TTCS.TTCS_ThayPhuong.Exception.TokenExpireException;
import TTCS.TTCS_ThayPhuong.Repository.OrderRepository;
import TTCS.TTCS_ThayPhuong.Repository.SupplierHasIngredientRepository;
import TTCS.TTCS_ThayPhuong.Repository.UserRepository;
import TTCS.TTCS_ThayPhuong.Service.OrderService;
import TTCS.TTCS_ThayPhuong.Util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final SupplierHasIngredientRepository supplierHasIngredientRepository;
    @Override
    public OrderResponse createOrder(OrderRequest request) {
        String email= SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new TokenExpireException("Bạn chưa đăng nhập"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));

        Order order =Order.builder()
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .note(request.getNote())
                .paymentExpression(PaymentExpression.DIRECT)
                .orderStatus(OrderStatus.PENDDING)
                .user(user)
                .orderDate(LocalDateTime.now())
                .build();
        orderRepository.save(order);
        List<OrderDetail> orderDetails=new ArrayList<>();
        long totalMoney=0;
        for(OrderDetailRequest x:request.getDetailRequests()) {
            SupplierHasIngredient hasIngredient=supplierHasIngredientRepository.findById(x.getSupplierHasIngredientId())
                    .orElseThrow(()->new NotFoundException("Nguyên liệu của nhà cung cấp không được tìm thấy"));

            long totalMoneyIngredient=hasIngredient.getPrice()*x.getQuantity();
            totalMoney+=totalMoneyIngredient;
            orderDetails.add(OrderDetail.builder()
                        .order(order)
                        .supplierHasIngredient(hasIngredient)
                        .quantity(x.getQuantity())
                        .totalMoneyIngredient(totalMoneyIngredient)
                    .build());
        }
        order.setOrderDetails(orderDetails);
        order.setTotalMoney(totalMoney);
        orderRepository.save(order);
        return OrderResponse.convert(order);
    }

    @Override
    public List<OrderResponse> findOrderByUser() {
        String email= SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new TokenExpireException("Bạn chưa đăng nhập"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));
        List<Order> orders=orderRepository.findOrderByUserId(user.getId());
        return orders.stream().map(OrderResponse::convert).collect(Collectors.toList());
    }

    @Override
    public OrderResponse informationOrder(Long orderId) {
        Order order=orderRepository.findById(orderId)
                .orElseThrow(()-> new NotFoundException("Order not found"));
        return OrderResponse.convert(order);
    }

    @Override
    public void updateOrderStatus(Long orderId, OrderStatus newStatus) {//dành cho admin
        Order order=orderRepository.findById(orderId)
                .orElseThrow(()->new NotFoundException("Order not found"));
        order.setOrderStatus(newStatus);
        orderRepository.save(order);
    }

    @Override
    public void updatePaymentExpression(Long orderId, PaymentExpression newPayment) {
        String email= SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new TokenExpireException("Bạn chưa đăng nhập"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));
        Order order=orderRepository.findById(orderId)
                .orElseThrow(()->new NotFoundException("Order not found"));
        if(!Objects.equals(user.getId(),order.getUser().getId())){
            throw new BadRequestException("Email của bạn không trùng với email của người đặt đơn hàng trong hệ thống");
        }
        order.setPaymentExpression(newPayment);
        orderRepository.save(order);
    }

    @Override
    public List<OrderResponse> orderRecent(int page, int size) {
        Sort sort=Sort.by(Sort.Direction.DESC,"createdAt");
        Pageable pageable= PageRequest.of(page-1,size,sort);
        Page<Order> orderPage = orderRepository.findAll(pageable);
        return orderPage.stream().map(OrderResponse::convert).collect(Collectors.toList());
    }
}
