package TTCS.TTCS_ThayPhuong.Dto.Response;

import TTCS.TTCS_ThayPhuong.Entity.CartDetail;
import TTCS.TTCS_ThayPhuong.Entity.OrderDetail;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientBySupplierResponse implements Serializable {

    private Long ingredientId;
    private Long supplierId;
    private String nameIngredient;
    private String supplierName;
    private Long priceIngredient;
    private String thumbnail;
    private Long quantity;
    private Long totalPrice;

    public static IngredientBySupplierResponse convertCart(CartDetail cartDetail){
        return IngredientBySupplierResponse.builder()
                .ingredientId(cartDetail.getSupplierHasIngredient().getIngredient().getId())
                .supplierId(cartDetail.getSupplierHasIngredient().getSupplier().getId())
                .priceIngredient(cartDetail.getSupplierHasIngredient().getPrice())
                .thumbnail(cartDetail.getSupplierHasIngredient().getIngredient().getIngredientImage())
                .quantity(cartDetail.getQuantity())
                .totalPrice(cartDetail.getTotalMoneyIngedient())
                .build();
    }
    public static IngredientBySupplierResponse convertOrder(OrderDetail orderDetail){
        return IngredientBySupplierResponse.builder()
                .ingredientId(orderDetail.getSupplierHasIngredient().getIngredient().getId())
                .supplierId(orderDetail.getSupplierHasIngredient().getSupplier().getId())
                .priceIngredient(orderDetail.getSupplierHasIngredient().getPrice())
                .thumbnail(orderDetail.getSupplierHasIngredient().getIngredient().getIngredientImage())
                .quantity(orderDetail.getQuantity())
                .totalPrice(orderDetail.getTotalMoneyIngredient())
                .build();
    }
}
