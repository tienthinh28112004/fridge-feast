package TTCS.TTCS_ThayPhuong.Service;

import TTCS.TTCS_ThayPhuong.Dto.Request.SupplierHasIngredientRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.IngredientDetailResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.PageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IngredientBySupplierService {
    IngredientDetailResponse supplierUpLoadIngredient(SupplierHasIngredientRequest request);
    IngredientDetailResponse getIngredientDetailId(Long id);
    PageResponse<List<IngredientDetailResponse>> getAllIngredientBySupplier(int page, int size);
    PageResponse<List<IngredientDetailResponse>> getIngredientWithSortAndMultiFieldAndSearch(int page, int size, String keyword, String...sortBy);
}
