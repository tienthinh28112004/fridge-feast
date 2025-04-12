package TTCS.TTCS_ThayPhuong.Controller;

import TTCS.TTCS_ThayPhuong.Dto.Request.IngredientCreateRequest;
import TTCS.TTCS_ThayPhuong.Dto.Request.IngredientUpdateRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.ApiResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.DishResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.IngredientResponse;
import TTCS.TTCS_ThayPhuong.Service.IngredientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ingredient")
@RequiredArgsConstructor
@Slf4j
@Validated
public class IngredientController {
    private final IngredientService ingredientService;
    @PostMapping("/addIngredient")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<IngredientResponse> addIngredient(
            @RequestPart MultipartFile ingredientPdf,
            @RequestPart IngredientCreateRequest request
    ){
        return ApiResponse.<IngredientResponse>builder()
                .message("Add ingredient successfully")
                .result(ingredientService.createIngredient(request,ingredientPdf))
                .build();
    }
    @GetMapping("/getIngredientById/{ingredientId}")
    public ApiResponse<IngredientResponse> getIngredientById(
            @PathVariable("ingredientId") Long ingredientId
    ){
        return ApiResponse.<IngredientResponse>builder()
                .message("Ingredient by id successfully")
                .result(ingredientService.getById(ingredientId))
                .build();
    }

    @GetMapping("/getAllIngredient")
    @PreAuthorize("hasAuthority('SUPPLIER') or hasAuthority('ADMIN')")
    public ApiResponse<List<IngredientResponse>> getAllIngredient(){
        return ApiResponse.<List<IngredientResponse>>builder()
                .message("List all ingredient successfully")
                .result(ingredientService.getAllIngredient())
                .build();
    }
    @PatchMapping("/updateIngredient/{ingredientId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<IngredientResponse> updateIngredient(
            @PathVariable("ingredientId") Long ingredientId,
            @RequestPart MultipartFile ingredientPdf,
            @RequestPart IngredientUpdateRequest request
    ){
        return ApiResponse.<IngredientResponse>builder()
                .message("Update ingredient successfully")
                .result(ingredientService.updateIngredient(ingredientId,request,ingredientPdf))
                .build();
    }
    @DeleteMapping("/deleteSoft/{ingredientId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<IngredientResponse> deleteSoftIngredient(
          @PathVariable("ingredientId") Long ingredientId
    ){
        return ApiResponse.<IngredientResponse>builder()
                .message("Delete soft ingredient successfully")
                .result(ingredientService.softDeleteIngredient(ingredientId))
                .build();
    }

    @DeleteMapping("/deleteHard/{ingredientId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<IngredientResponse> deleteHardIngredient(
            @PathVariable("ingredientId") Long ingredientId
    ){
        ingredientService.hardDeleteIngredient(ingredientId);
        return ApiResponse.<IngredientResponse>builder()
                .message("Delete hard ingredient successfully")
                .build();
    }
}
