package TTCS.TTCS_ThayPhuong.Controller;

import TTCS.TTCS_ThayPhuong.Dto.Request.UserRegisterSupplierRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.ApiResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.SupplierResponse;
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<List<SupplierResponse>> getAllSupplier(){
        return ApiResponse.<List<SupplierResponse>>builder()
                .message("Get all supplier")
                .result(registerSupplierService.getAll())
                .build();
    }
    @PatchMapping("/acceptSupplier/{supplierId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<SupplierResponse> acceptSupplier(
            @PathVariable("supplierId") Long supplierId
    ){
        return ApiResponse.<SupplierResponse>builder()
                .message("Accept supplier")
                .result(registerSupplierService.acceptSupplier(supplierId))
                .build();
    }

    @PatchMapping("/rejectSupplier/{supplierId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<SupplierResponse> rejectSupplier(
            @PathVariable("supplierId") Long supplierId
    ) {
        return ApiResponse.<SupplierResponse>builder()
                .message("Reject supplier")
                .result(registerSupplierService.rejectSupplier(supplierId))
                .build();
    }

    @GetMapping("/registerSupplier")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<SupplierResponse> registerSupplier(
            @RequestPart @Validated UserRegisterSupplierRequest request,
            @RequestPart(required = false) MultipartFile avatarPdf,
            @RequestPart(required = false) MultipartFile resumePdf
    ){
        return ApiResponse.<SupplierResponse>builder()
                .message("Register supplier")
                .result(registerSupplierService.registerSupplier(avatarPdf,resumePdf,request))
                .build();
    }
}
