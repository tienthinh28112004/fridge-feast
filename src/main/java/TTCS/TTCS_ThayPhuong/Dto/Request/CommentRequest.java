package TTCS.TTCS_ThayPhuong.Dto.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "DishId cannot be null")
    @Min(value = 1,message = "Dish must be greater than 0")
    private Long dishId;
}
