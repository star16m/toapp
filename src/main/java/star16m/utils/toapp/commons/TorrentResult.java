package star16m.utils.toapp.commons;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TorrentResult<T> {

	private boolean success;
	private String message;
	private T data;
}
