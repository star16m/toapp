package star16m.utils.toapp.site;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import star16m.utils.toapp.commons.errors.ToAppException;
import star16m.utils.toapp.torrent.Torrent;
import star16m.utils.toapp.torrent.collector.TorrentCollector;

@Service
@Transactional
public class SiteService {

	@Autowired
	private TorrentCollector torrentCollector;
	@Autowired
	private SiteRepository siteRepository;

	public List<Torrent.TorrentLinkInfo> findDetailPageElement(Site.SiteInfo siteInfo) throws ToAppException {
		if (siteInfo == null || siteInfo.getSiteSearchUrl() == null || siteInfo.getSiteKeyword() == null || siteInfo.getPageSelector() == null) {
			throw new ToAppException("Not found site info.");
		}
		Site site = new Site();
		try {
			site.setSearchUrl(this.torrentCollector.getTorrentURL(siteInfo.getSiteSearchUrl(), siteInfo.getSiteKeyword()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new ToAppException("UnsupportedEncode exception", e);
		}
		site.setPageSelector(siteInfo.getPageSelector());
		site.setNameSelector(siteInfo.getNameSelector());
		site.setSizeSelector(siteInfo.getSizeSelector());
		site.setDateSelector(siteInfo.getDateSelector());
		List<Torrent.TorrentLinkInfo> foundResult = this.torrentCollector.findTorrentLinks(site, siteInfo.getSiteKeyword());
		return foundResult;
	}
	public Site getSiteById(Long siteId) {
		return this.siteRepository.findById(siteId).orElse(null);
	}
	public List<Site> findAll() {
		return this.siteRepository.findByOrderByIdAsc();
	}

    public Site createTempSite(Site site) {
		return this.siteRepository.save(site);
    }

	public void deleteTempSite(Long siteId) {
		this.siteRepository.deleteById(siteId);
	}

	public Site save(Site realSite) {
		return this.siteRepository.save(realSite);
	}

	public Site copySiteBySiteId(Long siteId) {
		Site site = this.siteRepository.findById(siteId).orElse(null);
		if (site == null) {
			return null;
		}
		Site newSite = new Site();
		BeanUtils.copyProperties(site, newSite);
		newSite.setId(null);
		newSite.setName(newSite.getName() + "_copy");
		newSite.setUseable(false);
		return this.siteRepository.save(newSite);
	}
}