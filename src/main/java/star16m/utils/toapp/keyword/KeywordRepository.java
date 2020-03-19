package star16m.utils.toapp.keyword;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
	Keyword findByKeyword(String keyword);
	
	@Query(value="select k.id, k.keyword from keyword k where exists (select 1 from torrent t where t.keyword = k.keyword and t.torrent_find_date > sysdate - 2 and not exists (select 1 from torrent t2 where torrent_find_date > sysdate - 7 and keyword = t.keyword and date_string = t.date_string and download_date is not null))", nativeQuery=true)
	List<Keyword> selectLatestkeyword();
}
