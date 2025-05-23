package TTCS.TTCS_ThayPhuong.Service.Impl;

import TTCS.TTCS_ThayPhuong.Dto.Request.IngredientCreateRequest;
import TTCS.TTCS_ThayPhuong.Dto.Request.IngredientUpdateRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.IngredientResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.PageResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.UserResponse;
import TTCS.TTCS_ThayPhuong.Entity.Ingredient;
import TTCS.TTCS_ThayPhuong.Entity.User;
import TTCS.TTCS_ThayPhuong.Exception.NotFoundException;
import TTCS.TTCS_ThayPhuong.Repository.IngredientRepository;
import TTCS.TTCS_ThayPhuong.Service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        ingredientRepository.save(ingredient);
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
            ingredient.setUnit(request.getUnit());
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
        ingredient.setActive(!ingredient.isActive());
        ingredientRepository.save(ingredient);
        return IngredientResponse.convert(ingredient);
    }

    @Override
    public List<IngredientResponse> ingredientByKeyword(String keyword) {
        Pageable pageable= PageRequest.of(0,9);
        List<Ingredient> ingredientList=ingredientRepository.findByIngredientKeyword(keyword,pageable);
        return ingredientList.stream().map(IngredientResponse::convert).collect(Collectors.toList());
    }

    @Override
    public void hardDeleteIngredient(Long ingredientId) {
        ingredientRepository.deleteById(ingredientId);
    }

    @Override
    public PageResponse<List<IngredientResponse>> getAllIngredient(int pageNo, int pageSize, String keyword, String sorts) {
        List<Sort.Order> orders=new ArrayList<>();

        if(sorts!=null) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sorts);
            if (matcher.find()) {
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    orders.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else {
                    orders.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }
            }
        }

        //Paging
        Pageable pageable=PageRequest.of(pageNo-1,pageSize,Sort.by(orders));
        Page<Ingredient> userPage=null;
        if(StringUtils.hasLength(keyword)&&keyword!=null){
            userPage =ingredientRepository.findAllByKeyword(pageable,keyword);
        }else{
            userPage = ingredientRepository.findAll(pageable);
        }
        List<IngredientResponse> userList=userPage.stream().map(IngredientResponse::convert).toList();
        return PageResponse.<List<IngredientResponse>>builder()
                .currentPage(pageNo)
                .pageSize(pageSize)
                .totalPages(userPage.getTotalPages())
                .items(userList)
                .totalElements((long) userList.size())
                .build();
    }
}
