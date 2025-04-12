package TTCS.TTCS_ThayPhuong.Service.Impl;

import TTCS.TTCS_ThayPhuong.Dto.Request.DishCreationRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.DishResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.PageResponse;
import TTCS.TTCS_ThayPhuong.Entity.*;
import TTCS.TTCS_ThayPhuong.Exception.NotFoundException;
import TTCS.TTCS_ThayPhuong.Repository.CategoryRepository;
import TTCS.TTCS_ThayPhuong.Repository.DishRepository;
import TTCS.TTCS_ThayPhuong.Repository.IngredientRepository;
import TTCS.TTCS_ThayPhuong.Service.DishService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DishServiceImpl implements DishService {
    private final DishRepository dishRepository;
    private final CloudinaryService cloudinaryService;
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;
    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') and isAuthenticated()")
    public DishResponse uploadDish(DishCreationRequest request, MultipartFile dishImage) {
        String dishImageUrl=null;
        if(dishImage!=null){
            dishImageUrl=cloudinaryService.uploadImage(dishImage);
        }
        Dish dish=Dish.builder()
                .name(request.getName())
                .description(request.getDescription())
                .dishImage(dishImageUrl)
                .price(request.getPrice())
                .timeCook(request.getTimeCook())
                .recipe(request.getRecipe())
                .build();
        List<DishHasCategory> dishHasCategoryList=new ArrayList<>();
        for(Long x: request.getListCategoryId()){
            Category category = categoryRepository.findById(x)
                    .orElseThrow(()->new NotFoundException("Not found category"));
            dishHasCategoryList.add(
                    DishHasCategory.builder()
                            .category(category)
                            .dish(dish)
                            .build()
            );
        }
        List<DishHasIngredient> dishHasIngredients=new ArrayList<>();
        for(Long x :request.getListIngredientId()){
            Ingredient ingredient=ingredientRepository.findById(x)
                    .orElseThrow(()->new NotFoundException("Ingredient not found"));
            dishHasIngredients.add(DishHasIngredient.builder()
                            .dish(dish)
                            .ingredient(ingredient)
                            .build()
            );
        }
        dish.setDishHasIngredients(dishHasIngredients);
        dish.setDishHasCategoryList(dishHasCategoryList);
        dishRepository.save(dish);

        return DishResponse.convert(dish);
    }

    @Override
    public DishResponse getDishId(Long dishId) {
        return DishResponse.convert(dishRepository.findById(dishId)
                .orElseThrow(()->new NotFoundException("Dish not found"))
        );
    }

    @Override
    public PageResponse<List<DishResponse>> getAllDish(int page, int size) {
        Pageable pageable= PageRequest.of(page-1,size);
        Page<Dish> dishPage=dishRepository.findAll(pageable);
        return PageResponse.<List<DishResponse>>builder()
                .currentPage(page)
                .pageSize(size)
                .totalElements(dishPage.getTotalElements())
                .totalPages(dishPage.getTotalPages())
                .items(dishPage.stream().map(DishResponse::convert).collect(Collectors.toList()))
                .build();
    }

    @Override
    public PageResponse<List<DishResponse>> getDishlWithSortAndMultiFieldAndSearch(int page, int size, List<String> sortBy, String... search) {
        return null;
    }

    @Override
    public PageResponse<List<DishResponse>> getDishWithSortAndSearchByKeyword(int page, int size, String keyword) {
        return null;
    }
}
