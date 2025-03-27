package TTCS.TTCS_ThayPhuong.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    Long ingredientId;

    @NotNull
    @Positive
    Long quantity = 1L;

    @NotNull
    @Positive
    Long price;

    @NotNull
    @Positive
    Long totalMoney;

    LocalDateTime created_at;

    LocalDateTime updated_at;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id", nullable = false)
    Cart cart;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "supplier_id", nullable = false)
    Supplier supplier;
}
