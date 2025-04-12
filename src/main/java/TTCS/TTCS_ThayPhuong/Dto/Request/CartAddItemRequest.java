package TTCS.TTCS_ThayPhuong.Dto.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartAddItemRequest {

    @NotNull(message = "supplierHasIngredientI cannot be null")
    @Min(value = 1,message = "supplierHasIngredientI must be greater than 0")
    private Long supplierHasIngredientId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1,message = "Quantity must be greater than 0")
    private Long quantity;
}
