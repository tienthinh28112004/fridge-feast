package TTCS.TTCS_ThayPhuong.Controller;

import TTCS.TTCS_ThayPhuong.Dto.Response.ApiResponse;
import TTCS.TTCS_ThayPhuong.Service.Impl.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final CloudinaryService cloudinaryService;

    @PostMapping("/update-avatar")
    ApiResponse<String> updateAvatar(@RequestParam("file") MultipartFile file){
        String url= cloudinaryService.uploadImage(file);

        cloudinaryService.updateImage(url);
        return ApiResponse.<String>builder()
                .message("Profile updated successfully")
                .build();
    }

    @PostMapping("/get-avatar")
    ApiResponse<String> getAvatar(){
        return ApiResponse.<String>builder()
                .result(cloudinaryService.getImage())
                .message("Profile get avatar successfully")
                .build();
    }

    @DeleteMapping("remove-avatar")
    ApiResponse<String> removeAvatar(){
        try{
            cloudinaryService.deleteAvatar();
            return ApiResponse.<String>builder()
                    .message("Profile removed successfully")
                    .build();
        }catch (Exception e){
            return ApiResponse.<String>builder()
                    .message(e.getMessage())
                    .build();
        }
    }
}
