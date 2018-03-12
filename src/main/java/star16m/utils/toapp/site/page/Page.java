package star16m.utils.toapp.site.page;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class Page {
	@Id
	@GeneratedValue
	private Long id;
}
