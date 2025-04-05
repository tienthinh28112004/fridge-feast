package TTCS.TTCS_ThayPhuong.Service.Impl;

import TTCS.TTCS_ThayPhuong.Dto.Request.PaymentRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.PaymentResponse;
import TTCS.TTCS_ThayPhuong.Service.PaymentService;
import TTCS.TTCS_ThayPhuong.Util.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private  final VNPayUtil vnPayUtil;
    @Override
    public PaymentResponse createVnPayPayment(PaymentRequest request, HttpServletRequest req) {
        String paymentUrl=vnPayUtil.getPaymentURL(request.getAmount(), req);
        return PaymentResponse.builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl)
                .build();
    }
}
