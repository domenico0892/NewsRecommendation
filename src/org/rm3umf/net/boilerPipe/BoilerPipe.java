package org.rm3umf.net.boilerPipe;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

import org.xml.sax.SAXException;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;


public class BoilerPipe {

	public static String getNewsContentFromNews(String url_str) throws IOException, SAXException, BoilerpipeProcessingException {
		URL url = new URL(url_str);    
		// open connection
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY); 

		// stop following browser redirect
		httpURLConnection.setInstanceFollowRedirects(false);

		// extract location header containing the actual destination URL
		String expandedURL = httpURLConnection.getHeaderField("Location");
		httpURLConnection.disconnect();
		
		 URL url2 = new URL(expandedURL);
		return ArticleExtractor.INSTANCE.getText(url2); 

	}

	public static void main(String args[]) throws IOException, SAXException, BoilerpipeProcessingException {
		System.out.println(getNewsContentFromNews("https://t.co/Z1Vz6QKoRm"));

	}
}
