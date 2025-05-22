package TTCS.TTCS_ThayPhuong.Controller;

import TTCS.TTCS_ThayPhuong.Dto.Request.CartAddItemRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.ApiResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.CartTotalResponse;
import TTCS.TTCS_ThayPhuong.Entity.CartDetail;
import TTCS.TTCS_ThayPhuong.Service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CartController {

    private final CartService cartService;
    @PostMapping("/addOrUpdateItemCart")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<CartTotalResponse> addOrUpdateItemCart(
            @RequestBody CartAddItemRequest request) {
        return ApiResponse.<CartTotalResponse>builder()
                .message("add or update cart Successfully")
                .result(cartService.addOrUpdateItemCart(request))
                .build();
    }
    @DeleteMapping("/deleteItem/{ingredientId}")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<?> deleteIngredientByCart(
            @PathVariable("ingredientId") Long ingredientId) {
        cartService.deleteItemCart(ingredientId);
        return ApiResponse.<Void>builder()
                .message("Delete ingredient by cart successfully")
                .build();
    }
    @DeleteMapping("/deleteAll")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<?> deleteAllIngredientByCart() {
        cartService.clearAllItemCart();
        return ApiResponse.<Void>builder()
                .message("Delete all ingredient by cart successfully")
                .build();
    }

    @GetMapping("/detailCart")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<List<CartDetail>> getDetailCart() {
        return ApiResponse.<List<CartDetail>>builder()
                .message("Get detail cart successfully")
                .result(cartService.getDetailCart())
                .build();
    }

}
