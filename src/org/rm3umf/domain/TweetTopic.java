package org.rm3umf.domain;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class TweetTopic extends Enrichment{
	
	public String uri;
	public Double relevance;
	public Date creation_time;
	public User user;
	public Message tweet;
	
	public TweetTopic() {
		
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

	public Date getCreation_time() {
		return creation_time;
	}

	public void setCreation_time(Date publish_date) {
		this.creation_time = publish_date;
	}

	@Override
	public String toString() {
		return "TweetTopic [uri=" + uri + ", relevance=" + relevance
				+ ", creation_time=" + creation_time + ", user=" + user
				+ ", tweet=" + tweet + "]";
	}

}
