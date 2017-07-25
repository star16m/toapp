package star16m.utils.toapp;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import star16m.utils.message.Message;
import star16m.utils.message.MessageRepository;

@Controller
public class ToController {
	private static Logger LOG = LoggerFactory.getLogger(ToController.class); 
	@Autowired
	private TorrentService service;
	@Autowired
	private TorrentSiteRepository siteRepository;
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
	@RequestMapping(value="/site",method=RequestMethod.GET)
	public String site(Map<String, Object> model) {
		LOG.debug("try findAll torrent site.");
		List<TorrentSite> siteList = siteRepository.findAll();
		LOG.debug("successfully findAll torrent site. size:" + siteList.size());
		model.put("sites", siteList);
		return "site";
	}
	@RequestMapping(value="/site", method=RequestMethod.POST)
	public ResponseEntity<?> createTorrentSite(@Param("torrentSite") TorrentSite torrentSite) {
	
		LOG.debug("try createTorrentsite:::" + torrentSite);
		TorrentSite site = siteRepository.save(torrentSite);
		LOG.debug("successfully created torrent site[" + site + "]");
		return new ResponseEntity<>(site, HttpStatus.OK);
	}
}
