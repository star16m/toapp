package star16m.utils.toapp.torrent;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TorrentService {
	@Autowired
	private TorrentRepository repository;
	public List<Torrent> selectAll() {
		return repository.findAllTorrentByOrderByDateStringDescKeywordAscTorrentFindDateDesc();
	}
	public List<Torrent> selectByKeywords(List<String> keywordList) {
		return this.repository.findAllTorrentByKeywordInOrderByDateStringDescKeywordAscTorrentFindDateDesc(keywordList);
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
}
