package TTCS.TTCS_ThayPhuong.Dto.Response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentUpdateResponse {
    private Long commentId;

    private String content;
}
