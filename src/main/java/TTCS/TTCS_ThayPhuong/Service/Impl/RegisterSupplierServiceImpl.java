package TTCS.TTCS_ThayPhuong.Service.Impl;

import TTCS.TTCS_ThayPhuong.Dto.Request.UserRegisterSupplierRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.UserRegisterSupplierResponse;
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
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
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
    public List<UserRegisterSupplierResponse> getAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        log.info("Lấy được danh sách các tác gải thành công");
        return userRepository.findAll(sort)
                .stream()
                .filter(user ->
                        user != null &&
                                user.getStatusRegisterSupplier() != null &&  // Add null check
                                StatusRegisterSupplier.PENDING.equals(user.getStatusRegisterSupplier()) &&
                                user.getUserHasRoles() != null &&
                                !user.getUserHasRoles().isEmpty())
                .map(UserRegisterSupplierResponse::convert)
                .collect(Collectors.toList());
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
    public UserRegisterSupplierResponse registerSupplier(UserRegisterSupplierRequest request,
                                                     MultipartFile cv,
                                                     MultipartFile certificate){
        String email= SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new BadCredentialsException("email invalid"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));

        if(user.getStatusRegisterSupplier() == null || user.getStatusRegisterSupplier().equals(StatusRegisterSupplier.REJECTED)){
            String cvUrl=null;
            if(cv!=null){
                cvUrl = cloudinaryService.uploadImage(cv);
            }
            String certificateUrl=null;
            if(certificate!=null){
                certificateUrl= cloudinaryService.uploadImage(certificate);
            }
            user.setBio(request.getBio());
            user.setFacebookLink(request.getFacebookLink());
            user.setCvUrl(cvUrl);
            user.setCertificate(certificateUrl);
//            user.setEmail(request.getEmail());
            user.setPhoneNumber(request.getPhone());
            user.setExpertise(request.getExpertise());
            user.setYearsOfExperience(request.getYearsOfExperience());
//            user.setFullName(request.getName());
            user.setStatusRegisterSupplier(StatusRegisterSupplier.PENDING);

            userRepository.save(user);
        }
        return UserRegisterSupplierResponse.convert(user);
    }

    @Override
    @Transactional
    public UserRegisterSupplierResponse acceptSupplier(Long supplierId) {
        User supplier=userRepository.findById(supplierId)
                .orElseThrow(()->new NotFoundException("Supplier not found"));
        boolean supply=false;
        for(UserHasRole x: supplier.getUserHasRoles()){
            if(x.getRole().getName().equalsIgnoreCase(String.valueOf(Role.SUPPLIER))){
                supply=true;
            }
        }
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
        return UserRegisterSupplierResponse.convert(supplier);
    }

    @Override
    @Transactional
    public UserRegisterSupplierResponse rejectSupplier(Long supplierId) {
        User supplier = userRepository.findById(supplierId)
                .orElseThrow(() -> new NotFoundException("Supplier not found"));

        if (!Objects.equals(supplier.getStatusRegisterSupplier(),StatusRegisterSupplier.REJECTED)) {
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

        return UserRegisterSupplierResponse.convert(supplier);
    }
}
