package star16m.utils.toapp.site;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TempExtractResult {
    private Site site;
    private String result;
    private List<TempSiteExtractResult> extractResultList;
}
