package star16m.utils.toapp.site;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteRepository extends JpaRepository<Site, Long> {

	List<Site> findByUseableTrue();
	List<Site> findByOrderByIdAsc();
}
