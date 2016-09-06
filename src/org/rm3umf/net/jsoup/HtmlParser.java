package org.rm3umf.net.jsoup;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



/**
 * Dovrebbe prendere le informazioni per popolare la tabella news nel db.
 * Problemi: come prendere le informazioni da sorgenti differenti
 * le pagine sono vecchie quindi non ci sono più
 * @author Sirian
 *
 */
public class HtmlParser {

	public static void getInfoHtml(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		Elements links = doc.select("a[href]");
		Elements media = doc.select("[src]");
		Elements imports = doc.select("link[href]");

		//  System.out.println(doc.title());

		// System.out.println(doc.g);

		for(Element meta : doc.select("meta")) {
			System.out.println("Name: " + meta.attr("description"));
			// System.out.println("Name: " + meta.attr("name") + " - Content: " + meta.attr("content"));
		}


	}

	public static void main(String args[]) throws IOException {
		getInfoHtml("http://www.tgcom24.mediaset.it/cronaca/lazio/il-papa-la-chiesa-deve-testimoniare-la-poverta-non-si-viva-da-faraoni-_2142443-201502a.shtml");
	}

	public static String getTitleFromHtml(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		return doc.title();
	} 
}
