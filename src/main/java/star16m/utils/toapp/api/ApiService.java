package star16m.utils.toapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import star16m.utils.toapp.keyword.KeywordRepository;
import star16m.utils.toapp.torrent.Torrent;
import star16m.utils.toapp.torrent.TorrentService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ApiService {

    @Value("${app.api.lastDays:3}")
    private Integer lastDays;
    @Value("${app.api.top:10}")
    private Integer top;
    @Autowired
    private TorrentService torrentService;
    @Autowired
    private KeywordRepository keywordRepository;

    public List<ApiDataInfo<String>> getDataInfos() {
        List<Map<String, Long>> keywordMapList = this.torrentService.groupByKeyword();
        List<ApiDataInfo<String>> filterRequestList = keywordMapList.stream().map(k -> ApiDataInfo.<String>builder()
                .filterRequestType(FilterRequestType.KEYWORD)
                .filterTarget(String.valueOf(k.get("id")))
                .filteredResult(k.get("result"))
                .build())
                .collect(Collectors.toList());
        List<Torrent> torrentsByLastDays = this.torrentService.selectByLastDays(3L);
        List<Torrent> torrentsByTop = this.torrentService.selectByTop(10L);
        filterRequestList.add(0, ApiDataInfo.<String>builder().filterRequestType(FilterRequestType.LAST_DAYS).filterTarget(this.lastDays.toString()).filteredResult(Long.valueOf(torrentsByLastDays.size())).build());
        filterRequestList.add(0, ApiDataInfo.<String>builder().filterRequestType(FilterRequestType.TOP).filterTarget(this.top.toString()).filteredResult(Long.valueOf(torrentsByTop.size())).build());
        return filterRequestList;
    }
}
