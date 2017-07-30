package star16m.utils.toapp;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import star16m.utils.toapp.commons.Message;
import star16m.utils.toapp.commons.MessageRepository;
import star16m.utils.toapp.torrent.ToService;
import star16m.utils.toapp.torrent.Torrent;

@Controller
@Slf4j
public class ToAppController {
	@Autowired
	private ToService service;
	@Autowired
	private MessageRepository messageRepository;
	@RequestMapping("/")
	public String index(Map<String, Object> model) {
		List<Torrent> torrents = service.selectAll();
		log.debug("current torrents is " + torrents.size() + ".");
		Message m = new Message();
		m.setType("info");
		m.setMessage("current torrents is " + torrents.size() + ".");
		m.setCreateDate(new Date());
		messageRepository.save(m);
		model.put("torrents", torrents);
		model.put("messages", messageRepository.findTop10ByOrderByCreateDateDesc());
		return "index";
	}
}
