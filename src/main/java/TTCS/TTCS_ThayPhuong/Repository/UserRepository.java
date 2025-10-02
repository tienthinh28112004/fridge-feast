package TTCS.TTCS_ThayPhuong.Repository;

import TTCS.TTCS_ThayPhuong.Dto.Response.UserResponse;
import TTCS.TTCS_ThayPhuong.Entity.User;
import TTCS.TTCS_ThayPhuong.Enums.StatusRegisterSupplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    @Query("select u from User u where u.statusRegisterSupplier IS NOT NULL")
    List<User> findBySupplierAll(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.statusRegisterSupplier = :status")
    List<User> findByStatusRegister(Pageable pageable,@Param("status") StatusRegisterSupplier status);

    @Query("SELECT u FROM User u WHERE u.fullName LIKE %:keyword% OR u.email LIKE %:keyword%")
    Page<User> findAllByKeyword(Pageable pageable,@Param("keyword") String keyword);
}
