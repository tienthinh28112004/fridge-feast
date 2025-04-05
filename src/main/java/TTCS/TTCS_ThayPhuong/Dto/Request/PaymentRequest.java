package TTCS.TTCS_ThayPhuong.Dto.Request;

import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {
    @Min(value = 0,message = "Amount must be greater than 0")
    private Long amount;
}
