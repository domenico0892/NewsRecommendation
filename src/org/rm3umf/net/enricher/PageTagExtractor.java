package org.rm3umf.net.enricher;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.rm3umf.domain.Message;
import org.rm3umf.domain.News;
import org.rm3umf.domain.NewsEntity;
import org.rm3umf.domain.TweetEntity;
import org.rm3umf.domain.TweetTopic;
import org.rm3umf.domain.User;
import org.rm3umf.persistenza.AAFacadePersistence;
import org.rm3umf.persistenza.PersistenceException;
import org.rm3umf.persistenza.mongodb.MongoConnection;
import org.rm3umf.persistenza.postgreSQL.DataSourcePostgreSQL;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Domenico on 23/08/16.
 */
public class PageTagExtractor {

    private MongoCollection<Document> collTweet;
//    private MongoCollection<Document> collPagine;
//    private HtmlUnitDownloader htmlUnitDownloader;
//    private TagMeClient tagMeClient;
    private BoilerpipeService boilerpipeService;
//    private OpenCalaisClient openCalaisClient;
//    private final String queryAnalyzedAndUrls = "{$and: [{\"is_analyzed\": {$exists: false}}, {\"urls\": {$not: {$size: 0}}}]}";
    private final String queryNewsAnalyzed = "{\"newsAnalyzed\": {$exists: false}}";
    private final String queryTweetAnalyzed = "{\"tweetAnalyzed\": {$exists: false}}";

    public PageTagExtractor() {
        this.collTweet = MongoConnection.getInstance().getConnection().getDatabase("prove").getCollection("prove");
//        this.collPagine = MongoConnection.getInstance().getConnection().getDatabase("pagine").getCollection("pagine");
//        this.htmlUnitDownloader = new HtmlUnitDownloader();
//        this.tagMeClient = new TagMeClient();
        this.boilerpipeService = new BoilerpipeService();
//        this.openCalaisClient = new OpenCalaisClient();
    }
    
    public void extractEntityTweet() throws PersistenceException {
    	Document query = Document.parse(queryTweetAnalyzed);
    	FindIterable<Document> docs = this.collTweet.find(query);
    	Map<String, String> entities;
    	TweetEntity te;
    	User u;
    	Message m;
    	for (Document doc : docs) {
    		entities = StanfordTagger.getEntities((String) doc.get("cleanedTweet"));
    		for (String key : entities.keySet()) {
    			u = new User();
    			u.setIduser((Long)doc.get("id_user"));
    			m = new Message();
    			m.setUser(u);
    			m.setIdMessage((String)doc.get("id"));
    			te = new TweetEntity();
    			te.setCreationTime(new Timestamp(System.currentTimeMillis()));
    			te.setName(key);
    			te.setNumberInstance(1);
    			te.setRelevance(1d);
    			te.setTweet(m);
    			te.setType(entities.get(key));
    			te.setTypeURI("");
    			te.setUri("");
    			te.setUser(u);
    			AAFacadePersistence.getInstance().saveTweetEntity(te);		
    		}    		
    		this.collTweet.replaceOne(new Document().append("_id", doc.getObjectId("_id")), doc.append("tweetAnalyzed", "true"));
    	}
    }
    
    public void extractEntityNews() {
    	try {
			News n = AAFacadePersistence.getInstance().retriveNotAnalyzed();
			Map<String, String> entities;
			NewsEntity ne;
			while (n != null) {
				entities = StanfordTagger.getEntities(n.getNewscontent());
				for (String key : entities.keySet()) {
					ne = new NewsEntity();
					ne.setNews(n);
					ne.setPublish_date(new Timestamp(System.currentTimeMillis()));
					ne.setName(key);
					ne.setType(entities.get(key));
					ne.setRelevance(1d);
					ne.setTypeURI("");
					ne.setUri("");
					ne.setNumberInstance(1);
					AAFacadePersistence.getInstance().saveNewsEntity(ne);
				}
				AAFacadePersistence.getInstance().updateEntityAnalyzed(Long.parseLong(n.getId()), 1);
				n = AAFacadePersistence.getInstance().retriveNotAnalyzed();
			}
		} 
    	catch (PersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

    public void importNews() {
    	Document query = Document.parse(queryNewsAnalyzed);
    	FindIterable<Document> docs = this.collTweet.find(query);
    	String testo;
    	News news;
    	for (Document doc : docs) {
    		List<String> urls = (List<String>) doc.get("urls");
    		if (urls != null) {
    			for (String url : urls) {
    				testo = this.boilerpipeService.getTextFromUrl(url);
    				news = new News();
    				news.setUrl(url);
    				news.setSource(url);
    				news.setCategory("");
    				news.setTitle("");
    				news.setDescription("");
    				news.setNewscontent(testo);
    				news.setPublish_date(new java.sql.Date(System.currentTimeMillis()));
    				news.setUpdate_date(null);
    				news.setCrawl_date(new java.sql.Date(System.currentTimeMillis()));
    				try {
						AAFacadePersistence.getInstance().saveNews(news);
					} catch (PersistenceException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
    		this.collTweet.replaceOne(new Document().append("_id", doc.getObjectId("_id")), doc.append("newsAnalyzed", "true"));
    		}
    		
    	}
    }
}