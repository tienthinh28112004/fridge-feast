package TTCS.TTCS_ThayPhuong.Dto.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailRequest {
    @NotNull(message = "supplierHasIngredientId cannot be null")
    @Min(value = 1,message = "supplierHasIngredientId must be greater than 0")
    private Long supplierHasIngredientId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1,message = "Quantity must be greater than 0")
    private Long quantity;
}
