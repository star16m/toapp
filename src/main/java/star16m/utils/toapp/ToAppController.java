package star16m.utils.toapp;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import star16m.utils.toapp.keyword.Keyword;
import star16m.utils.toapp.keyword.KeywordRepository;
import star16m.utils.toapp.torrent.ToService;
import star16m.utils.toapp.torrent.Torrent;
import star16m.utils.toapp.torrent.TorrentRepository;
import star16m.utils.toapp.torrent.collector.TorrentCollector;

@Controller
@Slf4j
public class ToAppController {
	@Autowired
	private ToService service;
	@Autowired
	private TorrentRepository torrentRepository;
	@Autowired
	private KeywordRepository keywordRepository;
	@RequestMapping("/")
	public String index(Map<String, Object> model) {
		List<Torrent> torrents = service.selectAll();
		log.debug("current torrents is " + torrents.size() + ".");
		model.put("torrents", torrents);
		// last 2 day
		DateTime dateTime = new DateTime(new Date());
		model.put("torrentLast2days", dateTime.minusDays(1).toString("yyyyMMdd"));
		model.put("torrentLast7days", dateTime.minusDays(6).toString("yyyyMMdd"));
		List<String> last2DaysList = TorrentCollector.getTargetLastDays(2);
		List<String> last7DaysList = TorrentCollector.getTargetLastDays(7);
		Long torrentLast2DaysCount = torrentRepository.countByDateStringIn(last2DaysList);
		Long torrentLast7DaysCount = torrentRepository.countByDateStringIn(last7DaysList);
		model.put("torrentLast2DaysCount", torrentLast2DaysCount);
		model.put("torrentLast7DaysCount", torrentLast7DaysCount);
		
		List<Keyword> kList = keywordRepository.selectLatestkeyword();
		model.put("notDownloadKeyword", kList);
		return "index";
	}
}
