package TTCS.TTCS_ThayPhuong.Service;

import TTCS.TTCS_ThayPhuong.Dto.Request.IngredientCreateRequest;
import TTCS.TTCS_ThayPhuong.Dto.Request.IngredientUpdateRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.IngredientResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IngredientService {
    IngredientResponse createIngredient(IngredientCreateRequest request, MultipartFile ingredientPdf);

    IngredientResponse getById(Long ingredientId);

    List<IngredientResponse> getAllIngredient();

    IngredientResponse updateIngredient(Long ingredientId, IngredientUpdateRequest request,MultipartFile ingredientPdf);

    IngredientResponse softDeleteIngredient(Long ingredientId);

    void hardDeleteIngredient(Long ingredientId);
}
