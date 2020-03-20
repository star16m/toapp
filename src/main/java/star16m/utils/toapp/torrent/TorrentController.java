package star16m.utils.toapp.torrent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import star16m.utils.toapp.keyword.Keyword;
import star16m.utils.toapp.keyword.KeywordRepository;
import star16m.utils.toapp.torrent.collector.TorrentCollector;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("torrent")
@Slf4j
public class TorrentController {

    @Autowired
    private TorrentRepository torrentRepository;
    @Autowired
    private KeywordRepository keywordRepository;
    @Autowired
    private TorrentCollector collector;

    @GetMapping
    public String torrent(@RequestParam(required = false, defaultValue = "-1") Integer lastDays, @RequestParam(required = false, defaultValue = "-1") Integer targetDate, @RequestParam(name = "name", required = false) String torrentName, Model model) {
        log.debug("try findAll torrent site.");

        List<Torrent> torrentList = null;
        model.addAttribute("currentKeyword", "");
        if (targetDate != null && targetDate > 0) {
            List<String> targetDateList = new ArrayList<>();
            targetDateList.add(String.valueOf(targetDate));
            torrentList = torrentRepository.findTorrentByDateStringInOrderByDateStringDescKeywordAscTorrentFindDateDesc(targetDateList);
        } else if (torrentName != null && torrentName.length() > 0) {
            log.info("try torrent name [" + torrentName + "]");
            model.addAttribute("currentKeyword", torrentName);
            torrentList = torrentRepository.findTorrentByKeywordOrderByDateStringDescKeywordAscTorrentFindDateDesc(torrentName);
        } else {
            torrentList = torrentRepository.findAllTorrentByOrderByDateStringDescKeywordAscTorrentFindDateDesc();
        }
        List<Keyword> keywordList = keywordRepository.findAll();
        log.debug("successfully findAll torrent site. size:" + torrentList.size());

        model.addAttribute("torrents", torrentList);
        model.addAttribute("keywords", keywordList);
        return "torrent";
    }

    @PostMapping("search")
    public String search(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        log.debug("keyword = [{}]", keyword);
        if (keyword != null && keyword.length() > 0) {
            Keyword key = keywordRepository.findByKeyword(keyword);
            if (key != null && key.getKeyword() != null && key.getKeyword().length() > 0) {
                // already exists keyword
            } else {
                key = new Keyword();
                key.setKeyword(keyword);
                keywordRepository.save(key);
                collector.collect(keyword);
            }
        }
        String encodedString = "";
        try {
            encodedString = URLEncoder.encode(keyword, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            log.warn(e.getMessage());
        }
        return "redirect:/torrent/?name=" + encodedString;
    }

    @PatchMapping("download/{magnetHash}")
    public String createTorrent(@PathVariable String magnetHash, @RequestParam String currentKeyword, Model model) {
        log.info("Current keyword = {}", currentKeyword);
        log.info("MODEL[{}]" + model);
        log.info("patched torrent ::: " + magnetHash);
        Torrent torrent = torrentRepository.findByMagnetCode(magnetHash);
        torrent.setDownload(true);
        torrent.setDownloadDate(LocalDate.now());
        torrentRepository.save(torrent);
        log.info("target torrent = [{}]", torrent);
        String encodedString = "";
        try {
            encodedString = URLEncoder.encode(currentKeyword, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            log.warn(e.getMessage());
        }
        return "redirect:/torrent/?name=" + encodedString;
    }

    @PatchMapping("collect")
    public String collectTorrent(@RequestParam String currentKeyword, Model model) {
        log.info("CALLED collectTorrent{}", currentKeyword);
        collector.collect(currentKeyword);
        String encodedString = "";
        try {
            encodedString = URLEncoder.encode(currentKeyword, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            log.warn(e.getMessage());
        }
        return "redirect:/torrent/?name=" + encodedString;
    }
}
