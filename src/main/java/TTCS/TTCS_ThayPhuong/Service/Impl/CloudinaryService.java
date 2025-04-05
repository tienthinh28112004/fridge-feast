package TTCS.TTCS_ThayPhuong.Service.Impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;

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
}
