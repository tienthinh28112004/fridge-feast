package TTCS.TTCS_ThayPhuong.Service;

import TTCS.TTCS_ThayPhuong.Dto.Request.CategoryRequest;
import TTCS.TTCS_ThayPhuong.Entity.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(CategoryRequest request);
    Category getCategoryById(Long categoryId);
    List<Category> getAllCategories();
    void deleteCategory(Long categoryId);
}
