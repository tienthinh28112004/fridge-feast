package TTCS.TTCS_ThayPhuong.Dto.Response;


import TTCS.TTCS_ThayPhuong.Entity.CartDetail;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse implements Serializable {

    private Long supplierHasIngredientId;///
    private String supplierName;
    private Long quantity;//
    private Long ingredientId;///
    private String nameIngredient;//
    private Long priceIngredient;////
    private String ingredientUrl;//
    private Long totalPrice;//

    public static CartItemResponse convert(CartDetail cartDetail){
        return CartItemResponse.builder()
                .supplierHasIngredientId(cartDetail.getSupplierHasIngredient().getId())
                .supplierName(cartDetail.getSupplierHasIngredient().getSupplier().getFullName()!=null?cartDetail.getSupplierHasIngredient().getSupplier().getFullName():"admin")
                .quantity(cartDetail.getQuantity())
                .ingredientId(cartDetail.getSupplierHasIngredient().getIngredient().getId())
                .nameIngredient(cartDetail.getSupplierHasIngredient().getIngredient().getName())
                .priceIngredient(cartDetail.getSupplierHasIngredient().getPrice())
                .ingredientUrl(cartDetail.getSupplierHasIngredient().getIngredient().getIngredientImage())
                .totalPrice(cartDetail.getTotalMoneyIngedient())
                .build();
    }
}
