package TTCS.TTCS_ThayPhuong.Dto.Response;

import TTCS.TTCS_ThayPhuong.Entity.SupplierHasIngredient;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientDetailResponse {
    private Long ingredientId;
    private Long supplierId;
    private String nameIngredient;
    private String supplierName;
    private Long priceIngredient;
    private String ingredientUrl;
    private Long stock;

    public static IngredientDetailResponse convert(SupplierHasIngredient hasIngredient){
        return IngredientDetailResponse.builder()
                .ingredientId(hasIngredient.getIngredient().getId())
                .supplierId(hasIngredient.getSupplier().getId())
                .nameIngredient(hasIngredient.getIngredient().getName())
                .supplierName(hasIngredient.getSupplier().getSupplierName())
                .ingredientUrl(hasIngredient.getIngredient().getIngredientImage())
                .priceIngredient(hasIngredient.getPrice())
                .stock(hasIngredient.getStock())
                .build();
    }
}
