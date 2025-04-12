package TTCS.TTCS_ThayPhuong.Dto.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntrospectRequest {
    @NotBlank
    private String accessToken;
}
