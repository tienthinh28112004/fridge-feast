package TTCS.TTCS_ThayPhuong.Dto.Response;

import TTCS.TTCS_ThayPhuong.Entity.User;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private boolean isActive;
    private LocalDate dob;
    private String avatarUrl;
    private Double latitude;
    private Double longitude;
    private LocalDate createdAt;
    private List<String> roles;
    //private List<IngredientBySupplierResponse> cartDetailList=new ArrayList<>();

    public static UserResponse convert(User user){
        List<String> rolelist=new ArrayList<>();
        user.getUserHasRoles().stream().map(userHasRole -> userHasRole.getRole().getName()).forEach(rolelist::add);
        return UserResponse.builder()
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .createdAt(LocalDate.from(user.getCreatedAt()))
                .isActive(user.isActive())
                .phoneNumber(user.getPhoneNumber()!=null?user.getPhoneNumber():"N/A")
                .avatarUrl(user.getAvatarUrl())
                .dob(user.getDob())
                .latitude(user.getLatitude())
                .longitude(user.getLongitude())
                .roles(rolelist)
                //.cartDetailList(user.getCart().getCartDetails().stream().map(IngredientBySupplierResponse::convertCart).collect(Collectors.toList()))
                .build();
    }
}
