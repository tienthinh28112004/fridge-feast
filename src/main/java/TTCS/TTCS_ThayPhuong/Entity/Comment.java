package TTCS.TTCS_ThayPhuong.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments")
public class Comment extends AbstractEntity<Long>{

    @ManyToOne()
    @JoinColumn(name = "dish_id")
    @JsonBackReference
    private Dish dish;
    //thiếu nguyên liệu ở đây nữa
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)//chỉ ra khi bị gọi
    @JoinColumn(name = "parent_comment_id")
    Comment parentComment;

    @OneToMany(mappedBy = "parentComment",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    List<Comment> replies;
}

