package star16m.utils.toapp.torrent.collector;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import star16m.utils.toapp.keyword.Keyword;
import star16m.utils.toapp.site.Site;

@Getter
@ToString
@AllArgsConstructor
public class CollectResult {

	private Site collectSite;
	private Keyword collectKeyword;
	private int targetNum;
	private int foundNum;
	private boolean skipped;
	private String description;
	
	public String getResultString() {
		return String.format("%s%s:%s -> result [%d/%d] %s", skipped? "[SKIP] ":"", collectSite.getName(), collectKeyword.getKeyword(), foundNum, targetNum, description);
	}
}
