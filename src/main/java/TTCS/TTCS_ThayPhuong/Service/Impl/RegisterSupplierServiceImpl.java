package TTCS.TTCS_ThayPhuong.Service.Impl;

import TTCS.TTCS_ThayPhuong.Dto.Request.UserRegisterSupplierRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.UserResponse;
import TTCS.TTCS_ThayPhuong.Entity.Roles;
import TTCS.TTCS_ThayPhuong.Entity.User;
import TTCS.TTCS_ThayPhuong.Entity.UserHasRole;
import TTCS.TTCS_ThayPhuong.Enums.Role;
import TTCS.TTCS_ThayPhuong.Enums.StatusRegisterSupplier;
import TTCS.TTCS_ThayPhuong.Exception.BadRequestException;
import TTCS.TTCS_ThayPhuong.Exception.NotFoundException;
import TTCS.TTCS_ThayPhuong.Exception.TokenExpireException;
import TTCS.TTCS_ThayPhuong.Repository.RolesRepository;
import TTCS.TTCS_ThayPhuong.Repository.UserRepository;
import TTCS.TTCS_ThayPhuong.Service.RegisterSupplierService;
import TTCS.TTCS_ThayPhuong.Util.SecurityUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterSupplierServiceImpl implements RegisterSupplierService {
    private final CloudinaryService cloudinaryService;
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    @Override
    @Transactional
    public List<UserResponse> getAll(int page,int size) {
        Pageable pageable= PageRequest.of(page-1,size);
        List<User> supplierList=userRepository.findBySupplierAll(pageable);
        return supplierList.stream().map(UserResponse::convert).collect(Collectors.toList());

    }

    @Override
    @Transactional
    public List<UserResponse> getAllStatus(int page,int size,String status) {
        Pageable pageable= PageRequest.of(page-1,size);
        List<User> supplierList=userRepository.findByStatusRegister(pageable,StatusRegisterSupplier.valueOf(status));
        return supplierList.stream().map(UserResponse::convert).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponse registerSupplier(MultipartFile avatarPdf, MultipartFile resumePdf, UserRegisterSupplierRequest request) {
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
        user.setAvatarUrl(avatarUrl);
        user.setResumeUrl(resumeUrl);
        user.setFullName(request.getSupplierName());
        user.setAddress(request.getAddress());
        user.setFacebookLink(request.getFacebookLink());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setStatusRegisterSupplier(StatusRegisterSupplier.PENDING);
        userRepository.save(user);
        return UserResponse.convert(user);

    }

    @Override
    @Transactional
    public UserResponse acceptSupplier(Long supplierId) {
        User supplier=userRepository.findById(supplierId)
                .orElseThrow(()->new NotFoundException("Supplier not found"));
        if(!Objects.equals(supplier.getStatusRegisterSupplier(),StatusRegisterSupplier.APPROVED)){
            supplier.setStatusRegisterSupplier(StatusRegisterSupplier.APPROVED);

            Roles roles=rolesRepository.findByName(String.valueOf(Role.SUPPLIER))
                    .orElseThrow(()->new NotFoundException("Role not found"));

            UserHasRole userHasRole=UserHasRole.builder()
                    .role(roles)
                    .user(supplier)
                    .build();
            supplier.getUserHasRoles().add(userHasRole);
            userRepository.save(supplier);
        }
        return UserResponse.convert(supplier);
    }

    @Override
    @Transactional
    public UserResponse rejectSupplier(Long supplierId) {
        User supplier = userRepository.findById(supplierId)
                .orElseThrow(() -> new NotFoundException("Supplier not found"));

        if (supplier.getStatusRegisterSupplier() == StatusRegisterSupplier.APPROVED) {
            supplier.setStatusRegisterSupplier(StatusRegisterSupplier.REJECTED);

            UserHasRole roleToRemove = supplier.getUserHasRoles().stream()
                    .filter(r -> r.getRole().getName().equals(Role.SUPPLIER.name()))
                    .findFirst()
                    .orElse(null);

            if (roleToRemove != null) {
                supplier.getUserHasRoles().remove(roleToRemove);
            }

            userRepository.save(supplier);
        }

        return UserResponse.convert(supplier);
    }
}
