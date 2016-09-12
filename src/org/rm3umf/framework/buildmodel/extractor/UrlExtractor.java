package org.rm3umf.framework.buildmodel.extractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.parser.ParseException;
import org.rm3umf.domain.Message;
import org.rm3umf.domain.News;
import org.rm3umf.framework.importing.DatasetAdapter;
import org.rm3umf.framework.importing.DatasetUmap;
import org.rm3umf.persistenza.AAFacadePersistence;
import org.rm3umf.persistenza.PersistenceException;

public class UrlExtractor {
	
	private String regex = "(http|https):(.)[^\\s]*";
	private String regex_urlextend = "expanded_url\":\"(.*?)\"";
	private DatasetUmap dataset;
	
	public UrlExtractor () {
		this.dataset = new DatasetUmap();
	}
	
	public List<String> getUrls (List<Message> listMessage) {
		Pattern pattern = Pattern.compile(regex);
		Pattern patternExt = Pattern.compile(regex_urlextend);
		Matcher matcher, matcherExt; 
		List<String> results = new ArrayList<String>();
		for (Message m : listMessage) {
			matcher = pattern.matcher(m.getText());
			while (matcher.find()) {
				results.add(matcher.group());
			}
			matcherExt = patternExt.matcher(m.getText());
			while (matcherExt.find()) {
				results.add(matcherExt.group().substring(15));
			}
		}
		return results;
	}
	
	
	public Map<News, String> getNews(List<Message> listMessage) throws ExtractorException, PersistenceException, IOException, ParseException{
		Map<News, String> mappaUrl=new HashMap<News, String>();


		for(Message message:listMessage){

			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(message.getText());
			while (matcher.find()){
				News u;
				//setto name trasformandolo in minuscolo
				String url=matcher.group();
				System.out.println("Analizzo url: "+url);
				//u = AAFacadePersistence.getInstance().doRetrieveNewsByUrl(url);
				u = dataset.doRetrieveNewsByUrl(url);
				if(u == null) {
					System.out.println("Notizia non trovata, analizzo extend url");
					//System.out.println(message.getIdMessage());
					//String json_str = AAFacadePersistence.getInstance().messageretriveJsonTextFromId(message.getIdMessage());
					String json_str = dataset.retriveJsonTextFromId(message.getIdMessage());
					//System.out.println(json_str);
					Pattern pattern_urlextend = Pattern.compile(regex_urlextend);
					Matcher matcher_urlextend = pattern_urlextend.matcher(json_str);
					//Se non matcha significa che il campo è null
					if (matcher_urlextend.find()){
						//setto name trasformandolo in minuscolo
						String url_extend=matcher_urlextend.group();
						url_extend = url_extend.substring(15, url_extend.length()-1);
						System.out.println("Analizzo url_extend" + url_extend);
						u = dataset.doRetrieveNewsByUrl(url_extend);
					}

				}
				if(u != null) {
					mappaUrl.put(u, message.getIdMessage());
					System.out.println("Aggiunta notizia "+u.getUrl());
				} 
			}
		}
		//pero ogni hashtag treo
		return mappaUrl;
	}


}
