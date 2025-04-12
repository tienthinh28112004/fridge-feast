package TTCS.TTCS_ThayPhuong.Controller;

import TTCS.TTCS_ThayPhuong.Dto.Request.DishCreationRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.ApiResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.DishResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.PageResponse;
import TTCS.TTCS_ThayPhuong.Service.DishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dish")
@RequiredArgsConstructor
@Slf4j
@Validated
public class DishController {
    private final DishService dishService;

    @GetMapping("/getDishWithSortAndMultiFieldAndSearch")
    public ApiResponse<PageResponse<List<DishResponse>>> getDishWithSortAndMultiFieldAndSearch(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) List<String> sortBy,
            @RequestParam(required = false) String ...search
    ){
        return ApiResponse.<PageResponse<List<DishResponse>>>builder()
                .message("Search dish")
                .result(dishService.getDishlWithSortAndMultiFieldAndSearch(page,size,sortBy,search))
                .build();
    }
    @PostMapping("/uploadDish")
    @PreAuthorize("hasAuthority('SUPPLIER') or hasAuthority('ADMIN')")
    public ApiResponse<DishResponse> uploadDish(
            @RequestPart(required = false) MultipartFile dishImage,
            @RequestPart @Validated DishCreationRequest request
    ){
    return ApiResponse.<DishResponse>builder()
            .message("Upload dish")
            .result(dishService.uploadDish(request,dishImage))
            .build();
    }
    @GetMapping("/getDishById/{dishId}")
    public ApiResponse<DishResponse> getDishById(
            @PathVariable("dishId") Long dishId
    ){
        return ApiResponse.<DishResponse>builder()
                .message("Dish by id successfully")
                .result(dishService.getDishId(dishId))
                .build();
    }

    @GetMapping("/getAllDish")
    public ApiResponse<PageResponse<List<DishResponse>>> getAllDish(
           @RequestParam(defaultValue = "1",required = false) int page,
           @RequestParam(defaultValue = "10",required = false) int size
    ){
        return ApiResponse.<PageResponse<List<DishResponse>>>builder()
                .message("All dish successfully")
                .result(dishService.getAllDish(page, size))
                .build();
    }

}
