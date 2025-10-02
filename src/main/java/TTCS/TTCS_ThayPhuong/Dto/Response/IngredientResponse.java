package TTCS.TTCS_ThayPhuong.Dto.Response;

import TTCS.TTCS_ThayPhuong.Entity.Ingredient;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientResponse {
    private Long ingredientId;

    private String name;

    private String description;

    private String unit;

    private boolean isActive;

    private String ingredientImage;

    public static IngredientResponse convert(Ingredient ingredient){
        return IngredientResponse.builder()
                .ingredientId(ingredient.getId())
                .name(ingredient.getName())
                .description(ingredient.getDescription())
                .unit(ingredient.getUnit())
                .isActive(ingredient.isActive())
                .ingredientImage(ingredient.getIngredientImage())
                .build();
    }
}
