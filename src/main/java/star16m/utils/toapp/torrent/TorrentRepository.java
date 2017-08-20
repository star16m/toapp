package star16m.utils.toapp.torrent;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface TorrentRepository extends JpaRepository<Torrent, String>{
	public List<Torrent> findAllTorrentByOrderByDateStringDescUrlDesc();
	@Modifying
	@Transactional
	public void deleteByKeyword(String keyword);
	public List<Torrent> findTorrentByKeywordOrderByDateStringDescUrlDesc(String keyword);
	@Transactional
	public boolean existsByUrl(String detailUrl);
	public List<Torrent> findTorrentByDownload(boolean download);
	public List<Torrent> findTorrentByDateStringInOrderByDateStringDescUrlDesc(List<String> targetDateStringList);
	@Transactional
	public void deleteByDateStringNotIn(List<String> targetDateStringList);
	
	public Long countByDateStringIn(List<String> targetDateStringList);
}
