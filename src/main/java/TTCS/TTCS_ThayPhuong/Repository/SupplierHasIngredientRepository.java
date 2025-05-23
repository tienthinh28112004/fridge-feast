package TTCS.TTCS_ThayPhuong.Repository;

import TTCS.TTCS_ThayPhuong.Entity.SupplierHasIngredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SupplierHasIngredientRepository extends JpaRepository<SupplierHasIngredient,Long> {

    @Query("SELECT s FROM SupplierHasIngredient s WHERE (s.ingredient.name LIKE %:keyword% OR s.ingredient.description LIKE %:keyword%) AND s.supplier.id=:userId")
    Page<SupplierHasIngredient> findAllByKeywordAndUserId(Pageable pageable,@Param("keyword") String keyword,@Param("userId") Long id);

    @Query("SELECT s FROM SupplierHasIngredient s WHERE s.supplier.id=:userId")
    Page<SupplierHasIngredient> findAllByUserId(Pageable pageable,@Param("userId") Long id);

}
