package star16m.utils.toapp.torrent;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import star16m.utils.toapp.api.SimpleMap;

@Service
@Transactional
public class TorrentService {
	@Autowired
	private TorrentRepository repository;
	public List<Torrent> selectAll() {
		return repository.findAllTorrentByOrderByDateStringDescKeywordAscTorrentFindDateDesc();
	}
	public Long count() {
		return repository.count();
	}
	public List<Torrent> selectByKeywords(String keyword) {
		return this.repository.findAllTorrentByKeywordOrderByDateStringDescKeywordAscTorrentFindDateDesc(keyword);
	}
	public Torrent create(Torrent torrent) {
		return repository.save(torrent);
	}
	public Torrent update(Torrent torrent) {
		return repository.save(torrent);
	}
	public void delete(Torrent torrent) {
		repository.delete(torrent);
	}
	public Torrent findByMagnetHashCode(String magnetHashCode) {
		return this.repository.findByMagnetCode(magnetHashCode);
	}

	public Torrent download(String magnetHashCode) {
		Torrent torrent = this.repository.findByMagnetCode(magnetHashCode);
		torrent.setDownload(true);
		torrent.setDownloadDate(LocalDate.now());
		return this.repository.save(torrent);
	}

	public List<Map<String, Long>> groupByKeyword() {
		return this.repository.groupByKeyword();
	}

	public List<Torrent> selectByTop(Long top) {
		return this.repository.findTopOrderByDateStringDesc(top);
	}

	public List<Torrent> selectByLastDays(Long last) {
		return this.repository.findLastDaysOrderByDateStringDesc(last);
	}

    public void deleteByKeyword(String keyword) {
		this.repository.deleteByKeyword(keyword);
    }
}
