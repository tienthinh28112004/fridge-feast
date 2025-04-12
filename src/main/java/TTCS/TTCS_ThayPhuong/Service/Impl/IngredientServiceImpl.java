package TTCS.TTCS_ThayPhuong.Service.Impl;

import TTCS.TTCS_ThayPhuong.Dto.Request.IngredientCreateRequest;
import TTCS.TTCS_ThayPhuong.Dto.Request.IngredientUpdateRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.IngredientResponse;
import TTCS.TTCS_ThayPhuong.Entity.Ingredient;
import TTCS.TTCS_ThayPhuong.Exception.NotFoundException;
import TTCS.TTCS_ThayPhuong.Repository.IngredientRepository;
import TTCS.TTCS_ThayPhuong.Service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {
    private final IngredientRepository ingredientRepository;
    private final CloudinaryService cloudinaryService;
    @Override
    public IngredientResponse createIngredient(IngredientCreateRequest request, MultipartFile ingredientPdf) {
        String ingredientUrl=null;
        if(ingredientPdf!=null){
            ingredientUrl=cloudinaryService.uploadImage(ingredientPdf);
        }
        Ingredient ingredient=Ingredient.builder()
                .name(request.getName())
                .description(request.getDescription())
                .unit(request.getUnit())
                .isActive(true)
                .ingredientImage(ingredientUrl)
                .build();
        return IngredientResponse.convert(ingredient);
    }

    @Override
    public IngredientResponse getById(Long ingredientId) {
        return IngredientResponse.convert(
                ingredientRepository.findById(ingredientId)
                        .orElseThrow(()-> new NotFoundException("Ingredient not found"))
        );
    }

    @Override
    public List<IngredientResponse> getAllIngredient() {
        List<Ingredient> ingredientList=ingredientRepository.findAll();
        return ingredientList.stream().map(IngredientResponse::convert).collect(Collectors.toList());
    }

    @Override
    public IngredientResponse updateIngredient(Long ingredientId, IngredientUpdateRequest request, MultipartFile ingredientPdf) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(()->new NotFoundException("Ingredient not found"));

        if(StringUtils.hasLength(request.getName())&& !Objects.equals(request.getName(),ingredient.getName())){
            ingredient.setName(request.getName());
        }
        if(StringUtils.hasLength(request.getDescription())&& !Objects.equals(request.getDescription(),ingredient.getDescription())){
            ingredient.setDescription(request.getDescription());
        }
        if(StringUtils.hasLength(request.getUnit())&& !Objects.equals(request.getUnit(),ingredient.getUnit())){
            ingredient.setName(request.getUnit());
        }
        if(ingredientPdf!=null){
            ingredient.setIngredientImage(cloudinaryService.uploadImage(ingredientPdf));
        }
        return IngredientResponse.convert(ingredient);
    }

    @Override
    public IngredientResponse softDeleteIngredient(Long ingredientId) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(()->new NotFoundException("Ingredient not found"));
        ingredient.setActive(false);
        return IngredientResponse.convert(ingredient);
    }

    @Override
    public void hardDeleteIngredient(Long ingredientId) {
        ingredientRepository.deleteById(ingredientId);
    }
}
