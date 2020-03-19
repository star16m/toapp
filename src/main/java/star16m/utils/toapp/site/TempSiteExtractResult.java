package star16m.utils.toapp.site;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TempSiteExtractResult {
    private String title;
    private String linkURL;
    private String size;
    private String date;
}
