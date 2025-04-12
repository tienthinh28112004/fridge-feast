package TTCS.TTCS_ThayPhuong.Entity;

import TTCS.TTCS_ThayPhuong.Enums.StatusRegisterSupplier;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "Supplier")
@Table(name = "supplier")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Supplier extends AbstractEntity<Long>{
    @Column(name = "supplier_name",nullable = false)
    private String supplierName;

    @Column(name = "address")
    private String address;

    @Column(name = "facebook_link")
    private String facebookLink;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "resume_url")
    private String resumeUrl;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_register_supplier")
    private StatusRegisterSupplier statusRegisterSupplier;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "supplier",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<SupplierHasIngredient> supplierHasIngredients;
}
