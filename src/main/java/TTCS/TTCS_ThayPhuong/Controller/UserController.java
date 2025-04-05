package TTCS.TTCS_ThayPhuong.Controller;


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
        return ApiResponse.<PageResponse>builder()
                .message("list users")
                .result(userService.getAllUsersWithSortByMultipleColumns(pageNo, pageSize, sorts))
                .build();
    }

    @PostMapping("/addUser")//để chờ register gọi đến
    public ApiResponse<UserResponse> createUser(
            @RequestBody @Valid UserCreateRequest request){
        log.info("add user");
        return ApiResponse.<UserResponse>builder()
                .message("create sucessfully")
                .result(userService.createUser(request))
                .build();
    }

    //@PreAuthorize("#email == authentication.token.claims['subject'] or hasAuthority('ADMIN')")
    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getUser(
            @PathVariable("userId") final Long userId) {
        return ApiResponse.<UserResponse>builder()
                .message("show user")
                .result(userService.findById(userId))
                .build();
    }

    //@PreAuthorize("#email == authentication.token.claims['subject'] or hasAuthority('ADMIN')")
    @PatchMapping("/update/{userId}")
    public ApiResponse<UserResponse> updateUser(
            @PathVariable("userId") Long userId,
            @RequestPart @Valid UserUpdateRequest request,
            @RequestPart(name = "avatarPdf", required = false) MultipartFile avatarPdf) {
        return ApiResponse.<UserResponse>builder()
                .message("update User")
                .result(userService.update(userId,request,avatarPdf))
                .build();

    }

    //@PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{userId}")
    public ApiResponse<String> deleteUser(
            @PathVariable("userId") final Long userId){
        userService.delete(userId);
        return ApiResponse.<String>builder().
                result("User has been deleted").
                build();
    }

    //@PreAuthorize("isAuthenticated()")
//    @GetMapping (value = "/myInfo")
//    public ApiResponse<UserResponse> getMyInfo () {
//        return ApiResponse.<UserResponse>builder()
//                .result(userService.getMyInfo())
//                .build();
//    }
}

