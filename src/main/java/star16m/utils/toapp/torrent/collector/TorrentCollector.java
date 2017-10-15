package star16m.utils.toapp.torrent.collector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import star16m.utils.toapp.commons.CommonService;
import star16m.utils.toapp.commons.MessageRepository;
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
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36";
	private static final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");
	public static DateTime START_DATE_STRING = null;
	public static final List<String> targetDateString = new ArrayList<String>();
	@Autowired
	private SiteRepository siteRepository;
	@Autowired
	private KeywordRepository keywordRepository;
	@Autowired
	private TorrentRepository torrentRepository;
	@Autowired
	private CommonService commonService;
	@Autowired
	private MessageRepository messageRepository;
	
	@Scheduled(initialDelay = 1*60*1000, fixedDelay = 5*60*1000)
	public void collect() {
		List<Site> siteList = siteRepository.findByUseableTrue();
		List<Keyword> keywordList = keywordRepository.findByIgnoreDateFalse();
		
		for (Site site : siteList) {
			for (Keyword keyword : keywordList) {
				try {
					log.info("##### try collect by site[{}], keyword[{}]", site.getName(), keyword.getKeyword());
					String result = collect(site, keyword);
					log.info("##### collected [{}]", result);
					commonService.saveMessage("cron-30", result);
				} catch (IOException e) {
					log.warn("error occured while collect torrent site[{}]", site);
					// do other site.
				}
			}
		}
			
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
	@Scheduled(cron="0 5 12 * * *")
	public void resetTarget() {
		int days = resetTargetDateString();
		log.info("delete torrent files that older than {} days.", days);
		torrentRepository.deleteByDateStringNotIn(targetDateString);
		messageRepository.deleteByCreateDateLessThan(new DateTime(new Date()).minusDays(1).toDate());
	}
	private static int resetTargetDateString() {
		targetDateString.clear();
		int days = 15;
		DateTime endDate = new DateTime(new Date());
		for (int i = 0; i < days; i++) {
			targetDateString.add(endDate.minusDays(i).toString(formatter));
		}
		START_DATE_STRING = endDate.minusDays(days+1);
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
						for (Site site : siteList) {
							try {
								String result = collect(site, keyword);
								commonService.saveMessage("col-key", result);
							} catch (IOException e) {
								log.warn("error occured while collect torrent site[{}]", site);
								// do other site.
							}
						}
					}
				}).start();
			}
		}
	}
	public String collect(Site site, Keyword keyword) throws IOException {
		if (targetDateString.size() <= 0) {
			resetTargetDateString();
		}
		Document doc = null;
		int notInTargetDate = 0;
		int alreadyExists   = 0;
		int success         = 0;
		int totalElementNum = 0;
		int alreadyExistUrl = 0;
		try {
			String url = site.getSearchUrl();
			if (site.getSearchUrl().contains("[KEYWORD]")) {
				if (keyword.getKeyword()!=null && keyword.getKeyword().length() > 0) {
					log.info("replace keyword as [{}]", keyword.getKeyword());
					url = url.replaceAll("\\[KEYWORD\\]", keyword.getKeyword());
					log.info("url is [{}]", url);
				}
			}
			doc = Jsoup.connect(url).userAgent(USER_AGENT).get();
			if (doc != null) {
				Elements elements = doc.select(site.getSelector());
				if (elements == null || elements.size() == 0) {
					return "[" + keyword.getKeyword() + "] [element not found]";
				}
				for (Element e : elements) {
					if (!e.tagName().equalsIgnoreCase("a")) {
						log.info("there is not a tag. [{}]", e.tagName());
						return "[" + keyword.getKeyword() + "] [a tag not found]";
					}
				}
				log.info("try to connect torrent detail page [{}]", elements.size());
				totalElementNum = elements.size();
				for (Element element : elements) {

					String aString = element.text();
					String tmpDateString = replaceGroup(aString, "(\\d{6,8})");
					if (tmpDateString != null && tmpDateString.length() == 6 && tmpDateString.startsWith("1")) {
						tmpDateString = "20" + tmpDateString;
						DateTime tmpDate = new DateTime(formatter.parseDateTime(tmpDateString));
						if (!keyword.isIgnoreDate() && START_DATE_STRING.isAfter(tmpDate)) {
							return "[" + keyword.getKeyword() + "] before that last day[" + START_DATE_STRING.toString(formatter) + "] collect is skipped.";
						}
					}
					String detailUrl = element.attr("abs:href");
					if (torrentRepository.existsByUrl(detailUrl)) {
						log.info("already exists torrent. url[{}]", detailUrl);
						alreadyExistUrl++;
						return "[" + keyword.getKeyword() + "] already exists torrent url [" + detailUrl + "]. collect is skipped.";
					}
					Document itemDoc = null;
					try {
						itemDoc = Jsoup.connect(detailUrl).userAgent(USER_AGENT).get();
						Torrent t = new Torrent();
						t.setUrl(detailUrl);
						String torrentName = null;
						String torrentSize = null;
						String torrentMagnetHash = null;
						if (!StringUtils.isEmpty(site.getTorrentNameSelector())) {
							torrentName = replaceGroup(itemDoc.select(site.getTorrentNameSelector()).text(), site.getTorrentNameReplace());
						}
						if (!StringUtils.isEmpty(site.getTorrentSizeSelector())) {
							torrentSize = replaceGroup(itemDoc.select(site.getTorrentSizeSelector()).text(), site.getTorrentSizeReplace());
						}
						if (!StringUtils.isEmpty(site.getTorrentMagnetHashSelector())) {
							torrentMagnetHash = replaceGroup(itemDoc.select(site.getTorrentMagnetHashSelector()).outerHtml(), site.getTorrentMagnetHashReplace());
						}
						t.setTitle(torrentName.substring(0, Math.min(255, torrentName.length())));
						String dateString = replaceGroup(torrentName, "(\\d{6,8})");
						if (dateString == null || dateString.equals(torrentName) || dateString.length() < 6 || dateString.length() > 8) {
							dateString = "--------";
						} else if (dateString != null && dateString.length() == 6 && dateString.startsWith("1")) {
							dateString = "20" + dateString;
						}
						if (!keyword.isIgnoreDate() && !targetDateString.contains(dateString)) {
							log.warn("{} is not target date[{}]!.", torrentName, dateString);
							notInTargetDate++;
							continue;
						}
//						if (!keyword.isIgnoreDate() && !dateString.equals("--------")) {
//							break;
//						}
						t.setDateString(dateString);
						t.setSize(torrentSize);
						t.setMagnetCode(torrentMagnetHash);
						t.setSiteName(site.getName());
						t.setKeyword(keyword.getKeyword());
						t.setTorrentFindDate(new Date());
						if (torrentRepository.exists(t.getMagnetCode())) {
							log.info("already exists torrent[{}]", t);
							alreadyExists++;
						} else {
							torrentRepository.saveAndFlush(t);
							success++;
						}
					} catch (IOException e) {
					}
				}
			}
		} catch (IOException e) {
			log.error("error occured while collect. site=[{}], keyword=[{}], error=[{}]", site, keyword, e);
			throw e;
		}
		return "Total[" + totalElementNum + "], alreadyExistUrl[" + alreadyExistUrl + ", notInDate[" + notInTargetDate + "], already[" + alreadyExists + "], success[" + success + "]";
	}
	private String replaceGroup(String orgString, String patternString) {
		String replaceString = new String(orgString);
		if (!StringUtils.isEmpty(patternString)) {
			Pattern p = Pattern.compile(patternString);
			Matcher m = p.matcher(replaceString);
			if (m.find()) {
				replaceString = m.group(1);
			}
		}
		log.info("REPLACE {}====={}", orgString, replaceString);
		return replaceString;
	}
}
