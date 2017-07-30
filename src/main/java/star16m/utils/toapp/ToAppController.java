package star16m.utils.toapp;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import star16m.utils.toapp.commons.Message;
import star16m.utils.toapp.commons.MessageRepository;
import star16m.utils.toapp.torrent.ToService;
import star16m.utils.toapp.torrent.Torrent;

@Controller
public class ToAppController {
	private static Logger LOG = LoggerFactory.getLogger(ToAppController.class);
	@Autowired
	private ToService service;
	@Autowired
	private MessageRepository messageRepository;
	@RequestMapping("/")
	public String index(Map<String, Object> model) {
		List<Torrent> torrents = service.selectAll();
		LOG.debug("current torrents is " + torrents.size() + ".");
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
