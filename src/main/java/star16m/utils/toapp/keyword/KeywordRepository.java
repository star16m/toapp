package star16m.utils.toapp.keyword;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
	public Keyword findByKeyword(String keyword);
}
