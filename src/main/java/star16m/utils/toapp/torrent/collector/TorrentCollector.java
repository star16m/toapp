package star16m.utils.toapp.torrent.collector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
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
	@Autowired
	private SiteRepository siteRepository;
	@Autowired
	private KeywordRepository keywordRepository;
	@Autowired
	private TorrentRepository torrentRepository;
	@Scheduled(cron="*/10 * * * * *")
	public void collect() {
		if (true) {
			return;
//			log.info("disable scheduled");
		}
		List<Site> siteList = siteRepository.findAll();
		List<Keyword> keywordList = keywordRepository.findAll();
		boolean collected = false;
		for (Site site : siteList) {
			for (Keyword keyword : keywordList) {
				try {
					collect(site, keyword);
					collected = true;
				} catch (IOException e) {
					log.warn("error occured while collect torrent site[{}]", site);
					// do other site.
				}
			}
			if (collected) {
				break;
			}
		}
			
	}
	public void collect(String keywordString) {
		log.info("try to collect by keyword [{}]", keywordString);
		final Keyword keyword = keywordRepository.findByKeyword(keywordString);
		log.info("found keyword [{}]", keyword);
		if (keyword != null) {
			final List<Site> siteList = siteRepository.findAll();
			if (siteList != null && siteList.size() > 0) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						boolean collected = false;
						for (Site site : siteList) {
							try {
								collect(site, keyword);
								collected = true;
							} catch (IOException e) {
								log.warn("error occured while collect torrent site[{}]", site);
								// do other site.
							}
							if (collected) {
								break;
							}
						}
					}
				}).start();
			}
		}
	}
	public void collect(Site site, Keyword keyword) throws IOException {
		Document doc = null;
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
					return;
				}
				for (Element e : elements) {
					if (!e.tagName().equalsIgnoreCase("a")) {
						log.info("there is not a tag. [{}]", e.tagName());
						return;
					}
				}
				log.info("try to connect torrent detail page [{}]", elements.size());
				for (Element element : elements) {
					String detailUrl = element.attr("abs:href");
					if (torrentRepository.existsByUrl(detailUrl)) {
						log.info("already exists torrent. url[{}]", detailUrl);
						continue;
					}
					Document itemDoc = Jsoup.connect(detailUrl).userAgent(USER_AGENT).get();
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
					t.setTitle(torrentName);
					String dateString = replaceGroup(torrentName, "(\\d{6,8})");
					if (dateString == null || dateString.equals(torrentName) || dateString.length() < 6 || dateString.length() > 8) {
						dateString = "------";
					}
					t.setDateString(dateString);
					t.setSize(torrentSize);
					t.setMagnetCode(torrentMagnetHash);
					t.setSiteName(site.getName());
					t.setKeyword(keyword.getKeyword());
					if (torrentRepository.exists(t.getMagnetCode())) {
						log.info("already exists torrent[{}]", t);
					} else {
						torrentRepository.saveAndFlush(t);
					}
				}
			}
		} catch (IOException e) {
			log.error("error occured while collect. site=[{}], keyword=[{}], error=[{}]", site, keyword, e);
			throw e;
		}
		return;
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
