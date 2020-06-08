package star16m.utils.toapp.commons.page;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.function.BiConsumer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.springframework.data.util.Pair;
import star16m.utils.toapp.ToAppConstants;

import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class PageConnector {
	
	private String url;
	private Document document;
	// SSL 우회 등록
	public static void setSSL() throws NoSuchAlgorithmException, KeyManagementException {
		TrustManager[] trustAllCerts = new TrustManager[] {
				new X509TrustManager() {
					public X509Certificate[] getAcceptedIssuers() {
						return null;
					}

					@Override
					public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
					}

					@Override
					public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
					}
				}
		};
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new SecureRandom());
		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}


	public PageConnector(String url) throws Exception {
		this.url = url;
		if (url.contains("https:")) {
			setSSL();
		}
		document = Jsoup.connect(this.url).timeout(5000).userAgent(ToAppConstants.USER_AGENT).get();
	}
	private Elements select(String cssQuery) {
		Elements elements = document.select(cssQuery);
		return elements;
	}
	public Pair<String, Integer> findWithCountNum(String cssQuery) {
		Elements elements = select(cssQuery);
		if (elements != null) {
			return Pair.of(elements.toString(), elements.size());
		}
		return null;
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

	public String getElementsString() {
		return this.document.toString();
	}
}
