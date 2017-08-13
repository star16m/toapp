package star16m.utils.toapp;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import star16m.utils.toapp.commons.Message;
import star16m.utils.toapp.commons.MessageRepository;
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
	private MessageRepository messageRepository;
	@Autowired
	private TorrentRepository torrentRepository;
	@RequestMapping("/")
	public String index(Map<String, Object> model) {
		List<Torrent> torrents = service.selectAll();
		log.debug("current torrents is " + torrents.size() + ".");
		Message m = new Message();
		m.setType("info");
		m.setMessage("current torrents is " + torrents.size() + ".");
		m.setCreateDate(new Date());
		messageRepository.save(m);
		long count = messageRepository.count();
		log.info("Message = {}", count);
		model.put("torrents", torrents);
		model.put("messages", messageRepository.findTop10ByOrderByCreateDateDesc());
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
		return "index";
	}
}
