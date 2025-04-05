package TTCS.TTCS_ThayPhuong.Dto.Request;

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
    private String password;
}
