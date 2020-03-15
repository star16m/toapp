package star16m.utils.toapp.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import star16m.utils.toapp.commons.message.Message;
import star16m.utils.toapp.commons.message.MessageService;
import star16m.utils.toapp.commons.utils.ToAppUtils;
import star16m.utils.toapp.keyword.Keyword;
import star16m.utils.toapp.keyword.KeywordRepository;
import star16m.utils.toapp.site.Site;
import star16m.utils.toapp.site.SiteService;
import star16m.utils.toapp.site.TempSite;
import star16m.utils.toapp.site.TempSiteDetail;
import star16m.utils.toapp.torrent.Torrent;
import star16m.utils.toapp.torrent.TorrentService;
import star16m.utils.toapp.torrent.collector.TorrentCollector;

import java.io.IOException;
import java.util.List;

@Slf4j
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
    @Autowired
    private TorrentCollector torrentCollector;

    /**
     * =====================================================================
     * SITE
     * =====================================================================
     */
    @GetMapping("sites")
    public List<TempSite> getSites() {
        return this.siteService.findAll();
    }

    @PostMapping("site")
    public ApiResponse<TempSite> createSite(@RequestBody TempSite site) {
        TempSite createdSite = this.siteService.createTempSite(site);
        return ApiResponse.ok(createdSite);
    }
    @PostMapping("site/{siteId}")
    public ApiResponse<String> getSiteDetail(@PathVariable Long siteId) throws IOException {
        TempSite tempSite = this.siteService.getTempSiteById(siteId);
        Keyword keyword = this.keywordRepository.findAll().stream().findFirst().orElse(null);
        if (keyword == null) {
            return ApiResponse.of(ApiHeader.NOT_FOUND, null);
        }
        return ApiResponse.ok(this.torrentCollector.downloadTorrentSite(tempSite.getSearchUrl(), keyword.getKeyword()));
    }
    @PostMapping("site/{siteId}/find")
    public ApiResponse<String> findSiteDetail(@PathVariable Long siteId, @RequestBody TempSiteDetail siteDetail) throws IOException {
        log.info("request site find. siteId[{}], request[{}]", siteId, siteDetail);
        String tempSite = this.torrentCollector.findTempSite(siteDetail.getPageSelector());
        return ApiResponse.ok(tempSite);
    }
    @DeleteMapping("site/{siteId}")
    public ApiResponse<ApiResponse.EmptyBody> deleteSite(@PathVariable Long siteId) {
        this.siteService.deleteTempSite(siteId);
        return ApiResponse.emptyBody(ApiHeader.SUCCESS);
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
