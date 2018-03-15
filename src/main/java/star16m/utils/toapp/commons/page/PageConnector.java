package star16m.utils.toapp.commons.page;

import java.io.IOException;
import java.util.function.BiConsumer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import star16m.utils.toapp.ToAppConstants;

public class PageConnector {
	
	private String url;
	private Document document;
	public PageConnector(String url) throws IOException {
		this.url = url;
		document = Jsoup.connect(this.url).timeout(5000).userAgent(ToAppConstants.USER_AGENT).get();
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
