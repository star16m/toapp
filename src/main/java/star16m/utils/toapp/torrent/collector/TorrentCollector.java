package star16m.utils.toapp.torrent.collector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
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
import star16m.utils.toapp.torrent.Torrent;
import star16m.utils.toapp.torrent.TorrentRepository;

@Component
@Transactional
@Slf4j
public class TorrentCollector {
    public static DateTime START_DATE_STRING = null;
    public static final List<String> targetDateString = new ArrayList<String>();
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

    private PageConnector tempDownloadedPage;

    @Scheduled(initialDelay = 1 * 60 * 1000, fixedDelay = 30 * 60 * 1000)
    public void collect() {
        List<Site> siteList = siteRepository.findByUseableTrue();
        List<Keyword> keywordList = keywordRepository.findByIgnoreDateFalse();

        siteList.stream().forEach(site -> {
            keywordList.stream().forEach(keyword -> {
                log.info("##### try collect by site[{}], keyword[{}]", site.getName(), keyword.getKeyword());
                CollectResult result = null;
                try {
                    result = collect(site, keyword);
                    commonService.saveMessage("cron-30", result.getResultString());
                } catch (Exception e) {
                    log.error("error occured while collect torrent file");
                    commonService.saveMessage("cron-30", "[ERROR] " + e.getMessage());
                }
            });
        });
    }

    public static List<String> getTargetLastDays(int lastDays) {
        if (targetDateString.size() <= 0) {
            resetTargetDateString();
        }
        return targetDateString.subList(0, Math.min(lastDays, targetDateString.size()));
    }

    /**
     * 매일
     */
    @Scheduled(initialDelay = 1 * 60 * 1000, fixedDelay = 360 * 60 * 1000)
    public void resetTarget() {
        int days = resetTargetDateString();
        log.info("delete torrent files that older than {} days.", days);
        torrentRepository.deleteByDateStringNotIn(targetDateString);
        messageRepository.deleteByCreateDateLessThan(new DateTime(new Date()).minusHours(6).toDate());
    }

    private static int resetTargetDateString() {
        targetDateString.clear();
        int days = 10;
        DateTime endDate = new DateTime(new Date());
        for (int i = 0; i < days; i++) {
            targetDateString.add(endDate.minusDays(i).toString(ToAppConstants.DATE_TIME_FORMATTER));
        }
        START_DATE_STRING = endDate.minusDays(days + 1);
        return days;
    }

    public void collect(String keywordString) {
        log.info("try to collect by keyword [{}]", keywordString);
        final Keyword keyword = keywordRepository.findByKeyword(keywordString);
        final Keyword searchKeyword = new Keyword(keyword.getId(), keyword.getKeyword(), true);
        log.info("found keyword [{}]", searchKeyword);
        final List<Site> siteList = siteRepository.findByUseableTrue();
        if (siteList != null && siteList.size() > 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    siteList.stream().forEach(site -> {
                        try {
                            CollectResult result = collect(site, searchKeyword);
                            commonService.saveMessage("col-key", result.getResultString());
                        } catch (ToAppException e) {
                            log.warn(e.getMessage());
                        }
                    });
                }
            }).start();
        }
    }

    private CollectResult collect(Site site, Keyword keyword) throws ToAppException {
        if (targetDateString.size() <= 0) {
            resetTargetDateString();
        }
        int alreadyExistsMagnetNum = 0;
        int foundAndSaveTorrentNum = 0;
        int totalElementNum = 0;
        try {
            List<Torrent.TorrentLinkInfo> torrentDetailPageList = findTorrentLinks(site, keyword.getKeyword());
            totalElementNum = torrentDetailPageList.size();
            log.info("try to connect torrent detail page [{}]", torrentDetailPageList);
            for (Torrent.TorrentLinkInfo torrentLinkInfo : torrentDetailPageList) {

                if (!keyword.isIgnoreDate() && torrentLinkInfo.getCreateDate() != null && START_DATE_STRING.isAfter(torrentLinkInfo.getCreateDate())) {
//					return new CollectResult(site, keyword, totalElementNum, foundAndSaveTorrentNum, true, "torrent's date(" + torrentLinkInfo.getCreateDate() + ") is after base line(" + START_DATE_STRING + ")");
                    continue;
                }
                if (torrentRepository.existsByUrl(torrentLinkInfo.getLinkURL())) {
                    // return new CollectResult(site, keyword, totalElementNum, foundAndSaveTorrentNum, true, "already exists torrent(by url).");
                	continue;
                }
                Torrent.TorrentSimpleInfo torrentInfo = null;
                try {
                	torrentInfo = findTorrentInfo(torrentLinkInfo.getLinkURL(), site);
                } catch (ToAppException e) {
                	// ignore exception.
                	continue;
                }
                if (!keyword.isIgnoreDate() && !targetDateString.contains(torrentLinkInfo.getCreateDate().toString(ToAppConstants.DATE_TIME_FORMATTER))) {
                    log.info("torrent[" + torrentInfo.toString() + "] is ignored.");
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
                torrent.setDateString(torrentLinkInfo.getCreateDate().toString(ToAppConstants.DATE_TIME_FORMATTER));
                torrent.setTorrentFindDate(new Date());
                if (torrentRepository.exists(torrent.getMagnetCode())) {
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

    public String getTorrentURL(String torrentURL, String keywordString) {
        return ToAppUtils.replace(torrentURL, ToAppConstants.SEARCH_KEYWORD, keywordString);
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
        } catch (IOException e) {
            throw new ToAppException(e);
        }
        return foundResult;
    }

    public Torrent.TorrentSimpleInfo findTorrentInfo(String torrentURLString, Site site) throws ToAppException {
        PageConnector pageConnector = null;
        try {
            log.info("try to url [{}]", torrentURLString);
            pageConnector = new PageConnector(torrentURLString);
            Torrent.TorrentSimpleInfo torrentPageInfo = new Torrent.TorrentSimpleInfo();
            String torrentName = pageConnector.find(site.getTorrentNameSelector());
            String torrentSize = pageConnector.find(site.getTorrentSizeSelector());
            String torrentMagnet = pageConnector.find(site.getTorrentMagnetHashSelector());
            log.info("founded torrentName[{}], torrentSize[{}], torrentMagnet[{}]", torrentName, torrentSize, torrentMagnet);
            if (ToAppUtils.isNotEmpty(torrentName) && ToAppUtils.isNotEmpty(site.getTorrentNameReplace())) {
                torrentName = ToAppUtils.replaceGroup(torrentName, site.getTorrentNameReplace());
                log.info("replaced torrentName[{}]", torrentName);
            }
            if (ToAppUtils.isNotEmpty(torrentSize) && ToAppUtils.isNotEmpty(site.getTorrentSizeReplace())) {
                torrentSize = ToAppUtils.replaceGroup(torrentSize, site.getTorrentSizeReplace());
                log.info("replaced torrentSize[{}]", torrentSize);
            }
            if (ToAppUtils.isNotEmpty(torrentMagnet) && ToAppUtils.isNotEmpty(site.getTorrentMagnetHashReplace())) {
                torrentMagnet = ToAppUtils.replaceGroup(torrentMagnet, site.getTorrentMagnetHashReplace());
                log.info("replaced torrentMagnet[{}]", torrentMagnet);
            }
            torrentPageInfo.setTorrentName(torrentName);
            torrentPageInfo.setTorrentSize(torrentSize);
            torrentPageInfo.setTorrentMagnet(torrentMagnet);
            return torrentPageInfo;
        } catch (IOException e) {
            throw new ToAppException(e);
        }
    }

    public void clearDownloadedTorrentSite() {
        this.tempDownloadedPage = null;
    }
    public String downloadTorrentSite(String url, String keyword) throws IOException {
        PageConnector pageConnector = new PageConnector(this.getTorrentURL(url, keyword));
        this.tempDownloadedPage = pageConnector;
        return this.tempDownloadedPage.getElementsString();
    }
    public String findTempSite(String cssQuery) {
        if (ToAppUtils.isEmpty(cssQuery)) {
            return this.tempDownloadedPage.getElementsString();
        }
        return this.tempDownloadedPage.find(cssQuery);
    }
}