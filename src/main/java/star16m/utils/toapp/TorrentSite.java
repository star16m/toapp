package star16m.utils.toapp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class TorrentSite {

	@Id @GeneratedValue
	private Long id;
	@NotNull
	private String name;
	@NotNull
	private String searchUrl;
	
	@NotNull
	private String selector;
	@NotNull
	private String torrentNameSelector;
	private String torrentNameReplace;
	@NotNull
	private String torrentUrlSelector;
	private String torrentUrlReplace;
	@NotNull
	private String torrentSizeSelector;
	private String torrentSizeReplace;
	@NotNull
	private String torrentMagnetHashSelector;
	private String torrentMagnetHashReplace;
	
}
