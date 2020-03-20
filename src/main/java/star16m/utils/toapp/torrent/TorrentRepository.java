package star16m.utils.toapp.torrent;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TorrentRepository extends JpaRepository<Torrent, String> {
    List<Torrent> findAllTorrentByOrderByDateStringDescKeywordAscTorrentFindDateDesc();

    List<Torrent> findAllTorrentByKeywordInOrderByDateStringDescKeywordAscTorrentFindDateDesc(List<String> keyword);

    @Modifying
    @Transactional
    void deleteByKeyword(String keyword);

    List<Torrent> findTorrentByKeywordOrderByDateStringDescKeywordAscTorrentFindDateDesc(String keyword);

    boolean existsByUrl(String detailUrl);

    boolean existsByMagnetCode(String magnetHash);
    Torrent findByMagnetCode(String magnetHash);
    List<Torrent> findTorrentByDownload(boolean download);

    List<Torrent> findTorrentByDateStringInOrderByDateStringDescKeywordAscTorrentFindDateDesc(List<String> targetDateStringList);

    // @Query(value="", nativeQuery=true)
    @Transactional
    void deleteByDateStringNotIn(List<String> targetDateStringList);

    Long countByDateStringIn(List<String> targetDateStringList);
}
