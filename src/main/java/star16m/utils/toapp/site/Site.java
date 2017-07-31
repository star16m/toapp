package star16m.utils.toapp.site;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Site {

	private static final String URL_PATTERN = "^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;\\[\\]]*[-a-zA-Z0-9+&@#/%=~_|\\[\\]]";
	@Getter
	@Setter
	@ToString
	public static class Create {
		@NotEmpty
		@Size(min=4, max=20)
		private String siteName;
		@NotEmpty
		@Size(min=10, max=255)
		@Pattern(regexp=URL_PATTERN, message="URL의 바른 형식이 아닙니다.")
		private String siteSearchUrl;
		@NotEmpty
		@Size(min=4, max=255)
		private String siteSelector;
		private String siteKeyword;
		private String torrentNameSelector;
		private String torrentNameReplace;
		private String torrentSizeSelector;
		private String torrentSizeReplace;
		private String torrentMagnetHashSelector;
		private String torrentMagnetHashReplace;
	}
	@Id @GeneratedValue
	private Long id;
	@NotNull
	@Size(min=4, max=20)
	private String name;
	@NotNull
	@Size(min=10, max=255)
	@Pattern(regexp=URL_PATTERN, message="URL의 바른 형식이 아닙니다.")
	private String searchUrl;
	@NotNull
	@Size(min=4, max=255)
	private String selector;
	@NotNull
	private String torrentNameSelector;
	private String torrentNameReplace;
	@NotNull
	private String torrentSizeSelector;
	private String torrentSizeReplace;
	@NotNull
	private String torrentMagnetHashSelector;
	private String torrentMagnetHashReplace;
	
}
