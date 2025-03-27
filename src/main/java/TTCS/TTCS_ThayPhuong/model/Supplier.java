package TTCS.TTCS_ThayPhuong.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    String full_name;

    @NotNull
    @Email
    String email;

    @NotNull
    @JsonIgnore
    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;

    @Size(min = 10, message = "PHONE_NUMBER_INVALID")
    String phone_number;

    String address;

    boolean is_active;

    String avatar_url;

    Float rating;

    @CreationTimestamp
    LocalDateTime create_at;

    @UpdateTimestamp
    LocalDateTime updated_at;

    @OneToMany(mappedBy = "supplier")
    List<CartDetail> cartDetails = new ArrayList<>();

    @OneToMany(mappedBy = "supplier")
    List<SupplierHasIngredient> supplierHasIngredients = new ArrayList<>();

    @OneToMany(mappedBy = "supplier")
    List<OrderDetail> orderDetails = new ArrayList<>();
}
