package star16m.utils.toapp.keyword;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
	public Keyword findByKeyword(String keyword);
	
	@Query(value="select k.id, k.keyword, k.ignore_date from keyword k where ignore_date = 0 and exists (select 1 from torrent t where t.keyword = k.keyword and t.torrent_find_date > sysdate - 2 and not exists (select 1 from torrent t2 where torrent_find_date > sysdate - 7 and keyword = t.keyword and date_string = t.date_string and download_date is not null))", nativeQuery=true)
	public List<Keyword> selectLatestkeyword();
	
	public List<Keyword> findByIgnoreDateFalse();
}
