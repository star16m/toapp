package star16m.utils.toapp.torrent;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ToService {
	@Autowired
	private TorrentRepository repository;
	public List<Torrent> selectAll() {
//		Torrent t = new Torrent();
//		t.setTitle("haha");
//		t.setSize("2gb");
//		t.setUrl("http://haha.com");
//		t.setMagnetCode("lhaksjfhlakjsdfhlajksdf");
//		t.setDescription("test Torrent");
//		repository.save(t);
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
