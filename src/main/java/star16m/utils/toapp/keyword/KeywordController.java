package star16m.utils.toapp.keyword;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import star16m.utils.toapp.torrent.TorrentRepository;

import java.util.List;

// @Controller
@RequestMapping("keyword")
@Slf4j
public class KeywordController {

    @Autowired
    private KeywordRepository keywordRepository;
    @Autowired
    private TorrentRepository torrentRepository;

    @GetMapping
    public String get(Model model) {
        List<Keyword> keywordList = keywordRepository.findAll();
        log.info("find {} keywords.", keywordList.size());
        model.addAttribute("keywords", keywordList);
        model.addAttribute("dto", new Keyword.ResponseDTO());
        return "keyword";
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable Long id, Model model) {
        Keyword keyword = keywordRepository.findById(id).orElse(null);
        if (keyword != null) {
            log.info("delete keyword [{}]", keyword);
            torrentRepository.deleteByKeyword(keyword.getKeyword());
            keywordRepository.deleteById(id);
        }

        return get(model);
    }
//	@PostMapping
//	public String create(@ModelAttribute("dto") @Valid Keyword.ResponseDTO dto, BindingResult result, Model model) {
//		log.info("create method called!!");
//		log.info("dto[{}]", dto);
//		Keyword keyword = modelMapper.map(dto, Keyword.class);
//		log.info("keyword [{}]", keyword);
//
//		keywordRepository.save(keyword);
//		return get(model);
//	}

}
