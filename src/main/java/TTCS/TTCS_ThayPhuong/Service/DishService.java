package TTCS.TTCS_ThayPhuong.Service;

import TTCS.TTCS_ThayPhuong.Dto.Request.DishCreationRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.CommentResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.DishResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.PageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DishService {
    DishResponse uploadDish(DishCreationRequest request, MultipartFile dishImage);
    DishResponse getDishId(Long dishId);
    PageResponse<List<DishResponse>> getAllDish(int page, int size);
    PageResponse<List<DishResponse>> getDishlWithSortAndMultiFieldAndSearch(int page, int size, List<String> sortBy, String...search);
    PageResponse<List<DishResponse>> getDishWithSortAndSearchByKeyword(int page, int size, String keyword);

}
