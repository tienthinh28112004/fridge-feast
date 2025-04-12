package TTCS.TTCS_ThayPhuong.Controller;

import TTCS.TTCS_ThayPhuong.Dto.Request.SupplierHasIngredientRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.ApiResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.IngredientDetailResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.PageResponse;
import TTCS.TTCS_ThayPhuong.Service.IngredientBySupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/v1/ingredientBySupplier")
@RequiredArgsConstructor
@Slf4j
@Validated
public class IngredientBySupplierController {
    private final IngredientBySupplierService ingredientBySupplierService;
    @GetMapping("/getIngredientById/{ingredientId}")
    public ApiResponse<IngredientDetailResponse> getIngredientById(
            @PathVariable("ingredientId") Long ingredientId
    ){
        return ApiResponse.<IngredientDetailResponse>builder()
                .message("Ingredient by id successfully")
                .result(ingredientBySupplierService.getIngredientDetailId(ingredientId))
                .build();
    }

    @PostMapping("/supplierUploadIngredient")
    @PreAuthorize("hasAuthority('SUPPLIER') or hasAuthority('ADMIN')")
    public ApiResponse<IngredientDetailResponse> addIngredient(
            @RequestBody SupplierHasIngredientRequest request
    ){
        return ApiResponse.<IngredientDetailResponse>builder()
                .message("Add ingredient successfully")
                .result(ingredientBySupplierService.supplierUpLoadIngredient(request))
                .build();
    }
    @GetMapping("/getAllIngredientBySupplier")
    public ApiResponse<PageResponse<List<IngredientDetailResponse>>> getAllIngredientBySupplier(
            @RequestParam(defaultValue = "1") int page,
            @RequestHeader(defaultValue = "10") int size
    ){
        return ApiResponse.<PageResponse<List<IngredientDetailResponse>>>builder()
                .message("Get all ingredient supplier")
                .result(ingredientBySupplierService.getAllIngredientBySupplier(page,size))
                .build();
    }

    @GetMapping("/getIngredientWithSortAndMultiFieldAndSearch")
    public ApiResponse<PageResponse<List<IngredientDetailResponse>>> getIngredientWithSortAndMultiFieldAndSearch(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String ...sortBy
    ){
        return ApiResponse.<PageResponse<List<IngredientDetailResponse>>>builder()
                .message("Search detail ingredient")
                .result(ingredientBySupplierService.getIngredientWithSortAndMultiFieldAndSearch(page, size, keyword, sortBy))
                .build();
    }
}
