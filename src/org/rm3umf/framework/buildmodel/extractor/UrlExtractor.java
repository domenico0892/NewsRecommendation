package org.rm3umf.framework.buildmodel.extractor;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.parser.ParseException;
import org.rm3umf.domain.Message;
import org.rm3umf.domain.News;
import org.rm3umf.persistenza.AAFacadePersistence;
import org.rm3umf.persistenza.PersistenceException;

public class UrlExtractor {
	
	private String regex = "(http|https):(.)[^\\s]*";
	private String regex_urlextend = "expanded_url\":\"(.*?)\"";
	
	public Map<News, String> getNews(List<Message> listMessage) throws ExtractorException, PersistenceException, IOException, ParseException{
		Map<News, String> mappaUrl=new HashMap<News, String>();


		for(Message message:listMessage){

			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(message.getText());

			while (matcher.find()){
				News u;
				//setto name trasformandolo in minuscolo
				String url=matcher.group().toLowerCase();
				u = AAFacadePersistence.getInstance().doRetrieveNewsByUrl(url);
				if(u == null) {
					String json_str = AAFacadePersistence.getInstance().messageretriveJsonTextFromId(message.getIdMessage());
					Pattern pattern_urlextend = Pattern.compile(regex_urlextend);
					Matcher matcher_urlextend = pattern_urlextend.matcher(json_str);
					//Se non matcha significa che il campo è null
					if (matcher_urlextend.find()){
						//setto name trasformandolo in minuscolo
						String url_extend=matcher_urlextend.group().toLowerCase();
						url_extend = url_extend.substring(15, url_extend.length()-1);
						u = AAFacadePersistence.getInstance().doRetrieveNewsByUrl(url_extend);
					}

				}
				if(u != null) {
					mappaUrl.put(u, message.getIdMessage()); 
				} 
			}
		}
		//pero ogni hashtag treo
		return mappaUrl;
	}


}
