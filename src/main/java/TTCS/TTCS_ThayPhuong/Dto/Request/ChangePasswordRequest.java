package TTCS.TTCS_ThayPhuong.Dto.Request;

import TTCS.TTCS_ThayPhuong.Validator.CustomPasswordValidator;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordRequest {
    @NotBlank(message = "email cannot be null")
    private String email;

    @CustomPasswordValidator
    private String password;

    @CustomPasswordValidator
    private String newPassword;

    @CustomPasswordValidator
    private String newPasswordConfirm;
}
