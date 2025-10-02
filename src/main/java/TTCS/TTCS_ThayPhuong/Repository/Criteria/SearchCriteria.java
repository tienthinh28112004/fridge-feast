package TTCS.TTCS_ThayPhuong.Repository.Criteria;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;
}
