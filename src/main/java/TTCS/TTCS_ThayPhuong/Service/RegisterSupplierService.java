package TTCS.TTCS_ThayPhuong.Service;

import TTCS.TTCS_ThayPhuong.Dto.Request.UserRegisterSupplierRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RegisterSupplierService {
    List<UserResponse> getAll(int page,int size);

    List<UserResponse> getAllStatus(int page,int size,String status);

    UserResponse registerSupplier(MultipartFile avatarPdf, MultipartFile resumePdf, UserRegisterSupplierRequest request);

    UserResponse acceptSupplier(Long supplierId);

    UserResponse rejectSupplier(Long supplierId);
}
