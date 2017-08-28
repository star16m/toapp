package star16m.utils.toapp.torrent;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;
import star16m.utils.toapp.keyword.Keyword;
import star16m.utils.toapp.keyword.KeywordRepository;
import star16m.utils.toapp.site.Site;
import star16m.utils.toapp.site.SiteRepository;
import star16m.utils.toapp.torrent.collector.TorrentCollector;

@Controller
@RequestMapping("torrent")
@Slf4j
public class TorrentController {

	@Autowired
	private TorrentRepository torrentRepository;
	@Autowired
	private KeywordRepository keywordRepository;
	@Autowired
	private SiteRepository siteRepository;
	@Autowired
	private TorrentCollector collector;
	
	@GetMapping
	public String torrent(@RequestParam(required=false, defaultValue="-1") Integer lastDays, @RequestParam(required=false, defaultValue = "-1") Integer targetDate, @RequestParam(name="name", required=false) String torrentName, Model model) {
		log.debug("try findAll torrent site.");
		log.info("torrentName ::: " + torrentName);
		
//		Site site = siteRepository.findOne(2L);
//		site.setTorrentNameReplace("토렌트명: (.+?)(시드|Info Hash)");
//		site.setTorrentMagnetHashReplace("Info Hash: ([a-zA-Z0-9]+?)");
//		site.setTorrentMagnetHashReplace("파일크기:([ 0-9\\.]+ [A-Z])");
//		siteRepository.save(site);
//		collector.collect();
		List<Torrent> torrentList = null;
		if (lastDays != null && lastDays > 0) {
			List<String> lastDaysList = TorrentCollector.getTargetLastDays(lastDays);
			torrentList = torrentRepository.findTorrentByDateStringInOrderByDateStringDescKeywordAscTorrentFindDateDesc(lastDaysList);
		} else if (targetDate != null && targetDate > 0) {
			List<String> targetDateList = new ArrayList<String>();
			targetDateList.add(String.valueOf(targetDate));
			torrentList = torrentRepository.findTorrentByDateStringInOrderByDateStringDescKeywordAscTorrentFindDateDesc(targetDateList);
		} else if (torrentName != null && torrentName.length() > 0) {
			log.info("try torrent name [" + torrentName + "]");
			torrentList = torrentRepository.findTorrentByKeywordOrderByDateStringDescKeywordAscTorrentFindDateDesc(torrentName);
		} else {
			torrentList = torrentRepository.findAllTorrentByOrderByDateStringDescKeywordAscTorrentFindDateDesc();
		}
		List<Keyword> keywordList = keywordRepository.findAll();
		log.debug("successfully findAll torrent site. size:" + torrentList.size());
		model.addAttribute("torrents", torrentList);
		model.addAttribute("keywords", keywordList);
		model.addAttribute("currentKeyword", "");
		
		return "torrent";
	}
	@GetMapping("{keyword}")
	public String torrentKeyword(@PathVariable String keyword, Model model) {
		log.debug("try find by keyword torrent site. keyword=[{}]", keyword);
		List<Torrent> torrentList = torrentRepository.findTorrentByKeywordOrderByDateStringDescKeywordAscTorrentFindDateDesc(keyword);
		List<Keyword> keywordList = keywordRepository.findAll();
		log.debug("successfully findAll torrent site. size:" + torrentList.size());
		model.addAttribute("torrents", torrentList);
		model.addAttribute("keywords", keywordList);
		model.addAttribute("currentKeyword", keyword);
		return "torrent";
	}

	@PatchMapping("download/{magnetHash}")
	public String createTorrent(@PathVariable String magnetHash, @RequestParam String currentKeyword, Model model) {
		log.info("Current keyword = {}", currentKeyword);
		log.info("MODEL[{}]" + model);
		log.info("patched torrent ::: " + magnetHash);
		Torrent torrent = torrentRepository.findOne(magnetHash);
		torrent.setDownload(true);
		torrent.setDownloadDate(new Date());
		torrentRepository.save(torrent);
		log.info("target torrent = [{}]", torrent);
		String encodedString = "";
		try {
			encodedString = URLEncoder.encode(currentKeyword, "UTF-8").replaceAll("\\+", "%20");
		} catch (UnsupportedEncodingException e) {
			log.warn(e.getMessage());
		}
		return "redirect:/torrent/" + encodedString;
	}
	@PatchMapping("collect")
	public String collectTorrent(@RequestParam String currentKeyword, Model model) {
		log.info("CALLED collectTorrent{}", currentKeyword);
		collector.collect(currentKeyword);
		String encodedString = "";
		try {
			encodedString = URLEncoder.encode(currentKeyword, "UTF-8").replaceAll("\\+", "%20");
		} catch (UnsupportedEncodingException e) {
			log.warn(e.getMessage());
		}
		return "redirect:/torrent/" + encodedString;
	}
}
