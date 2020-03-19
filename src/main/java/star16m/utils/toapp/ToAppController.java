package star16m.utils.toapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import star16m.utils.toapp.keyword.Keyword;
import star16m.utils.toapp.keyword.KeywordRepository;
import star16m.utils.toapp.torrent.Torrent;
import star16m.utils.toapp.torrent.TorrentRepository;
import star16m.utils.toapp.torrent.TorrentService;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class ToAppController {
    @Autowired
    private TorrentService service;
    @Autowired
    private TorrentRepository torrentRepository;
    @Autowired
    private KeywordRepository keywordRepository;

    @RequestMapping("/")
    public String index(Map<String, Object> model) {
        List<Torrent> torrents = service.selectAll();
        log.debug("current torrents is " + torrents.size() + ".");
        model.put("torrents", torrents);
        List<Keyword> kList = keywordRepository.selectLatestkeyword();
        model.put("notDownloadKeyword", kList);
        return "index";
    }
}
