package TTCS.TTCS_ThayPhuong.Controller;

import TTCS.TTCS_ThayPhuong.Dto.Request.PaymentRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.ApiResponse;
import TTCS.TTCS_ThayPhuong.Service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/create_payment")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<?> createPayment(HttpServletRequest req,
                                        @RequestBody @Valid PaymentRequest request) throws UnsupportedEncodingException {
        return ApiResponse.builder()
                .message("Payment Successfully")
                .result(paymentService.createVnPayPayment(request,req))
                .build();
    }

    @GetMapping("/vn-pay-callback")//dữ liệu lấy xuống từ url
    public void handleVnPayCallback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String redirectUrl;
        String transactionStatus = request.getParameter("vnp_ResponseCode");
        if ("00".equals(transactionStatus)) {
            redirectUrl = "http://localhost:3000/payment-success";
        } else if ("24".equals(transactionStatus)) {
            redirectUrl = "http://localhost:3000/payment-cancel";
        } else {
            redirectUrl = "http://localhost:3000/payment-failed";
        }
        response.sendRedirect(redirectUrl);
    }
}
