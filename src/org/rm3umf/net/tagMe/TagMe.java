package org.rm3umf.net.tagMe;

import org.rm3umf.domain.Enrichment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;


public class TagMe {

	public final static String KEY = "8020b57e2d41b6041c4fd06937acbec7";
	public final static boolean INCLUDE_ALL_SPORT = true;
	public final static boolean INCLUDE_ABSTRACT = true;
	public final static boolean INCLUDE_CATEGORIES = true;
	public final static String LANG = "it";
	
	public static String getEntityFromText (String text) throws  IOException  {
		String sentence_escape = URLEncoder.encode(text,"UTF-8");
		String url = "http://tagme.di.unipi.it/tag?";
		url += "&key="+KEY;
		url += "&text="+sentence_escape;
		url += "&include_all_sport="+INCLUDE_ALL_SPORT;
		//url += "&include_abstract="+INCLUDE_ABSTRACT;
		url += "&include_categories="+INCLUDE_CATEGORIES;
		url += "&lang="+LANG;

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		HttpResponse response = client.execute(request);
		BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		String result = "";
		while ((line = rd.readLine()) != null) {
			result += line;
		}
		return result;
	}

}
