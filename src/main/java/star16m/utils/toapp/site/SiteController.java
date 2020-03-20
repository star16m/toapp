package star16m.utils.toapp.site;

import java.util.List;
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
import star16m.utils.toapp.commons.utils.ToAppUtils;
import star16m.utils.toapp.torrent.Torrent;

@Controller
@RequestMapping("site")
@Slf4j
public class SiteController {
	@Autowired
	private SiteService siteService;
	@Autowired
	private SiteRepository siteRepository;
//
//	@PatchMapping
//	public ResponseEntity<?> editSiteEnable(@RequestParam("siteId") Long siteId, @RequestParam("enabled") Boolean enabled, Model model) {
//		Site site = siteRepository.findById(siteId);
//		site.setUseable(enabled);
//		siteRepository.save(site);
//		return new ResponseEntity<Site>(site, HttpStatus.OK);
//	}
//	@DeleteMapping("/{siteId}")
//	public ResponseEntity<?> deleteSite(@PathVariable("siteId") Long siteId, Model model) {
//		Site site = siteRepository.findById(siteId);
//		if (site == null) {
//			return ResponseEntity.badRequest().build();
//		}
//		siteRepository.delete(site);
//		return ResponseEntity.noContent().build();
//	}
	@GetMapping
	public String site(@SortDefault(sort="id", direction=Direction.DESC) Sort sort, Model model) {
		log.info("try findAll torrent site.");
		List<Site> siteList = siteRepository.findAll(sort);
		log.info("successfully findAll torrent site. size:" + siteList.size());
		model.addAttribute("sites", siteList);
		model.addAttribute("siteResult", "");
		model.addAttribute("siteDetailResult", "");
		model.addAttribute("siteCreate", new Site.SiteInfo());
		return "site";
	}
	@PatchMapping("check")
	public @ResponseBody TorrentResult<List<Torrent.TorrentLinkInfo>> checkSite(@RequestBody @Valid Site.SiteInfo siteInfo, BindingResult result) {
		TorrentResult<List<Torrent.TorrentLinkInfo>> torrentResult = new TorrentResult<>();
		if (result.hasErrors()) {
			log.warn("errors:" + result);
			torrentResult.setSuccess(false);
			torrentResult.setMessage(result.getFieldErrors().stream().map(f -> f.getField() + " : " + f.getDefaultMessage()).collect(Collectors.joining("\n")));
		} else {
			try {
				log.info("try check site url info [{}]", siteInfo);
				List<Torrent.TorrentLinkInfo> foundLinkResult = siteService.findDetailPageElement(siteInfo);
				log.info("found site result [{}]", foundLinkResult);
				torrentResult.setMessage(String.format("found %d tags.", foundLinkResult.size()));
				torrentResult.setSuccess(true);
				torrentResult.setData(foundLinkResult);
			} catch (ToAppException e) {
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
			site.setNameSelector(siteCreate.getNameSelector());
			site.setSizeSelector(siteCreate.getSizeSelector());
			site.setDateSelector(siteCreate.getDateSelector());
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
