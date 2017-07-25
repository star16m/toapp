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
public class Torrent {

	@Id @GeneratedValue
	private Long id;
	@NotNull
	private String title;
	@NotNull
	private String url;
	@NotNull
	private String size;
	@NotNull
	private String magnetCode;
	private String description;
}
