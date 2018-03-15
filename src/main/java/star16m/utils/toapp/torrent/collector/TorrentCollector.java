package star16m.utils.toapp.torrent.collector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
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
				} catch (Exception e) {
					log.error("error occured while collect torrent file");
				}
				commonService.saveMessage("cron-30", result.getResultString());
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
		log.info("found keyword [{}]", keyword);
		if (keyword != null) {
			final List<Site> siteList = siteRepository.findByUseableTrue();
			if (siteList != null && siteList.size() > 0) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						siteList.stream().forEach(site->{
							try {
								CollectResult result = collect(site, keyword);
								commonService.saveMessage("col-key", result.getResultString());
							} catch (ToAppException e) {
								log.warn(e.getMessage());
							}
						});
					}
				}).start();
			}
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
			String url = getTorrentURL(site.getSearchUrl(), keyword.getKeyword());
			List<Torrent.TorrentLinkInfo> torrentDetailPageList = findTorrentLinks(url, site.getPageSelector());
			totalElementNum = torrentDetailPageList.size();
			log.info("try to connect torrent detail page [{}]", torrentDetailPageList);
			for (Torrent.TorrentLinkInfo torrentLinkInfo : torrentDetailPageList) {

				if (torrentLinkInfo.getCreateDate() != null && START_DATE_STRING.isAfter(torrentLinkInfo.getCreateDate())) {
					return new CollectResult(site, keyword, totalElementNum, foundAndSaveTorrentNum, true, "torrent's date is after base line");
				}
				if (torrentRepository.existsByUrl(torrentLinkInfo.getLinkURL())) {
					return new CollectResult(site, keyword, totalElementNum, foundAndSaveTorrentNum, true, "already exists torrent(by url).");
				}
				Torrent.TorrentSimpleInfo torrentInfo = findTorrentInfo(torrentLinkInfo.getLinkURL(), site);
				if (torrentLinkInfo.getCreateDate() != null && !keyword.isIgnoreDate() && !targetDateString.contains(torrentLinkInfo.getCreateDate().toString(ToAppConstants.DATE_TIME_FORMATTER))) {
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
	public List<Torrent.TorrentLinkInfo> findTorrentLinks(String urlString, String torrentDetailPageSelector) throws ToAppException {
		List<Torrent.TorrentLinkInfo> foundResult = new ArrayList<>();
		PageConnector pageConnector = null;
		try {
			pageConnector = new PageConnector(urlString);
			pageConnector.find(torrentDetailPageSelector, (e, i) -> {
				Torrent.TorrentLinkInfo link = new Torrent.TorrentLinkInfo();
				link.setTitle(e.text());
				link.setLinkURL(e.attr("abs:href"));
				if (ToAppUtils.isNotEmpty(link.getTitle())) {
					String tmpDateString = ToAppUtils.replaceGroup(link.getTitle(), "(\\d{6,8})");
					if (tmpDateString != null && tmpDateString.length() == 6 && tmpDateString.startsWith("1")) {
						tmpDateString = "20" + tmpDateString;
						DateTime tmpDate = new DateTime(ToAppConstants.DATE_TIME_FORMATTER.parseDateTime(tmpDateString));
						link.setCreateDate(tmpDate);
					}
				}
				foundResult.add(link);
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
}