package TTCS.TTCS_ThayPhuong.Dto.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequest implements Serializable {
    @NotBlank(message = "content cannot be null")
    private String content;

    private Long parentCommentId;

    private Long dishId;
}
