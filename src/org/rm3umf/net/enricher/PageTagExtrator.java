package org.rm3umf.net.enricher;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.rm3umf.domain.News;
import org.rm3umf.domain.NewsEntity;
import org.rm3umf.domain.TweetEntity;
import org.rm3umf.domain.TweetTopic;
import org.rm3umf.persistenza.AAFacadePersistence;
import org.rm3umf.persistenza.PersistenceException;
import org.rm3umf.persistenza.mongodb.MongoConnection;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Domenico on 23/08/16.
 */
public class PageTagExtrator {

    private MongoCollection<Document> collTweet;
    private MongoCollection<Document> collPagine;
    private HtmlUnitDownloader htmlUnitDownloader;
//    private TagMeClient tagMeClient;
    private BoilerpipeService boilerpipeService;
//    private OpenCalaisClient openCalaisClient;
    private final String queryAnalyzedAndUrls = "{$and: [{\"is_analyzed\": {$exists: false}}, {\"urls\": {$not: {$size: 0}}}]}";
    private final String queryNewsAnalyzed = "{\"newsAnalyzed\": {$exists: false}}";

    public PageTagExtrator() {
        this.collTweet = MongoConnection.getInstance().getConnection().getDatabase("prove").getCollection("prove");
        this.collPagine = MongoConnection.getInstance().getConnection().getDatabase("pagine").getCollection("pagine");
        this.htmlUnitDownloader = new HtmlUnitDownloader();
//        this.tagMeClient = new TagMeClient();
        this.boilerpipeService = new BoilerpipeService();
//        this.openCalaisClient = new OpenCalaisClient();
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