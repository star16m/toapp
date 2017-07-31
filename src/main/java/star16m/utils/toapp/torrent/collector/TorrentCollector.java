package star16m.utils.toapp.torrent.collector;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import star16m.utils.toapp.torrent.TorrentRepository;

@Component
@Transactional
@Slf4j
public class TorrentCollector {

	@Autowired
	private TorrentRepository repository;
	@Scheduled(cron="*/5 * * * * *")
	public void collect() {
//		log.info("count is " + repository.count());
	}
}
