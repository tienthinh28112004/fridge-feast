package TTCS.TTCS_ThayPhuong.Dto.Response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInResponse {
    private Long userid;

    private String accessToken;

    private Long accessTokenExpireIn;

    private List<String> roles;
}
