package star16m.utils.toapp.torrent;

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

import star16m.utils.toapp.commons.Message;
import star16m.utils.toapp.commons.MessageRepository;
import star16m.utils.toapp.site.Site;
import star16m.utils.toapp.site.SiteRepository;

@Controller
@RequestMapping("torrent")
public class TorrentController {
	private static Logger LOG = LoggerFactory.getLogger(TorrentController.class); 

	@Autowired
	private SiteRepository siteRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public String site(Map<String, Object> model) {
		LOG.debug("try findAll torrent site.");
		List<Site> siteList = siteRepository.findAll();
		LOG.debug("successfully findAll torrent site. size:" + siteList.size());
		model.put("sites", siteList);
		return "site";
	}
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> createTorrentSite(@Param("torrentSite") Site torrentSite) {
	
		LOG.debug("try createTorrentsite:::" + torrentSite);
		Site site = siteRepository.save(torrentSite);
		LOG.debug("successfully created torrent site[" + site + "]");
		return new ResponseEntity<>(site, HttpStatus.OK);
	}
}
