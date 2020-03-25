package star16m.utils.toapp.site;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import star16m.utils.toapp.torrent.collector.TorrentCollector;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class SiteService {

    @Autowired
    private TorrentCollector torrentCollector;
    @Autowired
    private SiteRepository siteRepository;

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