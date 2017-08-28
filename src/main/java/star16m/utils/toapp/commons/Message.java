package star16m.utils.toapp.commons;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Message {

	@Id @GeneratedValue
	private Long id;
	@NotNull
	private String message;
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@NotNull
	private String type;
}
