package TTCS.TTCS_ThayPhuong.Repository;

import TTCS.TTCS_ThayPhuong.Entity.EmailVerificationToken;
import TTCS.TTCS_ThayPhuong.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken,Long> {
    @Modifying
    @Query("DELETE FROM EmailVerificationToken e WHERE e.user.id= :userId")
    void deleteByUserId(@Param("userId") Long userId);

    EmailVerificationToken findByToken(String token);

    @Query("SELECT e FROM EmailVerificationToken e WHERE e.user.id = :userId")
    EmailVerificationToken findByUserId(@Param("userId") Long id);
}
