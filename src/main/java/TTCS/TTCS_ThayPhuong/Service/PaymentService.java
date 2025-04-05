package TTCS.TTCS_ThayPhuong.Service;

import TTCS.TTCS_ThayPhuong.Dto.Request.PaymentRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.PaymentResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface PaymentService {
    PaymentResponse createVnPayPayment(PaymentRequest request, HttpServletRequest req);
}
