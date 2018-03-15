package star16m.utils.toapp.torrent;

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
		return repository.findAll();
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
}
