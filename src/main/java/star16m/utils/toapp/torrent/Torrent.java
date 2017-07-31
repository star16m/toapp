package star16m.utils.toapp.torrent;

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
	@Column(insertable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date downloadDate;
}
