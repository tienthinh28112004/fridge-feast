package TTCS.TTCS_ThayPhuong.Service.Impl;

import TTCS.TTCS_ThayPhuong.Dto.Request.SupplierHasIngredientRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.IngredientDetailResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.PageResponse;
import TTCS.TTCS_ThayPhuong.Entity.Ingredient;
import TTCS.TTCS_ThayPhuong.Entity.SupplierHasIngredient;
import TTCS.TTCS_ThayPhuong.Entity.User;
import TTCS.TTCS_ThayPhuong.Enums.StatusRegisterSupplier;
import TTCS.TTCS_ThayPhuong.Exception.AccessDeniedException;
import TTCS.TTCS_ThayPhuong.Exception.NotFoundException;
import TTCS.TTCS_ThayPhuong.Exception.TokenExpireException;
import TTCS.TTCS_ThayPhuong.Repository.*;
import TTCS.TTCS_ThayPhuong.Service.IngredientBySupplierService;
import TTCS.TTCS_ThayPhuong.Util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class IngredientBySupplierServiceImpl implements IngredientBySupplierService {
    private final SearchRepository searchRepository;
    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;
    private final SupplierHasIngredientRepository supplierHasIngredientRepository;

    @Override
    public IngredientDetailResponse supplierUpLoadIngredient(SupplierHasIngredientRequest request) {
        String email = SecurityUtils.getCurrentLogin()
                .orElseThrow(() -> new TokenExpireException("Bạn chưa đăng nhập"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
//        if(!Objects.equals(user.getStatusRegisterSupplier(), StatusRegisterSupplier.APPROVED)){
//            throw new AccessDeniedException("Tài khoản của bạn chưa được kích hoạt");
//        }
        Ingredient ingredient = ingredientRepository.findById(request.getIngredientId())
                .orElseThrow(()->new NotFoundException("Ingredient not found"));
        SupplierHasIngredient hasIngredient=SupplierHasIngredient.builder()
                .supplier(user)
                .ingredient(ingredient)
                .price(request.getPrice())
                .stock(request.getStock())
                .build();
        supplierHasIngredientRepository.save(hasIngredient);
        return IngredientDetailResponse.convert(hasIngredient);
    }

    @Override
    public IngredientDetailResponse getIngredientDetailId(Long id) {
        return IngredientDetailResponse.convert(
                supplierHasIngredientRepository.findById(id)
                        .orElseThrow(()->new NotFoundException("Không tìm được bản ghi phù hợp"))
        );
    }

    @Override
    public PageResponse<List<IngredientDetailResponse>> getAllIngredientBySupplier(int page, int size) {
        Pageable pageable= PageRequest.of(page-1,size);
        Page<SupplierHasIngredient> hasIngredients=supplierHasIngredientRepository.findAll(pageable);

        return PageResponse.<List<IngredientDetailResponse>>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(hasIngredients.getTotalPages())
                .totalElements(hasIngredients.getTotalElements())
                .items(hasIngredients.stream().map(IngredientDetailResponse::convert).collect(Collectors.toList()))
                .build();
    }

    @Override
    public PageResponse<List<IngredientDetailResponse>> getIngredientWithSortAndMultiFieldAndSearch(int page, int size, String sortBy, String... search) {
        return searchRepository.getIngredientWithSortMultiFieldAndSearch(page, size,sortBy,search);
    }

    @Override
    public PageResponse<List<IngredientDetailResponse>> getIngredientBySupplier(int page, int size, String keyword, String sorts) {
            String email=SecurityUtils.getCurrentLogin()
                    .orElseThrow(()->new BadCredentialsException("Bạn chưa đăng nhập"));
            User user=userRepository.findByEmail(email)
                    .orElseThrow(()->new NotFoundException("User not found"));
            //sort
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
            Pageable pageable = PageRequest.of(page - 1, size, Sort.by(orders));
            Page<SupplierHasIngredient> ingredientPage = null;
            if (StringUtils.hasLength(keyword)&&keyword!=null) {
                ingredientPage = supplierHasIngredientRepository.findAllByKeywordAndUserId(pageable, keyword,user.getId());
            } else {
                ingredientPage = supplierHasIngredientRepository.findAllByUserId(pageable,user.getId());
            }
            List<IngredientDetailResponse> ingredientList=ingredientPage.stream().map(IngredientDetailResponse::convert).toList();
            return PageResponse.<List<IngredientDetailResponse>>builder()
                    .currentPage(page)
                    .pageSize(size)
                    .totalPages(ingredientPage.getTotalPages())
                    .items(ingredientList)
                    .totalElements((long) ingredientList.size())
                    .build();
        }
    }
