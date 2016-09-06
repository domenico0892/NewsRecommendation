package org.rm3umf.domain;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class NewsTopic extends Enrichment{
	
	public News news ;
	public String uri;
	public Double relevance;
	public Date publish_date;
	
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

	public Date getPublish_date() {
		return publish_date;
	}

	public void setPublish_date(Date publish_date) {
		this.publish_date = publish_date;
	}

	@Override
	public String toString() {
		return "NewsTopic [news=" + news + ", uri=" + uri + ", relevance="
				+ relevance + ", publish_date=" + publish_date + "]";
	}	
}
