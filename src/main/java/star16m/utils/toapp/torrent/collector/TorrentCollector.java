package star16m.utils.toapp.torrent.collector;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import star16m.utils.toapp.ToAppConstants;
import star16m.utils.toapp.commons.errors.ToAppException;
import star16m.utils.toapp.commons.message.MessageRepository;
import star16m.utils.toapp.commons.message.MessageService;
import star16m.utils.toapp.commons.page.PageConnector;
import star16m.utils.toapp.commons.utils.ToAppUtils;
import star16m.utils.toapp.keyword.Keyword;
import star16m.utils.toapp.keyword.KeywordRepository;
import star16m.utils.toapp.site.Site;
import star16m.utils.toapp.site.SiteRepository;
import star16m.utils.toapp.site.TempExtractResult;
import star16m.utils.toapp.site.TempSiteExtractResult;
import star16m.utils.toapp.torrent.Torrent;
import star16m.utils.toapp.torrent.TorrentRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
@Slf4j
public class TorrentCollector {

    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private KeywordRepository keywordRepository;
    @Autowired
    private TorrentRepository torrentRepository;
    @Autowired
    private MessageService commonService;
    @Autowired
    private MessageRepository messageRepository;

    private Site currentEditSite;
    private PageConnector currentDownloadedPage;

    @Scheduled(cron = "0 3/33 10-22 * * *")
    public void collect() {
        List<Site> siteList = siteRepository.findByUseableTrue();
        List<Keyword> keywordList = keywordRepository.findAll();
        if (ToAppUtils.isEmpty(siteList) || ToAppUtils.isEmpty(keywordList)) {
            log.warn("There is no site or keyword.");
            commonService.saveMessage("collect", "there is no site or keyword.");
            return;
        }

        siteList.stream().forEach(site -> {
            keywordList.stream().forEach(keyword -> {
                log.info("##### try collect by site[{}], keyword[{}]", site.getName(), keyword.getKeyword());
                CollectResult result = null;
                try {
                    result = collect(site, keyword);
                    commonService.saveMessage("cron-30", result.getResultString());
                } catch (Exception e) {
                    log.error("error occured while collect torrent file", e);
                    commonService.saveMessage("cron-30", "[ERROR] " + e.getMessage());
                }
            });
        });
    }

    /**
     * 매일 저녁 11시 5분에 데이터 정리
     */
    @Scheduled(cron = "0 5 23 * * *")
    @Transactional
    public void cleanOldData() {
        log.info("try clean Old Data.");
        this.torrentRepository.deleteOldData(ToAppConstants.TARGET_DATE_RANGE);
        this.messageRepository.deleteOLdMessage(ToAppConstants.MESSAGE_AVAILABLE_DAY);
    }

    public void collect(String keywordString) {
        log.info("try to collect by keyword [{}]", keywordString);
        final Keyword keyword = keywordRepository.findByKeyword(keywordString);
        final Keyword searchKeyword = new Keyword(keyword.getId(), keyword.getKeyword());
        log.info("found keyword [{}]", searchKeyword);
        final List<Site> siteList = siteRepository.findByUseableTrue();
        if (siteList != null && siteList.size() > 0) {
            new Thread(() -> siteList.stream().forEach(site -> {
                try {
                    CollectResult result = collect(site, searchKeyword);
                    commonService.saveMessage("col-key", result.getResultString());
                } catch (ToAppException e) {
                    log.warn(e.getMessage());
                }
            })).start();
        }
    }

    public CollectResult collect(Site site, Keyword keyword) throws ToAppException {
        int alreadyExistsMagnetNum = 0;
        int foundAndSaveTorrentNum = 0;
        int totalElementNum = 0;
        try {
            List<Torrent.TorrentLinkInfo> torrentDetailPageList = findTorrentLinks(site, keyword.getKeyword());
            totalElementNum = torrentDetailPageList.size();
            log.info("try to connect torrent detail page [{}]", torrentDetailPageList);
            for (Torrent.TorrentLinkInfo torrentLinkInfo : torrentDetailPageList) {

                if (torrentLinkInfo.getCreateDate() != null && !ToAppUtils.isTargetDate(torrentLinkInfo.getCreateDate())) {
                    continue;
                }
                if (torrentRepository.existsByUrl(torrentLinkInfo.getLinkURL())) {
                    continue;
                }
                Torrent.TorrentSimpleInfo torrentInfo = null;
                try {
                    torrentInfo = findTorrentInfo(torrentLinkInfo, site);
                } catch (ToAppException e) {
                    // ignore exception.
                    continue;
                }
                Torrent torrent = new Torrent();
                torrent.setMagnetCode(torrentInfo.getTorrentMagnet());
                torrent.setTitle(torrentInfo.getTorrentName());
                torrent.setUrl(torrentLinkInfo.getLinkURL());
                torrent.setSize(torrentInfo.getTorrentSize());
                torrent.setSiteName(site.getName());
                torrent.setKeyword(keyword.getKeyword());
                torrent.setDownload(false);
                log.warn("download name [{}], createDate [{}]", torrentLinkInfo.getTitle(), torrentLinkInfo.getCreateDate());
                torrent.setDateString(ToAppConstants.DATE_TIME_FORMATTER.format(torrentLinkInfo.getCreateDate()));
                torrent.setTorrentFindDate(LocalDate.now());
                if (torrentRepository.existsByMagnetCode(torrent.getMagnetCode())) {
                    alreadyExistsMagnetNum++;
                } else {
                    torrentRepository.saveAndFlush(torrent);
                    foundAndSaveTorrentNum++;
                }
            }
        } catch (ToAppException e) {
            log.error("error occured while collect. site=[{}], keyword=[{}], error=[{}]", site, keyword, e);
            throw e;
        }
        return new CollectResult(site, keyword, totalElementNum, foundAndSaveTorrentNum, false, "already exists(by magnet hash) -> " + alreadyExistsMagnetNum);
    }

    public String getTorrentURL(String torrentURL, String keywordString) throws UnsupportedEncodingException {
        return ToAppUtils.replace(torrentURL, ToAppConstants.SEARCH_KEYWORD, URLEncoder.encode(keywordString, "UTF-8"));
    }

    public List<Torrent.TorrentLinkInfo> findTorrentLinks(Site site, String keyword) throws ToAppException {
        List<Torrent.TorrentLinkInfo> foundResult = new ArrayList<>();
        try {
            String url = getTorrentURL(site.getSearchUrl(), keyword);
            PageConnector pageConnector = new PageConnector(url);
            pageConnector.find(site.getPageSelector(), (e, i) -> {
                Torrent.TorrentLinkInfo link = new Torrent.TorrentLinkInfo();
                String title = e.select(site.getNameSelector()).text();
                Elements linkElement = e.select(site.getNameSelector());
                Elements sizeElement = e.select(site.getSizeSelector());
                Elements dateElement = e.select(site.getDateSelector());
                if (ToAppUtils.isNotEmpty(title)
                        && linkElement != null
                        && linkElement.select("a") != null
                        && sizeElement != null
                        && sizeElement.first() != null
                        && dateElement != null
                        && dateElement.first() != null) {
                    link.setTitle(title);
                    link.setLinkURL(linkElement.select("a").attr("abs:href"));
                    link.setSize(sizeElement.first().text());
                    link.setCreateDate(ToAppUtils.getDateTime(dateElement.first().text()));
                    foundResult.add(link);
                }
            });
        } catch (Exception e) {
            throw new ToAppException(e);
        }
        return foundResult;
    }

    public Torrent.TorrentSimpleInfo findTorrentInfo(Torrent.TorrentLinkInfo torrentLinkInfo, Site site) throws ToAppException {
        PageConnector pageConnector = null;
        try {
            log.info("try to url [{}]", torrentLinkInfo.getLinkURL());
            pageConnector = new PageConnector(torrentLinkInfo.getLinkURL());
            Torrent.TorrentSimpleInfo torrentPageInfo = new Torrent.TorrentSimpleInfo();
            if (ToAppUtils.isNotEmpty(site.getTorrentNameSelector())) {
                String torrentName = pageConnector.find(site.getTorrentNameSelector());
                if (ToAppUtils.isNotEmpty(torrentName) && ToAppUtils.isNotEmpty(site.getTorrentNameReplace())) {
                    torrentName = ToAppUtils.replaceGroup(torrentName, site.getTorrentNameReplace());
                    log.info("replaced torrentName[{}]", torrentName);
                }
                torrentPageInfo.setTorrentName(torrentName);
            } else {
                torrentPageInfo.setTorrentName(torrentLinkInfo.getTitle());
            }
            if (ToAppUtils.isNotEmpty(site.getTorrentSizeSelector())) {
                String torrentSize = pageConnector.find(site.getTorrentSizeSelector());
                if (ToAppUtils.isNotEmpty(torrentSize) && ToAppUtils.isNotEmpty(site.getTorrentSizeReplace())) {
                    torrentSize = ToAppUtils.replaceGroup(torrentSize, site.getTorrentSizeReplace());
                    log.info("replaced torrentSize[{}]", torrentSize);
                }
                torrentPageInfo.setTorrentSize(torrentSize);
            } else {
                torrentPageInfo.setTorrentSize(torrentLinkInfo.getSize());
            }
            String torrentMagnet = pageConnector.find(site.getTorrentMagnetHashSelector());
            if (ToAppUtils.isNotEmpty(torrentMagnet) && ToAppUtils.isNotEmpty(site.getTorrentMagnetHashReplace())) {
                torrentMagnet = ToAppUtils.replaceGroup(torrentMagnet, site.getTorrentMagnetHashReplace());
                log.info("replaced torrentMagnet[{}]", torrentMagnet);
            }
            torrentPageInfo.setTorrentMagnet(torrentMagnet);
            log.info("founded torrentName[{}], torrentSize[{}], torrentMagnet[{}]", torrentPageInfo.getTorrentName(), torrentPageInfo.getTorrentSize(), torrentPageInfo.getTorrentMagnet());
            return torrentPageInfo;
        } catch (Exception e) {
            throw new ToAppException(e);
        }
    }

    public void clearDownloadedTorrentSite() {
        this.currentDownloadedPage = null;
        this.currentEditSite = null;
    }

    public String downloadTorrentSite(Site site) throws Exception {
        Keyword keyword = this.keywordRepository.findAll().stream().findFirst().orElse(null);
        if (keyword == null) {
            return null;
        }
        this.currentEditSite = site;
        this.currentDownloadedPage = new PageConnector(this.getTorrentURL(this.currentEditSite.getSearchUrl(), keyword.getKeyword()));
        return this.currentDownloadedPage.getElementsString();
    }

    public TempExtractResult findTempSite(Site site) throws Exception {
        if (this.currentDownloadedPage == null || this.currentEditSite == null) {
            return null;
        }
        if (!this.currentEditSite.getId().equals(site.getId())) {
            return null;
        }
        if (!this.currentEditSite.getSearchUrl().equals(site.getSearchUrl())) {
            downloadTorrentSite(site);
        }
        if (ToAppUtils.isEmpty(site.getPageSelector())) {
            return TempExtractResult.builder()
                    .site(site)
                    .result(this.currentDownloadedPage.getElementsString())
                    .build();
        }
        Pair<String, Integer> withCountNum = this.currentDownloadedPage.findWithCountNum(site.getPageSelector());
        if (withCountNum != null && withCountNum.getSecond() > 0 && (ToAppUtils.isNotEmpty(site.getNameSelector()) || ToAppUtils.isNotEmpty(site.getSizeSelector()) || ToAppUtils.isNotEmpty(site.getDateSelector()))) {
            List<TempSiteExtractResult> extractResult = new ArrayList<>();
            this.currentDownloadedPage.find(site.getPageSelector(), (e, i) -> {
                TempSiteExtractResult torrentLinkInfo = new TempSiteExtractResult();
                if (ToAppUtils.isNotEmpty(site.getNameSelector())) {
                    Elements linkElement = e.select(site.getNameSelector());
                    torrentLinkInfo.setTitle(linkElement.text());
                    torrentLinkInfo.setLinkURL(linkElement.select("a").attr("abs:href"));
                }
                if (ToAppUtils.isNotEmpty(site.getSizeSelector())) {
                    torrentLinkInfo.setSize(e.select(site.getSizeSelector()).text());
                }
                if (ToAppUtils.isNotEmpty(site.getDateSelector())) {
                    torrentLinkInfo.setDate(e.select(site.getDateSelector()).text());
                }
                extractResult.add(torrentLinkInfo);
            });
            return TempExtractResult.builder()
                    .extractResultList(extractResult)
                    .site(site)
                    .result(withCountNum.getFirst())
                    .build();
        }
        return TempExtractResult.builder()
                .site(site)
                .result(withCountNum.getFirst())
                .build();
    }
}