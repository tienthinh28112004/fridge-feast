package TTCS.TTCS_ThayPhuong.Controller;

import TTCS.TTCS_ThayPhuong.Dto.Request.CommentRequest;
import TTCS.TTCS_ThayPhuong.Dto.Request.CommentUpdateRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.ApiResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.CommentResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.CommentUpdateResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.PageResponse;
import TTCS.TTCS_ThayPhuong.Service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping(value = "/getCommentsByDish/{dishId}")
    public ApiResponse<PageResponse<List<CommentResponse>>> commentByBook (@PathVariable Long dishId,
                                                                           @RequestParam(defaultValue = "1", required = false) int page,
                                                                           @RequestParam(defaultValue = "10", required = false) int size) {
        return ApiResponse.<PageResponse<List<CommentResponse>>>builder()
                .message("successfully")
                .result(commentService.getCommentsByDish(dishId,page,size))
                .build();
    }
    @PostMapping(value = "/insertComment")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<CommentResponse> insertComment (@RequestBody @Valid CommentRequest request) {
        return ApiResponse.<CommentResponse>builder()
                .message("Comment successfully")
                .result(commentService.insertComment(request))
                .build();
    }

    @PreAuthorize("isAuthenticated() or hasAuthority('ADMIN')")
    @DeleteMapping(value = "/deleteComment/{commentId}")
    public ApiResponse<Void> deleteComment (@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ApiResponse.<Void>builder()
                .message("Delete comment successfully")
                .build();
    }

    @PutMapping(value = "/updateComment/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<CommentUpdateResponse> updateComment (@PathVariable Long commentId, @RequestBody @Valid CommentUpdateRequest request) {
        return ApiResponse.<CommentUpdateResponse>builder()
                .message("Update comment successfully")
                .result(commentService.updateComment(commentId,request))
                .build();
    }

}
