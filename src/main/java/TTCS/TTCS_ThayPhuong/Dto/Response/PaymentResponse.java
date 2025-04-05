package TTCS.TTCS_ThayPhuong.Dto.Response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {
    private String code;
    private String message;
    private String paymentUrl;
}
