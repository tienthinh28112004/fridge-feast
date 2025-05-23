package TTCS.TTCS_ThayPhuong.Repository;

import TTCS.TTCS_ThayPhuong.Entity.Ingredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient,Long> {

    @Query("SELECT i FROM Ingredient i WHERE i.name LIKE %:keyword%")
    List<Ingredient> findByIngredientKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT i FROM Ingredient i WHERE i.name LIKE %:keyword% OR i.description LIKE %:keyword%")
    Page<Ingredient> findAllByKeyword(Pageable pageable,@Param("keyword") String keyword);
}
