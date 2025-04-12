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
    private String name;

    private String description;

    private String recipe;

    private Long timeCook;

    private Float price;

    private String dishImage;

    private List<Long> listCategoryId;

    private List<Long> listIngredientId;
}
