package TTCS.TTCS_ThayPhuong.Dto.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
    @NotBlank(message = "fullName cannot be null")
    private String fullName;

    @NotBlank(message = "phoneNumber cannot be null")
    private String phoneNumber;

    @NotBlank(message = "address cannot be null")
    private String address;

    @NotBlank(message = "note cannot be null")
    private String note;

    private List<OrderDetailRequest> detailRequests;
}
