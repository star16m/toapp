package star16m.utils.toapp;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import star16m.utils.toapp.commons.errors.ToAppException;
import star16m.utils.toapp.keyword.Keyword;
import star16m.utils.toapp.keyword.KeywordRepository;
import star16m.utils.toapp.site.Site;
import star16m.utils.toapp.site.SiteRepository;
import star16m.utils.toapp.torrent.Torrent;
import star16m.utils.toapp.torrent.TorrentRepository;
import star16m.utils.toapp.torrent.collector.CollectResult;
import star16m.utils.toapp.torrent.collector.TorrentCollector;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Slf4j
@Disabled
public class ToappApplicationTests {

	@Autowired
	private SiteRepository siteRepository;
	@Autowired
	private KeywordRepository keywordRepository;
	@Autowired
	private TorrentCollector torrentCollector;
	@Autowired
	private TorrentRepository torrentRepository;

	private Site getSiteVery() {

		this.siteRepository.deleteAll();
		String targetSearchUrl = "https://torrentvery.com/torrent_ent?bo_table=torrent_ent&sca=%EC%98%88%EB%8A%A5&stx=[KEYWORD]";
		List<Site> allSite = this.siteRepository.findAll();
		for (Site site : allSite) {
			if (site.getSearchUrl().equals(targetSearchUrl)) {
				return site;
			}
		}
		Site site = new Site();
		site.setName("testSite");
		site.setSearchUrl(targetSearchUrl);
		site.setPageSelector("#fboardlist > div:nth-child(10) > table > tbody > tr:has(td.list-subject:not(.pr_subject))");
		site.setNameSelector("td.list-subject a");
		site.setDateSelector("td.td_date2");
		site.setSizeSelector("td.td_size2600");
		site.setTorrentNameSelector("");
		site.setTorrentNameReplace("");
		site.setTorrentSizeSelector("");
		site.setTorrentSizeReplace("");
		site.setTorrentMagnetHashSelector("div a.view_file_download:has(i.fa-magnet)");
		site.setTorrentMagnetHashReplace(".+href=\".+:([^\"]+)\".+");
		site.setUseable(true);

		return this.siteRepository.save(site);
	}
	private Site getSiteQQ() {

		this.siteRepository.deleteAll();
		String targetSearchUrl = "https://torrentqq22.com/search?q=[KEYWORD]&sm=top.s";
		List<Site> allSite = this.siteRepository.findAll();
		for (Site site : allSite) {
			if (site.getSearchUrl().equals(targetSearchUrl)) {
				return site;
			}
		}
		Site site = new Site();
		site.setName("testSite");
		site.setSearchUrl(targetSearchUrl);
		site.setPageSelector("#searchresult > li");
		site.setNameSelector("div.wr-subject a.subject");
		site.setDateSelector("div.wr-size");
		site.setSizeSelector("div.wr-date");
		site.setTorrentNameSelector("");
		site.setTorrentNameReplace("");
		site.setTorrentSizeSelector("");
		site.setTorrentSizeReplace("");
		site.setTorrentMagnetHashSelector("a.btn-magnet");
		site.setTorrentMagnetHashReplace("btih:(.+)'");
		site.setUseable(true);

		return this.siteRepository.save(site);
	}

	private Keyword getKeyword() {
		Keyword keyword = new Keyword();
		keyword.setKeyword("골목");
		Keyword byKeyword = this.keywordRepository.findByKeyword(keyword.getKeyword());
		if (byKeyword == null) {
			return this.keywordRepository.save(keyword);
		}
		return byKeyword;
	}

	@Test
	public void testCollect() throws ToAppException {
		Site site = getSiteQQ();
		Keyword keyword = getKeyword();

		assertThat(site).isNotNull();
		assertThat(keyword).isNotNull();

		List<Torrent> torrentList = this.torrentRepository.findAll();
		torrentList.forEach(t -> {
			log.info("current torrent [{}]", t);
		});
		CollectResult collectResult = this.torrentCollector.collect(site, keyword);
		log.info("successfully collected. [{}]", collectResult);
	}

	@Test
	public void clearTorrent() throws Exception {
		this.torrentRepository.deleteAll();
	}
}
