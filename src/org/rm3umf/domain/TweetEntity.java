package org.rm3umf.domain;

import java.sql.Timestamp;
import java.util.Date;

public class TweetEntity extends Enrichment{

	
	public String type;
	public String typeURI;
	public String uri;
	public Double relevance;
	public Timestamp creationTime;
	public User user;
	public Message tweet;

	public TweetEntity() {

	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Message getTweet() {
		return tweet;
	}

	public void setTweet(Message tweet) {
		this.tweet = tweet;
	}

	public String getType() {
		return type;
	}
	
	public String getType (int level) {
		if (this.type.length() < 2) {
			return "";
		}
		String[] types = this.type.split(";");
		for (int i=0; i<types.length;i++) {
			if (i==level)
				return types[i];
		}
		return types[level-1];
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

	public Timestamp getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Timestamp creationTime) {
		this.creationTime = creationTime;
	}

	@Override
	public String toString() {
		return "TweetEntity [type=" + type + ", typeURI=" + typeURI + ", name=" + name
				+ ", uri=" + uri + ", relevance=" + relevance
				+ ", creationTime=" + creationTime + "]";
	}
}
