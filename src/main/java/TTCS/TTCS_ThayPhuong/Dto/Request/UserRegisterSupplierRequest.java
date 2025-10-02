package TTCS.TTCS_ThayPhuong.Dto.Request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterSupplierRequest {
    private String email;
    private String name;
    private String phone;
    private String expertise;
    private Double yearsOfExperience;
    private String bio;
    private String facebookLink;
}

