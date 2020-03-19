package star16m.utils.toapp.site;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import star16m.utils.toapp.torrent.Torrent;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TempExtractDetailResult {
    private String detailPageSource;
    private Torrent.TorrentSimpleInfo extractDetailResult;
}
