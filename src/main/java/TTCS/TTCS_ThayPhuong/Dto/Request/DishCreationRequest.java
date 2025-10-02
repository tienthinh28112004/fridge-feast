package TTCS.TTCS_ThayPhuong.Dto.Request;

import TTCS.TTCS_ThayPhuong.Entity.Comment;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DishCreationRequest {

    private String name; //rau muốn xào tỏi

    private String description;//món ăn thích hợp vào ngày nắng nóng

    private String recipe;

    private Long timeCook;//15

    private Float price;//20k

    private List<Long> listCategoryId;//1,2,3

    private List<Long> listIngredientId;//1,2,4
}
