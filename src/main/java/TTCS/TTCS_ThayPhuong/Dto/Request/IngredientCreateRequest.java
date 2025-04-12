package TTCS.TTCS_ThayPhuong.Dto.Request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientCreateRequest {
    @NotBlank
    private String name;

    private String description;

    private String unit;
}
