package star16m.utils.toapp.site;

import java.util.List;

import javax.transaction.Transactional;

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

	public List<Torrent.TorrentLinkInfo> findDetailPageElement(Site.SiteInfo siteInfo) throws ToAppException {
		if (siteInfo == null || siteInfo.getSiteSearchUrl() == null || siteInfo.getSiteKeyword() == null || siteInfo.getPageSelector() == null) {
			throw new ToAppException("Not found site info.");
		}
		Site site = new Site();
		site.setSearchUrl(this.torrentCollector.getTorrentURL(siteInfo.getSiteSearchUrl(), siteInfo.getSiteKeyword()));
		site.setPageSelector(siteInfo.getPageSelector());
		site.setNameSelector(siteInfo.getNameSelector());
		site.setSizeSelector(siteInfo.getSizeSelector());
		site.setDateSelector(siteInfo.getDateSelector());
		List<Torrent.TorrentLinkInfo> foundResult = this.torrentCollector.findTorrentLinks(site, siteInfo.getSiteKeyword());
		return foundResult;
	}
	public Torrent.TorrentSimpleInfo findTorrentDetailPageInfo(Site.SiteCreate siteCreate) throws ToAppException {
		String urlString = this.torrentCollector.getTorrentURL(siteCreate.getTorrentDetailPageURL(), siteCreate.getSiteKeyword());
		Site tmpSite = new Site();
		tmpSite.setTorrentNameSelector(siteCreate.getTorrentNameSelector());
		tmpSite.setTorrentNameReplace(siteCreate.getTorrentNameReplace());
		tmpSite.setTorrentSizeSelector(siteCreate.getTorrentSizeSelector());
		tmpSite.setTorrentSizeReplace(siteCreate.getTorrentSizeReplace());
		tmpSite.setTorrentMagnetHashSelector(siteCreate.getTorrentMagnetHashSelector());
		tmpSite.setTorrentMagnetHashReplace(siteCreate.getTorrentMagnetHashReplace());
		
		Torrent.TorrentSimpleInfo torrentInfo = this.torrentCollector.findTorrentInfo(urlString, tmpSite);
		return torrentInfo;
	}
}