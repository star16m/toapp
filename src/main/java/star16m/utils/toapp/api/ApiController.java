package star16m.utils.toapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import star16m.utils.toapp.commons.message.Message;
import star16m.utils.toapp.commons.message.MessageService;
import star16m.utils.toapp.commons.utils.ToAppUtils;
import star16m.utils.toapp.keyword.Keyword;
import star16m.utils.toapp.keyword.KeywordRepository;
import star16m.utils.toapp.site.Site;
import star16m.utils.toapp.site.SiteService;
import star16m.utils.toapp.torrent.Torrent;
import star16m.utils.toapp.torrent.TorrentService;

import java.util.List;

@RequestMapping("/api")
@RestController
public class ApiController {
    @Autowired
    private SiteService siteService;
    @Autowired
    private KeywordRepository keywordRepository;
    @Autowired
    private MessageService messageService;
    @Autowired
    private TorrentService torrentService;

    @GetMapping("sites")
    public List<Site> getSites() {
        return this.siteService.findAll();
    }

    /**
     * =====================================================================
     * KEYWORD
     * =====================================================================
     */
    @GetMapping("keywords")
    public List<Keyword> getKeywords() {
        return this.keywordRepository.findAll();
    }

    @PostMapping("keywords")
    public ApiResponse<Keyword> addNewKeyword(@RequestBody Keyword keyword) {
        if (keyword == null || ToAppUtils.isEmpty(keyword.getKeyword())) {
            return ApiResponse.of(ApiHeader.SUBJECT_SHOULD_NOT_BE_EMPTY, null);
        }
        return ApiResponse.ok(this.keywordRepository.save(keyword));
    }

    @DeleteMapping("keywords/{keywordId}")
    public ApiResponse<ApiResponse.EmptyBody> deleteKeyword(@PathVariable Long keywordId) {
        Keyword keyword = this.keywordRepository.findOne(keywordId);
        if (keyword == null) {
            return ApiResponse.emptyBody(ApiHeader.NOT_FOUND);
        }
        this.keywordRepository.delete(keywordId);
        return ApiResponse.emptyBody(ApiHeader.SUCCESS);
    }

    /**
     * =====================================================================
     * MESSAGE
     * =====================================================================
     */
    @GetMapping("messages")
    public ApiResponse<List<Message>> getMessages() {
        return ApiResponse.ok(this.messageService.findAll());
    }

    /**
     * =====================================================================
     * DATA
     * =====================================================================
     */
    @GetMapping("datas")
    public ApiResponse<List<Torrent>> getDatas() {
        return ApiResponse.ok(this.torrentService.selectAll());
    }
}
