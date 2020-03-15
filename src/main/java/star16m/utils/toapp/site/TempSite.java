package star16m.utils.toapp.site;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TempSite {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(min = 4, max = 20)
    private String name;
    @NotNull
    @Size(min = 10, max = 255)
    @Pattern(regexp = Site.SITE_URL_PATTERN, message = "URL의 바른 형식이 아닙니다.")
    private String searchUrl;

    public Site toSite() {
        Site site = new Site();
        site.setName(this.name);
        site.setSearchUrl(this.searchUrl);
        return site;
    }
}
