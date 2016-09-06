package org.rm3umf.domain;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


/**
 * Questa classe rappresenta la singola news relativo alla notizia.
 * 
 * @author Sirian
 *
 */

public class News {
	
	private String id;
	private String url;
	private String source;
	private String category;
	private  String title;
	private String description;
	private String newscontent;
	private String publish_date;
	private String update_date;
	private String crawl_date;
	private int numberinstance;
	private List<TweetEntity> newsEntity;
	private List<TweetTopic> newsTopic;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNewscontent() {
		return newscontent;
	}

	public void setNewscontent(String newscontent) {
		this.newscontent = newscontent;
	}

	public String getPublish_date() {
		return publish_date;
	}

	public void setPublish_date(String publish_date) {
		this.publish_date = publish_date;
	}

	public String getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}

	public String getCrawl_date() {
		return crawl_date;
	}

	public void setCrawl_date(String crawl_date) {
		this.crawl_date = crawl_date;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public int getNumberinstance() {
		return numberinstance;
	}

	public void setNumberinstance(int numberinstance) {
		this.numberinstance = numberinstance;
	}

	public boolean equals(Object urls){
		return (this.url).equals(((News)urls).getUrl());
	}
	
	public int hashCode(){
		return url.hashCode();
	}

	public List<TweetEntity> getNewsEntity() {
		return newsEntity;
	}

	public void setNewsEntity(List<TweetEntity> newsEntity) {
		this.newsEntity = newsEntity;
	}

	public List<TweetTopic> getNewsTopic() {
		return newsTopic;
	}

	public void setNewsTopic(List<TweetTopic> newsTopic) {
		this.newsTopic = newsTopic;
	}

	public String toString() {
		return "[News : id="+this.id+", url="+this.url+
			", source="+this.source+", category="+this.category+
			", title="+this.title+", description="+this.description+
			", newscontent="+this.newscontent+", publish_date="+this.publish_date+
			", update_date="+this.update_date+", crawl_date="+this.crawl_date+"]";
	}
}
