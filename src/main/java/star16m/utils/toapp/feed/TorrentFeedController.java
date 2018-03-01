package star16m.utils.toapp.feed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TorrentFeedController {

	@Autowired
	private TorrentFeedView feedView;
	
	@RequestMapping(path="/api/torrent/rssfeed", produces=MediaType.APPLICATION_XML_VALUE)
	@ResponseBody
	public TorrentFeedView rssfeed() {
		return feedView;
	}
}
