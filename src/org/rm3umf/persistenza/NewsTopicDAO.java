package org.rm3umf.persistenza;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.rm3umf.domain.Enrichment;
import org.rm3umf.domain.Message;
import org.rm3umf.domain.News;
import org.rm3umf.domain.NewsTopic;
import org.rm3umf.domain.Period;
import org.rm3umf.domain.User;

public interface NewsTopicDAO {


	public List<NewsTopic> getNewsTopicFromNewsId(String newsId, String type) throws PersistenceException;

	public NewsTopic retrieveByTopic(String topic) throws PersistenceException;
	
	public List<Enrichment> filter(int tHRESHOLD) throws PersistenceException;

	public void delete(Enrichment enr) throws PersistenceException;

	public List<Enrichment> retrieveAll() throws PersistenceException; 

	public Map<String, Enrichment> retrieve4UserAndPeriod(User user,
			int periodid) throws PersistenceException; 
	
	public List<Message> doRetrieveByUserIdAndDate(User user,Period period) throws PersistenceException;

}
