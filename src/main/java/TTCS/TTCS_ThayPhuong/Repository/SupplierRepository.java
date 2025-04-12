package TTCS.TTCS_ThayPhuong.Repository;

import TTCS.TTCS_ThayPhuong.Entity.Supplier;
import TTCS.TTCS_ThayPhuong.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier,Long> {
    Supplier findByUser(User user);
}
