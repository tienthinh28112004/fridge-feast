package TTCS.TTCS_ThayPhuong.Dto.Request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierHasIngredientRequest {
    private Long ingredientId;

    private Long stock;

    private Long price;
}
