package TTCS.TTCS_ThayPhuong.Dto.Request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterSupplierRequest {
    @NotBlank(message = "supplierName cannot be null")
    private String supplierName;

    @NotBlank(message = "address cannot be null")
    private String address;

    @NotBlank(message = "facebookLink cannot be null")
    private String facebookLink;

    @NotBlank(message = "phoneNumber cannot be null")
    private String phoneNumber;

//    private String avatarUrl;được cập nhật trong controller
//
//    private String resumeUrl;//được cập nhật trong controller

    private Double latitude;

    private Double longitude;

}
