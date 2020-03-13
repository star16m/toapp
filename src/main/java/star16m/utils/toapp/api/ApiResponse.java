package star16m.utils.toapp.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private static final EmptyBody EMPTY_BODY = new EmptyBody();
    private ApiHeader header;
    private T body;

    public static <T> ApiResponse<T> ok(T body) {
        return of(ApiHeader.SUCCESS, body);
    }

    public static <T> ApiResponse<T> of(ApiHeader header, T body) {
        return new ApiResponse<>(header, body);
    }

    public static ApiResponse<EmptyBody> emptyBody(ApiHeader header) {
        return of(header, EMPTY_BODY);
    }

    static class EmptyBody {
    }
}
