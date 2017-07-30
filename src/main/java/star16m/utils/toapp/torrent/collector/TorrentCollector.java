package star16m.utils.toapp.torrent.collector;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import star16m.utils.toapp.torrent.TorrentRepository;

@Component
@Transactional
public class TorrentCollector {

	private static final Logger LOG = LoggerFactory.getLogger(TorrentCollector.class);
	@Autowired
	private TorrentRepository repository;
	@Scheduled(cron="*/5 * * * * *")
	public void collect() {
//		LOG.info("count is " + repository.count());
	}
}
