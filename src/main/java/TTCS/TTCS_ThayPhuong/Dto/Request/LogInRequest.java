package TTCS.TTCS_ThayPhuong.Dto.Request;

import TTCS.TTCS_ThayPhuong.Validator.CustomPasswordValidator;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogInRequest {
    @NotBlank
    private String email;

    @NotBlank
    @CustomPasswordValidator
    private String password;
}
