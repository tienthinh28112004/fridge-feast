package TTCS.TTCS_ThayPhuong.Service;

import TTCS.TTCS_ThayPhuong.Entity.EmailVerificationToken;
import TTCS.TTCS_ThayPhuong.Entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

public interface EmailVerificationTokenService {
    EmailVerificationToken create(User user);
    User getUserByToken(String token);
    @Modifying
    @Query("DELETE FROM EmailVerificationToken rt WHERE rt.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}
