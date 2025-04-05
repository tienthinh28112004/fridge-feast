package TTCS.TTCS_ThayPhuong.Dto.Response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenResponse {
    private Long userId;

    private String accessToken;
}
