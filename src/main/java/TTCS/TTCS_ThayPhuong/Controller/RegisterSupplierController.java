package TTCS.TTCS_ThayPhuong.Controller;

import TTCS.TTCS_ThayPhuong.Dto.Request.UserRegisterSupplierRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.ApiResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.UserRegisterSupplierResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.UserResponse;
import TTCS.TTCS_ThayPhuong.Service.RegisterSupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/supplier")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RegisterSupplierController {
    private final RegisterSupplierService registerSupplierService;
    @GetMapping("/getAllSupplier")
    //@PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<List<UserRegisterSupplierResponse>> getAllSupplier(){
        return ApiResponse.<List<UserRegisterSupplierResponse>>builder()
                .message("Get all supplier")
                .result(registerSupplierService.getAll())
                .build();
    }

    @GetMapping("/getAllSupplierStatus")
    //@PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<List<UserResponse>> getAllSupplierStatus(
            @RequestParam(required = false) int page,
            @RequestParam(required = false) int size,
            @RequestParam(required = false) String status
    ){
        return ApiResponse.<List<UserResponse>>builder()
                .message("Get all supplier")
                .result(registerSupplierService.getAllStatus(page, size,status))
                .build();
    }
    @PatchMapping("/acceptSupplier/{supplierId}")
    //@PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<UserRegisterSupplierResponse> acceptSupplier(
            @PathVariable("supplierId") Long supplierId
    ){
        return ApiResponse.<UserRegisterSupplierResponse>builder()
                .message("Accept supplier")
                .result(registerSupplierService.acceptSupplier(supplierId))
                .build();
    }

    @PatchMapping("/rejectSupplier/{supplierId}")
    //@PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<UserRegisterSupplierResponse> rejectSupplier(
            @PathVariable("supplierId") Long supplierId
    ) {
        return ApiResponse.<UserRegisterSupplierResponse>builder()
                .message("Reject supplier")
                .result(registerSupplierService.rejectSupplier(supplierId))
                .build();
    }

    @PostMapping("/registerSupplier")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<UserRegisterSupplierResponse> registerSupplier(
            @RequestPart @Validated UserRegisterSupplierRequest request,
            @RequestPart(required = false) MultipartFile cv,
            @RequestPart(required = false) MultipartFile certificate
    ){
        return ApiResponse.<UserRegisterSupplierResponse>builder()
                .message("Register supplier")
                .result(registerSupplierService.registerSupplier(request,cv,certificate))
                .build();
    }
}
