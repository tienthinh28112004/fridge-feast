package TTCS.TTCS_ThayPhuong.Service.Impl;

import TTCS.TTCS_ThayPhuong.Dto.Request.UserRegisterSupplierRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.SupplierResponse;
import TTCS.TTCS_ThayPhuong.Entity.Roles;
import TTCS.TTCS_ThayPhuong.Entity.Supplier;
import TTCS.TTCS_ThayPhuong.Entity.User;
import TTCS.TTCS_ThayPhuong.Entity.UserHasRole;
import TTCS.TTCS_ThayPhuong.Enums.Role;
import TTCS.TTCS_ThayPhuong.Enums.StatusRegisterSupplier;
import TTCS.TTCS_ThayPhuong.Exception.BadRequestException;
import TTCS.TTCS_ThayPhuong.Exception.NotFoundException;
import TTCS.TTCS_ThayPhuong.Exception.TokenExpireException;
import TTCS.TTCS_ThayPhuong.Repository.RolesRepository;
import TTCS.TTCS_ThayPhuong.Repository.SupplierRepository;
import TTCS.TTCS_ThayPhuong.Repository.UserRepository;
import TTCS.TTCS_ThayPhuong.Service.RegisterSupplierService;
import TTCS.TTCS_ThayPhuong.Util.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegisterSupplierServiceImpl implements RegisterSupplierService {
    private final SupplierRepository supplierRepository;
    private final CloudinaryService cloudinaryService;
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    @Override
    @Transactional
    public List<SupplierResponse> getAll() {
        List<Supplier> supplierList=supplierRepository.findAll();
        return supplierList.stream().map(SupplierResponse::convert).collect(Collectors.toList());
    }

    @Override
    public SupplierResponse registerSupplier(MultipartFile avatarPdf, MultipartFile resumePdf, UserRegisterSupplierRequest request) {
        String email= SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new TokenExpireException("Bạn chưa đăng nhập"));
        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));
        if(StringUtils.isNotBlank(user.getPhoneNumber())&& !Objects.equals(user.getPhoneNumber(),request.getPhoneNumber())){
            throw new BadRequestException("Vui lòng nhập số điện thoại trùng với số ddienj thoaại bạn đã đăng kí");
        }
        String avatarUrl=null;
        if(avatarPdf!=null){
            avatarUrl=cloudinaryService.uploadImage(avatarPdf);
        }
        String resumeUrl=null;
        if(resumePdf!=null){
            resumeUrl=cloudinaryService.uploadImage(resumePdf);
        }
        Supplier supplier=Supplier.builder()
                .user(user)
                .supplierName(request.getSupplierName())
                .address(request.getAddress())
                .facebookLink(request.getFacebookLink())
                .phoneNumber(request.getPhoneNumber())
                .longitude(request.getLongitude())
                .latitude(request.getLatitude())
                .supplierHasIngredients(new ArrayList<>())
                .avatarUrl(avatarUrl==null?user.getAvatarUrl():avatarUrl)
                .resumeUrl(resumeUrl)
                .statusRegisterSupplier(StatusRegisterSupplier.PENDING)
                .build();
        supplierRepository.save(supplier);
        return SupplierResponse.convert(supplier);
    }

    @Override
    @Transactional
    public SupplierResponse acceptSupplier(Long supplierId) {
        Supplier supplier=supplierRepository.findById(supplierId)
                .orElseThrow(()->new NotFoundException("Supplier not found"));
        if(Objects.equals(supplier.getStatusRegisterSupplier(),StatusRegisterSupplier.PENDING)){
            supplier.setStatusRegisterSupplier(StatusRegisterSupplier.APPROVED);

            Roles roles=rolesRepository.findByName(String.valueOf(Role.SUPPLIER))
                    .orElseThrow(()->new NotFoundException("Role not found"));

            UserHasRole userHasRole=UserHasRole.builder()
                    .role(roles)
                    .user(supplier.getUser())
                    .build();
            supplier.getUser().getUserHasRoles().add(userHasRole);
            supplierRepository.save(supplier);
        }
        return SupplierResponse.convert(supplier);
    }

    @Override
    @Transactional
    public SupplierResponse rejectSupplier(Long supplierId) {
        Supplier supplier=supplierRepository.findById(supplierId)
                .orElseThrow(()->new NotFoundException("Supplier not found"));
        if(Objects.equals(supplier.getStatusRegisterSupplier(),StatusRegisterSupplier.APPROVED)){
            supplier.setStatusRegisterSupplier(StatusRegisterSupplier.REJECTED);

            Roles roles=rolesRepository.findByName(String.valueOf(Role.SUPPLIER))
                    .orElseThrow(()->new NotFoundException("Role not found"));

            UserHasRole userHasRole=UserHasRole.builder()
                    .role(roles)
                    .user(supplier.getUser())
                    .build();
            supplier.getUser().getUserHasRoles().remove(userHasRole);
            supplierRepository.save(supplier);
        }
        return SupplierResponse.convert(supplier);
    }

}
