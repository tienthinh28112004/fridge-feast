package TTCS.TTCS_ThayPhuong.Util;

import TTCS.TTCS_ThayPhuong.Entity.User;
import TTCS.TTCS_ThayPhuong.Exception.NotFoundException;
import TTCS.TTCS_ThayPhuong.Exception.TokenExpireException;
import TTCS.TTCS_ThayPhuong.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCurrentLoginUtils {
    private final UserRepository userRepository;
    public User getUserCurrent(){
        String email=SecurityUtils.getCurrentLogin()
                .orElseThrow(()-> new TokenExpireException("Người dùng chưa đăng nhập"));
        User user= userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));

        return user;
    }

}
