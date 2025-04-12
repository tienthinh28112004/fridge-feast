package TTCS.TTCS_ThayPhuong.Controller;

import TTCS.TTCS_ThayPhuong.Dto.Request.CartAddItemRequest;
import TTCS.TTCS_ThayPhuong.Dto.Request.CategoryRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.ApiResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.CartTotalResponse;
import TTCS.TTCS_ThayPhuong.Entity.Category;
import TTCS.TTCS_ThayPhuong.Service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CategoryController {

    private final CategoryService categoryService;
    @PostMapping("/addCategory")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<Category> addCategory(
            @RequestBody CategoryRequest request) {
        return ApiResponse.<Category>builder()
                .message("Add category successfully")
                .result(categoryService.createCategory(request))
                .build();
    }
    @GetMapping("/categoryById/{categoryId}")
    public ApiResponse<Category> categoryById(
            @PathVariable("categoryId") Long categoryId) {
        return ApiResponse.<Category>builder()
                .message("Category by id")
                .result(categoryService.getCategoryById(categoryId))
                .build();
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/deleteCategory/{categoryId}")
    public ApiResponse<?> deleteCategory(
            @PathVariable("categoryId") Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ApiResponse.<Void>builder()
                .message("Delete category successfully")
                .build();
    }
    @PostMapping("/listCategory")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<List<Category>> getAllCategory() {
        return ApiResponse.<List<Category>>builder()
                .message("Get all category")
                .result(categoryService.getAllCategories())
                .build();
    }
}
