package TTCS.TTCS_ThayPhuong.Repository;

import TTCS.TTCS_ThayPhuong.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query("SELECT n FROM Order n WHERE n.user.id= :userId")
    List<Order> findOrderByUserId(@Param("userId") Long userId);
}
