package star16m.utils.toapp.site;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("site")
@Slf4j
public class SiteController {

	@Autowired
	private SiteRepository siteRepository;
	
	@GetMapping
	public String site(Map<String, Object> model) {
		log.debug("try findAll torrent site.");
		List<Site> siteList = siteRepository.findAll();
		log.debug("successfully findAll torrent site. size:" + siteList.size());
		model.put("sites", siteList);
		model.put("sitecreate", new Site.Create());
		return "site";
	}
	@PostMapping
	public String createTorrentSite(@ModelAttribute @Valid Site.Create siteCreate, BindingResult result, Model model) {
	
		log.info("try createTorrentsite:::" + siteCreate);
		log.info("binding result:" + result);
		log.info("model:" + model);
		model.addAttribute("sitecreate", siteCreate);
		if (!result.hasErrors()) {
			log.info("try collect site info.");
		}
//		Site site = siteRepository.save(torrentSite);
//		log.debug("successfully created torrent site[" + site + "]");
		//model.put("site.create", siteCreate);
		return "site";
	}
}