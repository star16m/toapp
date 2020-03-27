package star16m.utils.toapp.torrent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface TorrentRepository extends JpaRepository<Torrent, String> {
    List<Torrent> findAllTorrentByOrderByDateStringDescKeywordAscTorrentFindDateDesc();

    List<Torrent> findAllTorrentByKeywordOrderByDateStringDescKeywordAscTorrentFindDateDesc(String keyword);

    @Modifying
    @Transactional
    void deleteByKeyword(String keyword);

    boolean existsByUrl(String detailUrl);

    boolean existsByMagnetCode(String magnetHash);

    Torrent findByMagnetCode(String magnetHash);

    List<Torrent> findTorrentByDownload(boolean download);

    @Modifying
    @Query(value = "delete from torrent where torrent_find_date < DATEADD('DAY', -:dataAvailableDay, CURRENT_DATE)", nativeQuery = true)
    void deleteOldData(Long dataAvailableDay);

    @Query(value = "select t.* from torrent t order by date_string desc, torrent_find_date desc limit :top", nativeQuery = true)
    List<Torrent> findTopOrderByDateStringDesc(Long top);

    @Query(value = "select t.* from torrent t where torrent_find_date > DATEADD('DAY', -:last, CURRENT_DATE) order by date_string desc, torrent_find_date desc", nativeQuery = true)
    List<Torrent> findLastDaysOrderByDateStringDesc(Long last);

    @Query(value =
            "select new map(k.keyword as id, count(*) as result) \n" +
                    "from Torrent t join Keyword k on t.keyword = k.keyword \n" +
                    "where 1=1 \n" +
                    "group by k.keyword"
    )
    List<Map<String, Long>> groupByKeyword();
}
