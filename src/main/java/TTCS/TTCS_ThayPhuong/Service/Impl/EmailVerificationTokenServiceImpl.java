package TTCS.TTCS_ThayPhuong.Service.Impl;

import TTCS.TTCS_ThayPhuong.Entity.EmailVerificationToken;
import TTCS.TTCS_ThayPhuong.Entity.User;
import TTCS.TTCS_ThayPhuong.Exception.BadRequestException;
import TTCS.TTCS_ThayPhuong.Repository.EmailVerificationTokenRepository;
import TTCS.TTCS_ThayPhuong.Service.EmailVerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailVerificationTokenServiceImpl implements EmailVerificationTokenService {
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    @Value("${app.registration.email.token.expires-in}")
    private final Long expiresIn;
    @Override
    public EmailVerificationToken create(User user) {
        String newToken =generateToken();
        Date expires = new Date(new Date().getTime() + expiresIn);
        EmailVerificationToken oldToken = emailVerificationTokenRepository.findByUserId(user.getId());

        if(oldToken !=null){//nếu tồn tại rồi
            oldToken.setToken(newToken);
            oldToken.setExpirationDate(expires);

            return emailVerificationTokenRepository.save(oldToken);
        }else{//nếu chưa tồn tại
            return emailVerificationTokenRepository.save(EmailVerificationToken.builder()
                            .user(user)
                            .token(newToken)
                            .expirationDate(expires)
                    .build());
        }
    }

    @Override
    public User getUserByToken(String token) {
        EmailVerificationToken emailVerificationToken = emailVerificationTokenRepository.findByToken(token);

        if(emailVerificationToken.getExpirationDate().before(new Date())){//hết hạn trả ra lỗi và xóa token cũ
            emailVerificationTokenRepository.deleteByUserId(emailVerificationToken.getUser().getId());
            throw new BadRequestException("Mã OTP của bạn đã hết hiệu lực,vui lòng tạo lại Tài khoản");
        }
        return emailVerificationToken.getUser();
    }

    @Override
    public void deleteByUserId(Long userId) {
        emailVerificationTokenRepository.deleteByUserId(userId);
    }

    public String generateToken(){
        Random random=new Random();
        long token= random.nextLong(8)+100000000;
        return String.valueOf(token);
    }
}
