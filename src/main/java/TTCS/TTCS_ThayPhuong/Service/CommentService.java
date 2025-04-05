package TTCS.TTCS_ThayPhuong.Service;

import TTCS.TTCS_ThayPhuong.Dto.Request.CommentRequest;
import TTCS.TTCS_ThayPhuong.Dto.Request.CommentUpdateRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.CommentResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.CommentUpdateResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.PageResponse;

import java.util.List;

public interface CommentService {
    CommentResponse insertComment(CommentRequest request);
    void deleteComment(Long commentId);
    CommentUpdateResponse updateComment(Long commentId, CommentUpdateRequest request);
    PageResponse<List<CommentResponse>> getCommentsByBook(Long bookId, int page, int size);
    List<CommentResponse> getUserComments(Long userId);
}
