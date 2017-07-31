package star16m.utils.toapp.torrent;

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

import lombok.extern.slf4j.Slf4j;
import star16m.utils.toapp.site.Site;
import star16m.utils.toapp.site.SiteController;
import star16m.utils.toapp.site.SiteRepository;

@Controller
@RequestMapping("torrent")
@Slf4j
public class TorrentController {

	@Autowired
	private SiteRepository siteRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public String site(Map<String, Object> model) {
		log.debug("try findAll torrent site.");
		List<Site> siteList = siteRepository.findAll();
		log.debug("successfully findAll torrent site. size:" + siteList.size());
		model.put("sites", siteList);
		return "site";
	}
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> createTorrentSite(@Param("torrentSite") Site torrentSite) {
	
		log.debug("try createTorrentsite:::" + torrentSite);
		Site site = siteRepository.save(torrentSite);
		log.debug("successfully created torrent site[" + site + "]");
		return new ResponseEntity<>(site, HttpStatus.OK);
	}
}
