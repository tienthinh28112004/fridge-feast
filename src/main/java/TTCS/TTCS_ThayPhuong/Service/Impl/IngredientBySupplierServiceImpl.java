package TTCS.TTCS_ThayPhuong.Service.Impl;

import TTCS.TTCS_ThayPhuong.Dto.Request.IngredientCreateRequest;
import TTCS.TTCS_ThayPhuong.Dto.Request.IngredientUpdateRequest;
import TTCS.TTCS_ThayPhuong.Dto.Request.SupplierHasIngredientRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.IngredientDetailResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.IngredientResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.PageResponse;
import TTCS.TTCS_ThayPhuong.Entity.Ingredient;
import TTCS.TTCS_ThayPhuong.Entity.Supplier;
import TTCS.TTCS_ThayPhuong.Entity.SupplierHasIngredient;
import TTCS.TTCS_ThayPhuong.Entity.User;
import TTCS.TTCS_ThayPhuong.Enums.StatusRegisterSupplier;
import TTCS.TTCS_ThayPhuong.Exception.AccessDeniedException;
import TTCS.TTCS_ThayPhuong.Exception.BadRequestException;
import TTCS.TTCS_ThayPhuong.Exception.NotFoundException;
import TTCS.TTCS_ThayPhuong.Exception.TokenExpireException;
import TTCS.TTCS_ThayPhuong.Repository.IngredientRepository;
import TTCS.TTCS_ThayPhuong.Repository.SupplierHasIngredientRepository;
import TTCS.TTCS_ThayPhuong.Repository.SupplierRepository;
import TTCS.TTCS_ThayPhuong.Repository.UserRepository;
import TTCS.TTCS_ThayPhuong.Service.IngredientBySupplierService;
import TTCS.TTCS_ThayPhuong.Service.IngredientService;
import TTCS.TTCS_ThayPhuong.Util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class IngredientBySupplierServiceImpl implements IngredientBySupplierService {
    private final UserRepository userRepository;
    private final SupplierRepository supplierRepository;
    private final IngredientRepository ingredientRepository;
    private final SupplierHasIngredientRepository supplierHasIngredientRepository;

    @Override
    public IngredientDetailResponse supplierUpLoadIngredient(SupplierHasIngredientRequest request) {
        String email = SecurityUtils.getCurrentLogin()
                .orElseThrow(() -> new TokenExpireException("Bạn chưa đăng nhập"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Supplier supplier = supplierRepository.findByUser(user);
        if(!Objects.equals(supplier.getStatusRegisterSupplier(), StatusRegisterSupplier.APPROVED)){
            throw new AccessDeniedException("Tài khoản của bạn chưa được kích hoạt");
        }
        Ingredient ingredient = ingredientRepository.findById(request.getIngredientId())
                .orElseThrow(()->new NotFoundException("Ingredient not found"));
        SupplierHasIngredient hasIngredient=SupplierHasIngredient.builder()
                .supplier(supplier)
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
    public PageResponse<List<IngredientDetailResponse>> getIngredientWithSortAndMultiFieldAndSearch(int page, int size, String keyword, String... sortBy) {
        return null;
    }
}
