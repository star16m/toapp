package star16m.utils.toapp.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiFilterRequest {
    private FilterRequestType filterRequestType;
    private String filterTarget;
}
