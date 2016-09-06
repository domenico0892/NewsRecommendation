package org.rm3umf.domain;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class NewsEntity extends Enrichment{

	public News news;
	public String type;
	public String typeURI;
	public String uri;
	public Double relevance;
	public Timestamp publish_date;

	public NewsEntity() {

	}
	
	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeURI() {
		return typeURI;
	}

	public void setTypeURI(String typeURI) {
		this.typeURI = typeURI;
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

	public void setRelevance(Double relevance) {
		this.relevance = relevance;
	}

	public Timestamp getPublish_date() {
		return publish_date;
	}

	public void setPublish_date(Timestamp creationTime) {
		this.publish_date = creationTime;
	}

	@Override
	public String toString() {
		return "NewsEntity [type=" + type + ", typeURI="
				+ typeURI + ", name=" + name + ", uri=" + uri + ", relevance="
				+ relevance + ", publish_date=" + publish_date + "]";
	}
}
