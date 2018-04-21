package star16m.utils.toapp.torrent;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface TorrentRepository extends JpaRepository<Torrent, String> {
    List<Torrent> findAllTorrentByOrderByDateStringDescKeywordAscTorrentFindDateDesc();

    @Modifying
    @Transactional
    void deleteByKeyword(String keyword);

    List<Torrent> findTorrentByKeywordOrderByDateStringDescKeywordAscTorrentFindDateDesc(String keyword);

    boolean existsByUrl(String detailUrl);

    List<Torrent> findTorrentByDownload(boolean download);

    List<Torrent> findTorrentByDateStringInOrderByDateStringDescKeywordAscTorrentFindDateDesc(List<String> targetDateStringList);

    @Transactional
    void deleteByDateStringNotIn(List<String> targetDateStringList);

    Long countByDateStringIn(List<String> targetDateStringList);
}
