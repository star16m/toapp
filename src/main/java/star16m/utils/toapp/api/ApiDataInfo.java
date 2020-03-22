package star16m.utils.toapp.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiDataInfo {
    private FilterRequestType filterRequestType;
    private Long filterTarget;
    private Long filteredResult;
}
