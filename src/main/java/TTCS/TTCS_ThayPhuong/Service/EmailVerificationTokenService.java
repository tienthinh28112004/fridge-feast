package TTCS.TTCS_ThayPhuong.Service;

import TTCS.TTCS_ThayPhuong.Entity.EmailVerificationToken;
import TTCS.TTCS_ThayPhuong.Entity.User;

public interface EmailVerificationTokenService {
    EmailVerificationToken create(User user);
    User getUserByToken(String token);
    void deleteByUserId(Long userId);
}
