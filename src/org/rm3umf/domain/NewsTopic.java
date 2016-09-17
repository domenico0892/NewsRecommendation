package org.rm3umf.domain;

import java.sql.Timestamp;

public class NewsTopic extends Enrichment{
	
	public News news ;
	public String uri;
	public Double relevance;
	public Timestamp publish_Timestamp;
	public String topic;
	
	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public NewsTopic() {
		
	}
	
	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Double getRelevance() {
		return relevance;
	}

	public void setRelevance(Double relavance) {
		this.relevance = relavance;
	}

	public Timestamp getPublish_date() {
		return publish_Timestamp;
	}

	public void setPublish_date(Timestamp publish_Timestamp) {
		this.publish_Timestamp = publish_Timestamp;
	}

	@Override
	public String toString() {
		return "NewsTopic [news=" + news + ", uri=" + uri + ", relevance="
				+ relevance + ", publish_Timestamp=" + publish_Timestamp + "]";
	}	
}
