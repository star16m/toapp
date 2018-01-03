package star16m.utils.toapp.site;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteRepository extends JpaRepository<Site, Long> {

	public Site findById(Long siteId);
	public List<Site> findByUseableTrue();	
	public List<Site> findByUseable(boolean useable);	
}
