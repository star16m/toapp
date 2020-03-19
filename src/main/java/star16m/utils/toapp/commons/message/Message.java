package star16m.utils.toapp.commons.message;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
public class Message {

    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    private String message;
    @NotNull
    private LocalDateTime createDate;
    @NotNull
    private String type;
}
