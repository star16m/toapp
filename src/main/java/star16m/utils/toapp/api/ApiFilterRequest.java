package star16m.utils.toapp.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiFilterRequest {
    private FilterRequestType filterRequestType;
    private Long filterTarget;
    enum FilterRequestType {
        KEYWORD,
        TOP,
        LAST_DAYS,
    }
}
