package TTCS.TTCS_ThayPhuong.Repository;

import TTCS.TTCS_ThayPhuong.Entity.Comment;

public interface CommentRepository {
    void deleteById(Long commentId);

    Comment findById(Long commentId);
}
