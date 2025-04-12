package TTCS.TTCS_ThayPhuong.Controller;


import TTCS.TTCS_ThayPhuong.Dto.Request.ChangePasswordRequest;
import TTCS.TTCS_ThayPhuong.Dto.Request.UserCreateRequest;
import TTCS.TTCS_ThayPhuong.Dto.Request.UserUpdateRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.ApiResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.PageResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.UserResponse;
import TTCS.TTCS_ThayPhuong.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/list-with-sort-by-multiple-columns")
    public ApiResponse<?> getAllUsersWithSortByMultipleColumns(@RequestParam(defaultValue = "1", required = false) int pageNo,
                                                               @RequestParam(defaultValue = "10", required = false) int pageSize,
                                                               @RequestParam(required = false) String... sorts) {
        log.info("Request get all of users with sort by multiple columns");
        return ApiResponse.<PageResponse<List<UserResponse>>>builder()
                .message("List users")
                .result(userService.getAllUsersWithSortByMultipleColumns(pageNo, pageSize, sorts))
                .build();
    }

    @PostMapping("/addUser")
    public ApiResponse<UserResponse> createUser(
            @RequestBody @Valid UserCreateRequest request){
        return ApiResponse.<UserResponse>builder()
                .message("Create user successfully")
                .result(userService.createUser(request))
                .build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getUser(
            @PathVariable("userId") final Long userId) {
        return ApiResponse.<UserResponse>builder()
                .message("Detail user")
                .result(userService.findById(userId))
                .build();
    }

    @PatchMapping("/update/{userId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ApiResponse<UserResponse> updateUser(
            @PathVariable("userId") Long userId,
            @RequestPart @Valid UserUpdateRequest request,
            @RequestPart(name = "avatarPdf", required = false) MultipartFile avatarPdf) {
        return ApiResponse.<UserResponse>builder()
                .message("Update User successfully")
                .result(userService.update(userId,request,avatarPdf))
                .build();

    }

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<String> deleteUser(
            @PathVariable("userId") final Long userId){
        userService.delete(userId);
        return ApiResponse.<String>builder().
                result("User has been deleted").
                build();
    }

    @PatchMapping("/changePassword")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<UserResponse> changePassword(
            @RequestBody @Valid ChangePasswordRequest request){
        return ApiResponse.<UserResponse>builder()
                .message("Change password successfully")
                .result(userService.changePassword(request))
                .build();
    }

    @GetMapping (value = "/myInfo")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<UserResponse> getMyInfo () {
        return ApiResponse.<UserResponse>builder()
                .message("User detail")
                .result(userService.getMyInfo())
                .build();
    }
}

