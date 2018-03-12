package star16m.utils.toapp.site;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import star16m.utils.toapp.commons.errors.ToAppException;
import star16m.utils.toapp.commons.utils.ToAppUtils;

@Service
@Transactional
public class SiteService {

	public static final String KEYWORD = "[KEYWORD]";

	public void validationSite(Site.Create siteInfo) throws ToAppException {
		if (siteInfo == null || siteInfo.getSiteSearchUrl() == null || siteInfo.getSiteKeyword() == null || siteInfo.getPageSelector() == null) {
			throw new ToAppException("Not found site info.");
		}
		if (ToAppUtils.isEmpty(siteInfo.getSiteKeyword()) || !siteInfo.getSiteSearchUrl().contains(KEYWORD)) {
			throw new ToAppException("site URL must be has " + KEYWORD);
		}
	}
	public void validationDetailSite(Site.DetailPage pageInfo) throws ToAppException {
		if (pageInfo == null || ToAppUtils.isEmpty(pageInfo.getTorrentNameSelector()) || ToAppUtils.isEmpty(pageInfo.getTorrentSizeSelector()) || ToAppUtils.isEmpty(pageInfo.getTorrentMagnetHashSelector())) {
			throw new ToAppException("Not found site info.");
		}
	}
	
	public String getSiteSearchString(Site.Create siteInfo) {
		return ToAppUtils.replace(siteInfo.getSiteSearchUrl(), SiteService.KEYWORD, siteInfo.getSiteKeyword());
	}
}
