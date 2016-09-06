package org.rm3umf.persistenza;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.rm3umf.domain.Enrichment;
import org.rm3umf.domain.Message;
import org.rm3umf.domain.Period;
import org.rm3umf.domain.TweetEntity;
import org.rm3umf.domain.User;
import org.rm3umf.persistenza.postgreSQL.DataSourcePostgreSQL;

public interface TweetsEntityDAO {
	
	public List<TweetEntity> getTweetEntityFromTweetId(String tweetId, String type) throws PersistenceException;

	public TweetEntity retrieveByName(String name) throws PersistenceException;
	
	public List<Enrichment> filter(int tHRESHOLD) throws PersistenceException;

	public void delete(Enrichment enr) throws PersistenceException;

	public List<Enrichment> retrieveAll() throws PersistenceException; 

	public Map<String, Enrichment> retrieve4UserAndPeriod(User user,
			int periodid) throws PersistenceException; 
	
	public List<Message> doRetrieveByUserIdAndDate(User user,Period period) throws PersistenceException;
}
