package star16m.utils.toapp.torrent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Torrent {

	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMdd");
	@Id @NotNull
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
	@Temporal(TemporalType.TIMESTAMP)
	private Date torrentFindDate;
	@Column(insertable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date downloadDate;
	public Date getDate() {
		if (dateString != null) {
			try {
				return FORMAT.parse(dateString);
			} catch (ParseException e) {
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
		private DateTime createDate;
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
