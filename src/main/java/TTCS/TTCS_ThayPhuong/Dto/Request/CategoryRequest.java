package TTCS.TTCS_ThayPhuong.Dto.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequest {
    @NotBlank(message = "Name Category cannot be null")
    private String name;

    @NotBlank(message = "description cannot be null")
    private String description;
}
