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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    String full_name;

    @NotNull
    @Email
    String email;

    @NotNull
    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;

    @Size(min = 10, message = "PHONE_NUMBER_INVALID")
    String phone_number;

    String refresh_token;

    boolean is_active;

    String avatar_url;

    LocalDate date_of_birth;

    LocalDateTime email_verified_at;

    @CreationTimestamp
    LocalDateTime create_at;

    @UpdateTimestamp
    LocalDateTime updated_at;

    double latitude;

    double longitude;

    @OneToOne(mappedBy = "user")
    Cart cart;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<Order> orders = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<UserHasIngredient> userHasIngredients = new ArrayList<>();
}
