package TTCS.TTCS_ThayPhuong.Service;

import TTCS.TTCS_ThayPhuong.Dto.Request.CartAddItemRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.CartTotalResponse;

public interface CartService {
    CartTotalResponse addOrUpdateItemCart(CartAddItemRequest request);
    void deleteItemCart(Long ingredientId);
    void clearAllItemCart();

}
