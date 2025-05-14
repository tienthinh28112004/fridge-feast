package TTCS.TTCS_ThayPhuong.Service.Impl;

import TTCS.TTCS_ThayPhuong.Entity.User;
import TTCS.TTCS_ThayPhuong.Exception.NotFoundException;
import TTCS.TTCS_ThayPhuong.Repository.UserRepository;
import TTCS.TTCS_ThayPhuong.Util.SecurityUtils;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;
    private final UserRepository userRepository;

    public String uploadImage(MultipartFile file){
        try{
            var result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder","/upload",//upload vào thư mục upload
                    "use_filename",true,//đặt tên giống file gốc(nếu có thể)
                    "unique_filename",true,//nếu tên không trùng file gốc thì tạo tên mới
                    "resource_type","auto"//tụ động xác định đây là file ảnh hay âm thanh
            ));
            return result.get("secure_url").toString();//lấy ra url của nó
        } catch (IOException e) {
            throw new RuntimeException("Upload fail");
        }
    }
    @PreAuthorize("isAuthenticated()")
    public String getImage(){
        String email= SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new BadCredentialsException("Bạn chưa đăng nhập"));
        User user =userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));

        return (user.getAvatarUrl() != null )?user.getAvatarUrl() : "";
    }

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void updateImage(String url){
        String email= SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new BadCredentialsException("Bạn chưa đăng nhập"));
        User user =userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));

        user.setAvatarUrl(url);
        userRepository.save(user);
    }

    @Transactional
    public void deleteAvatar(){
        String email= SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new BadCredentialsException("Bạn chưa đăng nhập"));
        User user =userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));
        user.setAvatarUrl(null);
        userRepository.save(user);
    }
}
