package star16m.utils.toapp.site;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Site {

	@Getter
	@Setter
	@ToString
	public static class Create {
		@NotEmpty
		private String siteName;
		@NotEmpty
		private String siteSearchUrl;
		@NotEmpty
		private String siteSelector;
	}
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
