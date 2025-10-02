package TTCS.TTCS_ThayPhuong.Dto.Response;

import TTCS.TTCS_ThayPhuong.Entity.User;
import TTCS.TTCS_ThayPhuong.Enums.StatusRegisterSupplier;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterSupplierResponse {
    private Long id;
    private String email;
    private String name;
    private String phone;
    private String expertise;
    private Double yearsOfExperience;
    private String bio;
    private String facebookLink;
    private String certificate;
    private String cvUrl;
    private StatusRegisterSupplier statusRegisterSupplier;

    public static UserRegisterSupplierResponse convert(User user){
        return UserRegisterSupplierResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getFullName())
                .phone(user.getPhoneNumber())
                .expertise(user.getExpertise())
                .yearsOfExperience(user.getYearsOfExperience())
                .bio(user.getBio())
                .facebookLink(user.getFacebookLink())
                .certificate(user.getCertificate())
                .cvUrl(user.getCvUrl())
                .statusRegisterSupplier(user.getStatusRegisterSupplier())
                .build();
    }
}
