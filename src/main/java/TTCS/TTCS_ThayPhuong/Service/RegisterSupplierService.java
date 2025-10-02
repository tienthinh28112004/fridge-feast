package TTCS.TTCS_ThayPhuong.Service;

import TTCS.TTCS_ThayPhuong.Dto.Request.UserRegisterSupplierRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.UserRegisterSupplierResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RegisterSupplierService {
    List<UserRegisterSupplierResponse> getAll();

    List<UserResponse> getAllStatus(int page,int size,String status);

    UserRegisterSupplierResponse registerSupplier(UserRegisterSupplierRequest request, MultipartFile cv, MultipartFile certificate);

    UserRegisterSupplierResponse acceptSupplier(Long supplierId);

    UserRegisterSupplierResponse rejectSupplier(Long supplierId);
}
