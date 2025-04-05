package TTCS.TTCS_ThayPhuong.Dto.Response;

import TTCS.TTCS_ThayPhuong.Entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String fullName;
    private String email;
    private String phoneNumber;
    private boolean isActive;
    private LocalDateTime dob;
    private String avatarUrl;
    private Double latitude;
    private Double longitude;

    public static UserResponse convert(User user){
        return UserResponse.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .isActive(user.isActive())
                .phoneNumber(user.getPhoneNumber()!=null?user.getPhoneNumber():"")
                .avatarUrl(user.getAvatarUrl())
                .dob(user.getDob())
                .latitude(user.getLatitude())
                .longitude(user.getLongitude())
                .build();
    }
}
