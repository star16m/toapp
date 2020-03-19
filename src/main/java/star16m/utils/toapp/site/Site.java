package star16m.utils.toapp.site;

import javax.persistence.Column;
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

	public static final String SITE_URL_PATTERN = "^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;\\[\\]]*[-a-zA-Z0-9+&@#/%=~_|\\[\\]]";
	@Id
	@GeneratedValue
	private Long id;
	@NotNull
	@Size(min = 4, max = 20)
	private String name;
	@NotNull
	@Size(min = 10, max = 255)
	@Pattern(regexp = SITE_URL_PATTERN, message = "URL의 바른 형식이 아닙니다.")
	private String searchUrl;
	private String pageSelector;
	private String nameSelector;
	private String sizeSelector;
	private String dateSelector;
	@Column(insertable = false, columnDefinition = "tinyint(1) default 1")
	private boolean useable = true;
	private String torrentNameSelector;
	private String torrentNameReplace;
	private String torrentSizeSelector;
	private String torrentSizeReplace;
	private String torrentMagnetHashSelector;
	private String torrentMagnetHashReplace;

	@Getter
	@Setter
	@ToString
	public static class SiteInfo {
		@NotEmpty
		@Size(min = 4, max = 20)
		private String siteName;
		@NotEmpty
		@Size(min = 10, max = 255)
		@Pattern(regexp = SITE_URL_PATTERN, message = "URL의 바른 형식이 아닙니다.")
		private String siteSearchUrl;
		@NotEmpty
		@Size(min = 4, max = 255)
		private String pageSelector;
		private String siteKeyword;
		@NotNull
		@Size(min = 4, max = 255)
		private String nameSelector;
		@NotNull
		@Size(min = 4, max = 255)
		private String sizeSelector;
		@NotNull
		@Size(min = 4, max = 255)
		private String dateSelector;
	}
	@Getter
	@Setter
	@ToString
	public static class SiteCreate {
		@NotEmpty
		@Size(min = 4, max = 20)
		private String siteName;
		@NotEmpty
		@Size(min = 10, max = 255)
		@Pattern(regexp = SITE_URL_PATTERN, message = "URL의 바른 형식이 아닙니다.")
		private String siteSearchUrl;
		@NotEmpty
		@Size(min = 4, max = 255)
		private String pageSelector;
		private String siteKeyword;
		private String torrentDetailPageURL;
		@NotNull
		@Size(min = 4, max = 255)
		private String nameSelector;
		@NotNull
		@Size(min = 4, max = 255)
		private String sizeSelector;
		@NotNull
		@Size(min = 4, max = 255)
		private String dateSelector;
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

	@Getter
	@Setter
	@ToString
	public static class SimpleSiteRequest<T> {
		private Long id;
		private T request;
	}
}
