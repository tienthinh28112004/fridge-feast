package TTCS.TTCS_ThayPhuong.Service.Impl;

import TTCS.TTCS_ThayPhuong.Dto.Request.CommentRequest;
import TTCS.TTCS_ThayPhuong.Dto.Request.CommentUpdateRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.CommentResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.CommentUpdateResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.PageResponse;
import TTCS.TTCS_ThayPhuong.Entity.Comment;
import TTCS.TTCS_ThayPhuong.Repository.CommentRepository;
import TTCS.TTCS_ThayPhuong.Service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    @Override
    public CommentResponse insertComment(CommentRequest request) {
        return null;
    }

    @Override
    public void deleteComment(Long commentId) {

        //commentRepository.deleteById(commentId);
    }

    @Override
    public CommentUpdateResponse updateComment(Long commentId, CommentUpdateRequest request) {
//        Comment comment = commentRepository.findById(commentId);
//        comment.
        return null;
    }

    @Override
    public PageResponse<List<CommentResponse>> getCommentsByBook(Long bookId, int page, int size) {
        return null;
    }

    @Override
    public List<CommentResponse> getUserComments(Long userId) {
        return null;
    }
}
