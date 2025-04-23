package TTCS.TTCS_ThayPhuong.Dto.Response;

import TTCS.TTCS_ThayPhuong.Entity.Supplier;
import TTCS.TTCS_ThayPhuong.Enums.StatusRegisterSupplier;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierResponse {
    private String userName;

    private String supplierName;

    private String address;

    private StatusRegisterSupplier statusRegisterSupplier;

    private String facebookLink;

    private String phoneNumber;

    private String avatarUrl;

    private String resumeUrl;

    private Double latitude;

    private Double longitude;

    public static SupplierResponse convert(Supplier supplier){
        return SupplierResponse.builder()
                .userName(supplier.getUser().getFullName())
                .supplierName(supplier.getSupplierName())
                .address(supplier.getAddress())
                .statusRegisterSupplier(supplier.getStatusRegisterSupplier())
                .facebookLink(supplier.getFacebookLink())
                .phoneNumber(supplier.getPhoneNumber())
                .avatarUrl(supplier.getAvatarUrl())
                .resumeUrl(supplier.getResumeUrl())
                .latitude(supplier.getLatitude())
                .longitude(supplier.getLongitude())
                .build();
    }
}
