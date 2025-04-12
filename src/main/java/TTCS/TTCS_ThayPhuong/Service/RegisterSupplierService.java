package TTCS.TTCS_ThayPhuong.Service;

import TTCS.TTCS_ThayPhuong.Dto.Request.UserRegisterSupplierRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.SupplierResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RegisterSupplierService {
    List<SupplierResponse> getAll();

    SupplierResponse registerSupplier(MultipartFile avatarPdf, MultipartFile resumePdf, UserRegisterSupplierRequest request);

    SupplierResponse acceptSupplier(Long supplierId);

    SupplierResponse rejectSupplier(Long supplierId);
}
