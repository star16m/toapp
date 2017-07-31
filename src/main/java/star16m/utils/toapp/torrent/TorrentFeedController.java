package star16m.utils.toapp.torrent;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class TorrentFeedController {

	@Autowired
	private TorrentRepository torrentRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private TorrentFeedView feedView;
	
	@RequestMapping(path="/api/torrent/rssfeed", produces=MediaType.APPLICATION_XML_VALUE)
	@ResponseBody
	public TorrentFeedView rssfeed() {
		return feedView;
	}
}
