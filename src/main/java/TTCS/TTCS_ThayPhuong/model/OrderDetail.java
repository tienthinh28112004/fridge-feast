package TTCS.TTCS_ThayPhuong.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CurrentTimestamp;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Positive
    Long quantity;

    @Positive
    float price;

    @Positive
    float total_money;

    @CurrentTimestamp
    LocalDateTime created_at;

    @CurrentTimestamp
    LocalDateTime updated_at;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    Order order;

    @ManyToOne
    @JoinColumn(name = "ingredient_id", referencedColumnName = "id")
    Ingredient ingredient;

    @ManyToOne
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    Supplier supplier;
}
