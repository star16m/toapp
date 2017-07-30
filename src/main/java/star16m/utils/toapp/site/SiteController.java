package star16m.utils.toapp.site;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("site")
public class SiteController {
	private static Logger LOG = LoggerFactory.getLogger(SiteController.class); 

	@Autowired
	private SiteRepository siteRepository;
	
	@GetMapping
	public String site(Map<String, Object> model) {
		LOG.debug("try findAll torrent site.");
		List<Site> siteList = siteRepository.findAll();
		LOG.debug("successfully findAll torrent site. size:" + siteList.size());
		model.put("sites", siteList);
		model.put("sitecreate", new Site.Create());
		return "site";
	}
	@PostMapping
	public String createTorrentSite(@ModelAttribute @Valid Site.Create siteCreate, BindingResult result, Model model) {
	
		LOG.info("try createTorrentsite:::" + siteCreate);
		LOG.info("binding result:" + result);
		LOG.info("model:" + model);
		model.addAttribute("sitecreate", siteCreate);
		if (!result.hasErrors()) {
			LOG.info("try collect site info.");
		}
//		Site site = siteRepository.save(torrentSite);
//		LOG.debug("successfully created torrent site[" + site + "]");
		//model.put("site.create", siteCreate);
		return "site";
	}
}