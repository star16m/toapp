package star16m.utils.toapp.feed;

import java.nio.charset.Charset;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import com.rometools.rome.feed.atom.Content;
import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Description;
import com.rometools.rome.feed.rss.Guid;
import com.rometools.rome.feed.rss.Item;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.util.UriUtils;
import star16m.utils.toapp.torrent.Torrent;
import star16m.utils.toapp.torrent.TorrentRepository;

import static java.util.stream.Collectors.toList;

@Component
@Getter
@Setter
public class TorrentFeedView extends AbstractRssFeedView {

	@Autowired
	private TorrentRepository torrentRepository;

	public TorrentFeedView() {
		setContentType("application/rss+xml");
	}

	protected Channel newFeed() {
		Channel channel = new Channel("rss_2.0");
		channel.setLink("/feed/");
		channel.setTitle("cutom feed");
		channel.setDescription("custom feed");
		return channel;
	}

	protected List<Item> buildFeedItems(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<Torrent> torrentList = torrentRepository.findTorrentByDownload(true);
		return torrentList.stream().map(e->this.createItem(e)).collect(toList());
	}

	private Item createItem(Torrent torrent) {
		Item item = new Item();
		item.setGuid(createGuid(torrent));
		String title = UriUtils.encode(torrent.getTitle().trim(), Charset.forName("UTF-8"));
		item.setLink("magnet:?xt=urn:btih:" + torrent.getMagnetCode().trim()+ "&dn=" + title
				+ "&tr=udp://tracker.openbittorrent.com:80&tr=http://megapeer.org:6969/announce&tr=http://mgtracker.org:2710/announce&tr=http://tracker.files.fm:6969/announce&tr=http://tracker.flashtorrents.org:6969/announce&tr=http://tracker.mg64.net:6881/announce&tr=http://tracker.nwps.ws:6969/announce&tr=http://tracker.ohys.net/announce&tr=http://tracker.tfile.me/announce&tr=udp://9.rarbg.com:2710/announce&tr=udp://9.rarbg.me:2710/announce&tr=udp://coppersurfer.tk:6969/announce&tr=udp://tracker.coppersurfer.tk:6969&tr=udp://tracker.leechers-paradise.org:6969&tr=udp://exodus.desync.com:6969/announce&tr=udp://open.coppersurfer.com:1337/announce");
		item.setTitle(torrent.getTitle());
		item.setDescription(createDescription(torrent));
		item.setPubDate(Date.from(torrent.getDownloadDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		return item;
	}

	private Description createDescription(Torrent torrent) {
		Description description = new Description();
		description.setType(Content.HTML);
		description.setValue(torrent.getTitle());
		return description;
	}

	private Guid createGuid(Torrent torrent) {
		Guid guid = new Guid();
		guid.setPermaLink(false);
		guid.setValue(torrent.getMagnetCode());
		return guid;
	}
}
