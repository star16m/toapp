package star16m.utils.toapp.commons.page;

import java.io.IOException;
import java.util.function.BiConsumer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PageConnector {
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36";
	
	private String url;
	private Document document;
	public PageConnector(String url) throws IOException {
		this.url = url;
		document = Jsoup.connect(this.url).timeout(5000).userAgent(USER_AGENT).get();
	}
	private Elements select(String cssQuery) {
		Elements elements = document.select(cssQuery);
		return elements;
	}
	public String find(String cssQuery) {
		Elements elements = select(cssQuery);
		if (elements != null) {
			return elements.toString();
		}
		return null;
	}
	public int find(String cssQuery, BiConsumer<Element, Integer> initializer) {
		Elements elements = select(cssQuery);
		Integer i = 0;
		for (Element element : elements) {
			initializer.accept(element , i++);
		}
		return elements.size();
	}
}
