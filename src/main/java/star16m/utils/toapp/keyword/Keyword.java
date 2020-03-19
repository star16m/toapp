package star16m.utils.toapp.keyword;

import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Keyword {

    @Getter
    @Setter
    @ToString
    public static class ResponseDTO {
        @NotEmpty
        @Size(min = 2, max = 255)
        private String keyword;
    }

    @Id
    @GeneratedValue
    private Long id;
    @NotEmpty
    @Size(min = 2, max = 255)
    @Column(unique = true)
    private String keyword;
}
