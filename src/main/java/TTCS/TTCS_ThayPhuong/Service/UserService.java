package TTCS.TTCS_ThayPhuong.Service;

import TTCS.TTCS_ThayPhuong.Dto.Request.UserCreateRequest;
import TTCS.TTCS_ThayPhuong.Dto.Request.UserUpdateRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.PageResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.UserResponse;
import TTCS.TTCS_ThayPhuong.Entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserResponse createUser(UserCreateRequest request);
    PageResponse<?> getAllUsersWithSortByMultipleColumns(int pageNo, int pageSize, String... sorts);
    UserResponse findById(Long id);
    UserResponse update(Long userId, UserUpdateRequest request, MultipartFile avatarPdf);
    void delete(Long userId);
    UserResponse getMyInfo();
}
