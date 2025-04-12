package TTCS.TTCS_ThayPhuong.Dto.Response;

import TTCS.TTCS_ThayPhuong.Entity.Cart;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
public class CartTotalResponse implements Serializable {
    private Long cartId;
    private Long userId;
    private Long totalElements;
    private Long totalMoney;
    private List<IngredientBySupplierResponse> items;

    public static CartTotalResponse convert(Cart cart){
        return CartTotalResponse.builder()
                .cartId(cart.getId())
                .userId(cart.getUser().getId())
                .totalElements((long)cart.getCartDetails().size())
                .totalMoney(cart.getTotalMoney())
                .items(
                        cart.getCartDetails().stream().map(IngredientBySupplierResponse::convertCart)
                                .collect(Collectors.toList())
                )
                .build();
    }
}