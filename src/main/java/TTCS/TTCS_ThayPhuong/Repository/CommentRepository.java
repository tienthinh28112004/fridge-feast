package TTCS.TTCS_ThayPhuong.Repository;

import TTCS.TTCS_ThayPhuong.Entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    void deleteById(Long commentId);

    Optional<Comment> findById(Long commentId);

    @Query("SELECT c FROM Comment c WHERE c.supplierHasIngredient.id=:hasIngredient AND c.parentComment IS NULL")
    Page<Comment> findCommentByHasIngredientIdAndParentCommentIsNull(@Param("hasIngredient") Long hasingredient, Pageable pageable);

    List<Comment> findCommentBySupplierHasIngredientId(Long supplierHasIngredientId);
}
