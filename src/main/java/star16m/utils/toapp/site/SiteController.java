package star16m.utils.toapp.site;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import star16m.utils.toapp.commons.ToAppUtils;

@Controller
@RequestMapping("site")
@Slf4j
public class SiteController {
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36";
	@Autowired
	private SiteRepository siteRepository;
	
	@PatchMapping
	public ResponseEntity<?> editSiteEnable(@RequestParam("siteId") Long siteId, @RequestParam("enabled") Boolean enabled, Model model) {
		Site site = siteRepository.findById(siteId);
		site.setUseable(enabled);
		siteRepository.save(site);
		return new ResponseEntity<Site>(site, HttpStatus.OK);
	}
	@DeleteMapping("/{siteId}")
	public ResponseEntity<?> deleteSite(@PathVariable("siteId") Long siteId, Model model) {
		Site site = siteRepository.findById(siteId);
		if (site == null) {
			return ResponseEntity.badRequest().build();
		}
		siteRepository.delete(site);
		return ResponseEntity.noContent().build();
	}
	@GetMapping
	public String site(@SortDefault(sort="id", direction=Direction.DESC) Sort sort, Model model) {
		log.info("try findAll torrent site.");
		List<Site> siteList = siteRepository.findAll(sort);
		log.info("successfully findAll torrent site. size:" + siteList.size());
		model.addAttribute("sites", siteList);
		model.addAttribute("siteResult", "");
		model.addAttribute("siteDetailResult", "");
		model.addAttribute("siteCreate", new Site.Create());
		return "site";
	}
	@PostMapping
	public String createTorrentSite(@ModelAttribute("siteCreate") @Valid Site.Create siteCreate, BindingResult result, @SortDefault(sort="id", direction=Direction.DESC) Sort sort, Model model) {
	
		log.info("try createTorrentsite:::" + siteCreate);
		log.info("binding result:" + result);
		log.info("model:" + model);
		if (!result.hasErrors()) {
			log.info("try collect site info.");
			Document doc = null;
			try {
				String url = siteCreate.getSiteSearchUrl();
				if (siteCreate.getSiteSearchUrl().contains("[KEYWORD]")) {
					if (siteCreate.getSiteKeyword()!=null && siteCreate.getSiteKeyword().length() > 0) {
						log.info("replace keyword as [{}]", siteCreate.getSiteKeyword());
						url = url.replaceAll("\\[KEYWORD\\]", siteCreate.getSiteKeyword());
						log.info("url is [{}]", url);
					} else {
						log.info("keyword [KEYWORD] is not replaced.");
						result.rejectValue("siteKeyword", null, "[KEYWORD] 를 입력해 주세요.");
						return "site";
					}
				}
				doc = Jsoup.connect(url).userAgent(USER_AGENT).get();
				if (doc != null) {
					Elements elements = doc.select(siteCreate.getSiteSelector());
					log.info("list elements {}", elements.toString());
					if (elements == null || elements.size() == 0) {
						model.addAttribute("siteResult", "there is no result");
						return "site";
					}
					for (Element e : elements) {
						if (!e.tagName().equalsIgnoreCase("a")) {
							log.info("there is not a tag. [{}]", e.tagName());
							result.rejectValue("siteSelector", null, "a tag 만 조회 가능합니다.");
							return "site";
						}
					}
					model.addAttribute("siteResult", elements.toString());
					log.info("try to connect torrent detail page [{}]", elements.get(0));
					Element element = elements.get(0);
					Document itemDoc = Jsoup.connect(element.attr("abs:href")).userAgent(USER_AGENT).get();
					log.info("DETAIL PAGE {}", itemDoc);
					model.addAttribute("siteDetailResult", itemDoc);
					if (!StringUtils.isEmpty(siteCreate.getTorrentNameSelector())) {
						model.addAttribute("torrentNameResult", ToAppUtils.replaceGroup(itemDoc.select(siteCreate.getTorrentNameSelector()).text(), siteCreate.getTorrentNameReplace()));
					}
					if (!StringUtils.isEmpty(siteCreate.getTorrentSizeSelector())) {
						model.addAttribute("torrentSizeResult", ToAppUtils.replaceGroup(itemDoc.select(siteCreate.getTorrentSizeSelector()).text(), siteCreate.getTorrentSizeReplace()));
					}
					if (!StringUtils.isEmpty(siteCreate.getTorrentMagnetHashSelector())) {
						model.addAttribute("torrentMagnetHashResult", ToAppUtils.replaceGroup(itemDoc.select(siteCreate.getTorrentMagnetHashSelector()).outerHtml(), siteCreate.getTorrentMagnetHashReplace()));
					}
				}
			} catch (IOException e) {
				model.addAttribute("site.result.error", e.getMessage());
			}
		}
		if (!result.hasErrors()) {
			if (model.containsAttribute("siteDetailResult") && model.containsAttribute("torrentNameResult") && model.containsAttribute("torrentSizeResult") && model.containsAttribute("torrentMagnetHashResult")) {
				Site site = new Site();
				site.setName(siteCreate.getSiteName());
				site.setSearchUrl(siteCreate.getSiteSearchUrl());
				site.setSelector(siteCreate.getSiteSelector());
				site.setTorrentNameSelector(siteCreate.getTorrentNameSelector());
				site.setTorrentNameReplace(siteCreate.getTorrentNameReplace());
				site.setTorrentSizeSelector(siteCreate.getTorrentSizeSelector());
				site.setTorrentSizeReplace(siteCreate.getTorrentSizeReplace());
				site.setTorrentMagnetHashSelector(siteCreate.getTorrentMagnetHashSelector());
				site.setTorrentMagnetHashReplace(siteCreate.getTorrentMagnetHashReplace());
				siteRepository.save(site);
				return site(sort, model);
			}
		}
		return "site";
	}
}
