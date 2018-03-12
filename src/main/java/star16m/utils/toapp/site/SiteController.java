package star16m.utils.toapp.site;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import star16m.utils.toapp.commons.TorrentResult;
import star16m.utils.toapp.commons.errors.ToAppException;
import star16m.utils.toapp.commons.page.PageConnector;
import star16m.utils.toapp.commons.utils.ToAppUtils;

@Controller
@RequestMapping("site")
@Slf4j
public class SiteController {
	@Autowired
	private SiteService siteService;
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
	@PatchMapping("check")
	public @ResponseBody TorrentResult<Map<String, String>> checkSite(@RequestBody @Valid Site.Create siteCreate, BindingResult result) {
		TorrentResult<Map<String, String>> torrentResult = new TorrentResult<Map<String, String>>();
		if (result.hasErrors()) {
			log.warn("errors:" + result);
			torrentResult.setSuccess(false);
			torrentResult.setMessage("it's error");
			
		} else {
			try {
				siteService.validationSite(siteCreate);
				String urlString = siteService.getSiteSearchString(siteCreate);
				log.debug("url is [{}]", urlString);
				final Map<String, String> foundResult = new HashMap<String, String>();
				PageConnector pageConnector = new PageConnector(urlString);
				int size = pageConnector.find(siteCreate.getPageSelector(), (e, i) -> {
					foundResult.put("data_" + i, e.attr("abs:href"));
					log.info("found tag [{}]", e);
				});
				if (size > 0) {
					torrentResult.setMessage(String.format("found %d tags.", size));
					torrentResult.setSuccess(true);
					foundResult.put("size", String.valueOf(size));
					// first url string
					String detailPageURL = foundResult.get("data_0");
					foundResult.put("detailPageURL", detailPageURL);
					torrentResult.setData(foundResult);
				} else {
					torrentResult.setMessage(String.format("found %d tags.", size));
				}
			} catch (IOException | ToAppException e) {
				torrentResult.setSuccess(false);
				torrentResult.setMessage(e.getMessage());
			}
		}
		return torrentResult;
	}
	@PatchMapping("detail/check")
	public @ResponseBody TorrentResult<String> checkDetailSite(@RequestBody @Valid Site.SiteCreate siteCreate, BindingResult result) {
		TorrentResult<String> torrentResult = new TorrentResult<>();
		log.info("page : [{}]", siteCreate);
		log.info("result [{}]", result);
		if (result.hasErrors()) {
			log.info("errors:" + result);
			torrentResult.setSuccess(false);
			torrentResult.setMessage("it's error");
		} else {
			try {
				Site.Create siteC = new Site.Create();
				siteC.setSiteName(siteCreate.getSiteName());
				siteC.setPageSelector(siteCreate.getPageSelector());
				siteC.setSiteKeyword(siteCreate.getSiteKeyword());
				siteC.setSiteSearchUrl(siteCreate.getTempDetailURL());
				
				Site.DetailPage detailPage = new Site.DetailPage();
				detailPage.setTorrentNameSelector(siteCreate.getTorrentNameSelector());
				detailPage.setTorrentNameReplace(siteCreate.getTorrentNameReplace());
				detailPage.setTorrentSizeSelector(siteCreate.getTorrentSizeSelector());
				detailPage.setTorrentSizeReplace(siteCreate.getTorrentSizeReplace());
				detailPage.setTorrentMagnetHashSelector(siteCreate.getTorrentMagnetHashSelector());
				detailPage.setTorrentMagnetHashReplace(siteCreate.getTorrentMagnetHashReplace());
				siteService.validationDetailSite(detailPage);
				String urlString = siteService.getSiteSearchString(siteC);
				log.info("detail page url is [{}]", urlString);
				final Map<String, String> foundResult = new HashMap<String, String>();
				PageConnector pageConnector = new PageConnector(urlString);
				String torrentName = pageConnector.find(siteCreate.getTorrentNameSelector());
				String torrentSize = pageConnector.find(siteCreate.getTorrentSizeSelector());
				String torrentMagnet = pageConnector.find(siteCreate.getTorrentMagnetHashSelector());
				log.info("founded torrentName : [{}]", torrentName);
				log.info("founded torrentSize : [{}]", torrentSize);
				log.info("founded torrentMagnet : [{}]", torrentMagnet);
				boolean selectValidation = true;
				selectValidation &= ToAppUtils.isNotEmpty(torrentName);
				selectValidation &= ToAppUtils.isNotEmpty(torrentSize);
				selectValidation &= ToAppUtils.isNotEmpty(torrentMagnet);
				foundResult.put("01.foundTorrentName", ToAppUtils.getString(torrentName, "[NOT FOUND TORRENT NAME]"));
				foundResult.put("02.foundTorrentSize", ToAppUtils.getString(torrentSize, "[NOT FOUND TORRENT SIZE]"));
				foundResult.put("03.foundTorrentMagnet", ToAppUtils.getString(torrentMagnet, "[NOT FOUND TORRENT MAGNET]"));
				if (selectValidation) {
					// founded all item.
					if (!ToAppUtils.isEmpty(siteCreate.getTorrentNameReplace())) {
						String foundTorrentNameReplaced = ToAppUtils.replaceGroup(torrentName, siteCreate.getTorrentNameReplace());
						log.info("foundTorrentNameReplaced : [{}]", foundTorrentNameReplaced);
						foundResult.put("01.foundTorrentName", foundTorrentNameReplaced);
					}
					if (!ToAppUtils.isEmpty(siteCreate.getTorrentSizeReplace())) {
						String foundTorrentSizeReplaced = ToAppUtils.replaceGroup(torrentSize, siteCreate.getTorrentSizeReplace());
						log.info("foundTorrentNameReplaced : [{}]", foundTorrentSizeReplaced);
						foundResult.put("02.foundTorrentSize", foundTorrentSizeReplaced);
					}
					if (!ToAppUtils.isEmpty(siteCreate.getTorrentMagnetHashReplace())) {
						String foundTorrentMagnetReplaced = ToAppUtils.replaceGroup(torrentMagnet, siteCreate.getTorrentMagnetHashReplace());
						log.info("foundTorrentMagnetReplaced : [{}]", foundTorrentMagnetReplaced);
						foundResult.put("03.foundTorrentMagnet", foundTorrentMagnetReplaced);
					}
					torrentResult.setSuccess(true);
					torrentResult.setMessage("found item.");
				}
				torrentResult.setData(foundResult.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(entry -> entry.getKey() + " - " + entry.getValue()).collect(Collectors.joining("\n")));
			} catch (IOException | ToAppException e) {
				torrentResult.setSuccess(false);
				torrentResult.setMessage(e.getMessage());
			}
		}
		return torrentResult;
	}
	@PostMapping
	public @ResponseBody TorrentResult<Site> createTorrentSite(@RequestBody Site.SiteCreate siteCreate, BindingResult result) {
		log.info("try createTorrentsite:::" + siteCreate);
		TorrentResult<Site> torrentResult = new TorrentResult<>();
		if (!result.hasErrors()) {
			log.info("try collect site info.");
			Site site = new Site();
			site.setName(siteCreate.getSiteName());
			site.setSearchUrl(siteCreate.getSiteSearchUrl());
			site.setPageSelector(siteCreate.getPageSelector());
			site.setTorrentNameSelector(siteCreate.getTorrentNameSelector());
			site.setTorrentNameReplace(siteCreate.getTorrentNameReplace());
			site.setTorrentSizeSelector(siteCreate.getTorrentSizeSelector());
			site.setTorrentSizeReplace(siteCreate.getTorrentSizeReplace());
			site.setTorrentMagnetHashSelector(siteCreate.getTorrentMagnetHashSelector());
			site.setTorrentMagnetHashReplace(siteCreate.getTorrentMagnetHashReplace());
			siteRepository.save(site);
			torrentResult.setSuccess(true);
			torrentResult.setMessage("create result");
			torrentResult.setData(site);
			return torrentResult;
		}
		return torrentResult;
	}
}
