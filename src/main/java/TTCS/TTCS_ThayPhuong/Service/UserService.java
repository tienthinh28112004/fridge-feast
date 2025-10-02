package TTCS.TTCS_ThayPhuong.Service;

import TTCS.TTCS_ThayPhuong.Dto.Request.ChangePasswordRequest;
import TTCS.TTCS_ThayPhuong.Dto.Request.UserCreateRequest;
import TTCS.TTCS_ThayPhuong.Dto.Request.UserUpdateRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.PageResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.SupplierApplicationDetailResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.UserResponse;
import TTCS.TTCS_ThayPhuong.Entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserCreateRequest request);
    PageResponse<List<UserResponse>> getAllUsersWithSortByMultipleColumns(int pageNo, int pageSize,String keyword, String sorts);
    UserResponse findById(Long id);
    UserResponse update(UserUpdateRequest request);
    void banUser(Long userId);
    void unBanUser(Long userId);
    void delete(Long userId);
    UserResponse changePassword(ChangePasswordRequest request);
    UserResponse getMyInfo();
    Double getDistance(Long userId,Long supplierId);

    SupplierApplicationDetailResponse getUserApplicationDetail(Long userId);
}
