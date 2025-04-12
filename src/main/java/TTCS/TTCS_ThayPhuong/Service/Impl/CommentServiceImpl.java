package TTCS.TTCS_ThayPhuong.Service.Impl;

import TTCS.TTCS_ThayPhuong.Dto.Request.CommentRequest;
import TTCS.TTCS_ThayPhuong.Dto.Request.CommentUpdateRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.CommentResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.CommentUpdateResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.PageResponse;
import TTCS.TTCS_ThayPhuong.Entity.Comment;
import TTCS.TTCS_ThayPhuong.Entity.Dish;
import TTCS.TTCS_ThayPhuong.Entity.User;
import TTCS.TTCS_ThayPhuong.Entity.UserHasRole;
import TTCS.TTCS_ThayPhuong.Enums.Role;
import TTCS.TTCS_ThayPhuong.Exception.BadRequestException;
import TTCS.TTCS_ThayPhuong.Exception.NotFoundException;
import TTCS.TTCS_ThayPhuong.Exception.TokenExpireException;
import TTCS.TTCS_ThayPhuong.Repository.CommentRepository;
import TTCS.TTCS_ThayPhuong.Repository.DishRepository;
import TTCS.TTCS_ThayPhuong.Repository.UserRepository;
import TTCS.TTCS_ThayPhuong.Service.CommentService;
import TTCS.TTCS_ThayPhuong.Util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final DishRepository dishRepository;
    @Override
    public CommentResponse insertComment(CommentRequest request) {
        String email = SecurityUtils.getCurrentLogin()
                .orElseThrow(()-> new TokenExpireException("Bạn chưa đăng nhập"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));

        Dish dish = dishRepository.findById(request.getDishId())
                .orElseThrow(()->new NotFoundException("Dish not found"));

        Comment parentComment = null;
        if(request.getParentCommentId()!=null){//nếu null thì đây là comment đầu tiên
            parentComment = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(()->new NotFoundException("Comment not found"));
        }
        if(StringUtils.hasLength(request.getContent())){
            throw new BadRequestException("Bạn chưa comment");
        }
        Comment newComment= Comment.builder()
                .user(user)
                .dish(dish)
                .parentComment(parentComment)
                .content(request.getContent())
                .build();

        commentRepository.save(newComment);
        return CommentResponse.convert(newComment);
    }

    @Override
    public void deleteComment(Long commentId) {
        String email = SecurityUtils.getCurrentLogin()
                .orElseThrow(()-> new TokenExpireException("Bạn chưa đăng nhập"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));

        Comment comment=commentRepository.findById(commentId)
                .orElseThrow(()->new NotFoundException("Comment not found"));
        boolean admin=false;
        for(UserHasRole x : user.getUserHasRoles()){
            if(x.getRole().getName().equalsIgnoreCase(String.valueOf(Role.ADMIN))){
                admin=true;
                break;
            }
        }
        if(admin || Objects.equals(user.getId(),comment.getUser().getId())){
            commentRepository.deleteById(commentId);
        }
    }

    @Override
    public CommentUpdateResponse updateComment(Long commentId, CommentUpdateRequest request) {
        String email = SecurityUtils.getCurrentLogin()
                .orElseThrow(()-> new TokenExpireException("Bạn chưa đăng nhập"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));

        Comment comment=commentRepository.findById(commentId)
                .orElseThrow(()->new NotFoundException("Comment not found"));

        if(!Objects.equals(comment.getUser().getId(),user.getId())){
            throw new BadRequestException("Email đang đăng nhập không trùng với email của hệ thống");
        }
        comment.setContent(request.getContent());
        return CommentUpdateResponse.builder()
                .content(request.getContent())
                .build();
    }

    @Override
    public PageResponse<List<CommentResponse>> getCommentsByDish(Long dishId, int page, int size) {
        Sort sort=Sort.by(Sort.Direction.DESC,"createdAt");

        Pageable pageable= PageRequest.of(page-1,size,sort);
        Page<Comment> commentPage=commentRepository.findCommentByDishIdAndParentCommentIsNull(dishId,pageable);

        List<CommentResponse> responseList=commentPage.stream().map(CommentResponse::convert).toList();
        List<Comment> totalComment = commentRepository.findCommentByDishId( dishId );
        return PageResponse.<List<CommentResponse>>builder()
                        .currentPage(page)
                        .pageSize(size)
                        .items(responseList)
                        .totalElements((long) totalComment.size())
                .build();
    }
}
