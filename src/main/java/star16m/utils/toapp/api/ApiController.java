package star16m.utils.toapp.api;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.select.Selector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import star16m.utils.toapp.commons.message.Message;
import star16m.utils.toapp.commons.message.MessageService;
import star16m.utils.toapp.commons.page.PageConnector;
import star16m.utils.toapp.commons.utils.ToAppUtils;
import star16m.utils.toapp.keyword.Keyword;
import star16m.utils.toapp.keyword.KeywordRepository;
import star16m.utils.toapp.site.*;
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
    public List<Site> getSites() {
        return this.siteService.findAll();
    }

    @PostMapping("site")
    public ApiResponse<Site> createSite(@RequestBody Site site) {
        if (ToAppUtils.isEmpty(site.getPageSelector())) {
            site.setPageSelector("div");
        }
        if (ToAppUtils.isEmpty(site.getTorrentNameReplace())) {
            site.setTorrentNameReplace(">(.+)<");
        }
        if (ToAppUtils.isEmpty(site.getTorrentSizeReplace())) {
            site.setTorrentSizeReplace(">(.+)<");
        }
        if (ToAppUtils.isEmpty(site.getTorrentMagnetHashReplace())) {
            site.setTorrentMagnetHashReplace(".+href=\".+:([^\"]+)\".+");
        }
        Site createdSite = this.siteService.createTempSite(site);
        return ApiResponse.ok(createdSite);
    }

    @PatchMapping("site/{siteId}/useable")
    public ApiResponse<Site> updateUseable(@PathVariable Long siteId, @RequestBody Site.SimpleSiteRequest<Boolean> siteRequest) {
        Site realSite = this.siteService.getSiteById(siteId);
        realSite.setUseable(siteRequest.getRequest());
        this.siteService.save(realSite);
        return ApiResponse.ok(realSite);
    }

    @PostMapping("site/{siteId}")
    public ApiResponse<TempExtractResult> getSiteDetail(@PathVariable Long siteId) throws IOException {
        Site site = this.siteService.getSiteById(siteId);
        String result = this.torrentCollector.downloadTorrentSite(site);
        TempExtractResult tempExtractResult = TempExtractResult.builder()
                .site(site)
                .result(result)
                .build();
        return ApiResponse.ok(tempExtractResult);
    }
    @PostMapping("site/{siteId}/copy")
    public ApiResponse<Site> copySite(@PathVariable Long siteId) throws IOException {
        Site site = this.siteService.copySiteBySiteId(siteId);
        return ApiResponse.ok(site);
    }

    @PostMapping("site/{siteId}/find")
    public ApiResponse<TempExtractResult> findSiteSource(@PathVariable Long siteId, @RequestBody Site siteDetail) throws IOException {
        log.info("request site. siteId[{}], request[{}]", siteId, siteDetail);
        TempExtractResult tempExtractResult = this.torrentCollector.findTempSite(siteDetail);
        if (tempExtractResult == null) {
            return ApiResponse.of(ApiHeader.NOT_FOUND, null);
        }
        return ApiResponse.ok(tempExtractResult);
    }

    @PostMapping("site/{siteId}/findDetail")
    public ApiResponse<TempExtractDetailResult> findSiteDetailSource(@PathVariable Long siteId, @RequestBody TempSiteDetailRequest detailRequest) throws IOException {
        log.info("request detail site. siteId[{}], detailRequest[{}]", siteId, detailRequest);
        Site site = detailRequest.getSite();
        if (ToAppUtils.isEmpty(site.getTorrentNameSelector()) && ToAppUtils.isEmpty(site.getTorrentNameReplace())
                && ToAppUtils.isEmpty(site.getTorrentSizeSelector()) && ToAppUtils.isEmpty(site.getTorrentSizeReplace())
                && ToAppUtils.isEmpty(site.getTorrentMagnetHashSelector()) && ToAppUtils.isEmpty(site.getTorrentMagnetHashReplace())) {
            return ApiResponse.ok(TempExtractDetailResult.builder().detailPageSource(new PageConnector(detailRequest.getDetailPageUrl()).getElementsString()).build());
        }
        Torrent.TorrentSimpleInfo tempExtractResult = null;
        PageConnector detailPageConnector = new PageConnector(detailRequest.getDetailPageUrl());
        Torrent.TorrentSimpleInfo torrentPageInfo = new Torrent.TorrentSimpleInfo();
        try {
            if (ToAppUtils.isNotEmpty(detailPageConnector.find(site.getTorrentNameSelector()))) {
                String torrentName = detailPageConnector.find(site.getTorrentNameSelector());
                if (ToAppUtils.isNotEmpty(site.getTorrentNameReplace())) {
                    torrentName = ToAppUtils.replaceGroup(torrentName, site.getTorrentNameReplace());
                }
                torrentPageInfo.setTorrentName(torrentName);
            }
            if (ToAppUtils.isNotEmpty(site.getTorrentSizeSelector())) {
                String torrentSize = detailPageConnector.find(site.getTorrentSizeSelector());
                if (ToAppUtils.isNotEmpty(site.getTorrentSizeReplace())) {
                    torrentSize = ToAppUtils.replaceGroup(torrentSize, site.getTorrentSizeReplace());
                }
                torrentPageInfo.setTorrentSize(torrentSize);
            }
            if (ToAppUtils.isNotEmpty(site.getTorrentMagnetHashSelector())) {
                String torrentMagnet = detailPageConnector.find(site.getTorrentMagnetHashSelector());
                if (ToAppUtils.isNotEmpty(site.getTorrentMagnetHashReplace())) {
                    torrentMagnet = ToAppUtils.replaceGroup(torrentMagnet, site.getTorrentMagnetHashReplace());
                }
                torrentPageInfo.setTorrentMagnet(torrentMagnet);
            }
        } catch (Selector.SelectorParseException e) {
            log.error("Error occurred while parsing detail page.", e);
            return ApiResponse.of(ApiHeader.PARSE_ERROR, null);
        }
        return ApiResponse.ok(TempExtractDetailResult.builder().detailPageSource(detailPageConnector.getElementsString()).extractDetailResult(torrentPageInfo).build());
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
