package org.rm3umf.persistenza;

import java.util.List;
import java.util.Map;

import org.rm3umf.domain.Enrichment;
import org.rm3umf.domain.Message;
import org.rm3umf.domain.Period;
import org.rm3umf.domain.User;



public interface TopicDAO {
	
	public List<Enrichment> getTopicFromTweetId(String tweetId) throws PersistenceException;

	public List<Enrichment> retrieveByTopic(String topic) throws PersistenceException;
	
	public List<Enrichment> retrieveAll() throws PersistenceException ;
	
	public Map<String, Enrichment> retrieve4UserAndPeriod(User user, int periodid) throws PersistenceException ;
	
	public List<Message> doRetrieveByUserIdAndDate(User user,Period period) throws PersistenceException;

}
