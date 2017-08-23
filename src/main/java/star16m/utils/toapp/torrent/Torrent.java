package star16m.utils.toapp.torrent;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
