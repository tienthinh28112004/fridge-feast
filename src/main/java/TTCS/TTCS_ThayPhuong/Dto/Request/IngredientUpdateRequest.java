package TTCS.TTCS_ThayPhuong.Dto.Request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientUpdateRequest {
    private String name;

    private String description;

    private String unit;
}
