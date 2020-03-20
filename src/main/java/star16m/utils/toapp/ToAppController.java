package star16m.utils.toapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import star16m.utils.toapp.keyword.KeywordRepository;
import star16m.utils.toapp.torrent.TorrentRepository;
import star16m.utils.toapp.torrent.TorrentService;

@Controller
@Slf4j
public class ToAppController {
    @Autowired
    private TorrentService service;
    @Autowired
    private TorrentRepository torrentRepository;
    @Autowired
    private KeywordRepository keywordRepository;

    @RequestMapping(value = "{path:[^\\\\.]*}", method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.PUT})
    public String vueForwarding() {
        return "forward:/index.html";
    }
}
