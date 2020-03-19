package star16m.utils.toapp.site;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TempSiteDetailRequest {
    private Site site;
    private String detailPageUrl;
}
