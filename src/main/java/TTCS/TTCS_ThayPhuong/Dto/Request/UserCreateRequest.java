package TTCS.TTCS_ThayPhuong.Dto.Request;

import TTCS.TTCS_ThayPhuong.Validator.CustomPasswordValidator;
import TTCS.TTCS_ThayPhuong.Validator.MinSizeValidator;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequest {
    @NotBlank
    @MinSizeValidator(min=8)
    private String fullName;

    @NotBlank
    @MinSizeValidator(min=8)
    private String email;

    @NotBlank
    @CustomPasswordValidator()
    private String password;
}
