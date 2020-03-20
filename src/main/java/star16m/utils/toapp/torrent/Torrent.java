package star16m.utils.toapp.torrent;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Torrent {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    @Id
    @NotNull
    private String magnetCode;
    @NotNull
    private String title;
    @NotNull
    private String url;
    @NotNull
    private String size;
    @NotNull
    private String siteName;
    @NotNull
    private String keyword;
    @NotNull
    private boolean download;
    private String dateString;
    private LocalDate torrentFindDate;
    @Column(insertable = false)
    private LocalDate downloadDate;

    public LocalDate getDate() {
        if (dateString != null) {
            try {
                return LocalDate.parse(dateString, DATE_TIME_FORMATTER);
            } catch (DateTimeParseException e) {

            }
        }
        return null;
    }

    @Getter
    @Setter
    @ToString
    public static class TorrentLinkInfo {
        @NotEmpty
        private String title;
        @NotEmpty
        private String linkURL;
        @NotEmpty
        private String size;
        @NotEmpty
        private LocalDate createDate;
    }

    @Getter
    @Setter
    @ToString
    public static class TorrentLinkCreateInfo {
        List<String> message;
        List<TorrentLinkInfo> torrentLinkInfoList;
    }

    @Getter
    @Setter
    @ToString
    public static class TorrentSimpleInfo {
        private String torrentName;
        private String torrentSize;
        private String torrentMagnet;
    }
}
