package org.rm3umf.persistenza.postgreSQL;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.rm3umf.domain.Enrichment;
import org.rm3umf.domain.NewsTopic;
import org.rm3umf.domain.TweetEntity;
import org.rm3umf.domain.TweetTopic;
import org.rm3umf.domain.Message;
import org.rm3umf.domain.Period;
import org.rm3umf.domain.User;
import org.rm3umf.persistenza.DataSource;
import org.rm3umf.persistenza.PersistenceException;
import org.rm3umf.persistenza.TweetsEntityDAO;
import org.rm3umf.persistenza.TweetsTopicDAO;


public class TweetsTopicDAOpostgreSQL implements TweetsTopicDAO{

	private Logger logger = Logger.getLogger(TweetsTopicDAOpostgreSQL.class);



	@Override
	public List<TweetTopic> getTweetTopicFromTweetId(String tweetId, String type) throws PersistenceException {
		List<TweetTopic> lista = new LinkedList<TweetTopic>();
		Map<String, TweetTopic> mappa = new HashMap<String, TweetTopic>();
		TweetTopic ne = null;
		User u = null;
		Message m = null;
		String name;
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "select userId, tweetId, topic, uri, relevance, creationTime, Count(*) "
					+ "from new_news_recommendation.semanticsTweetsTopic  "
					+ "where tweetId=? "
					+ "Group By topic";
			statement = connection.prepareStatement(retrieve);
			statement.setString(1, tweetId);
			result = statement.executeQuery();
			while(result.next()){
				ne = new TweetTopic();
				u = new User();
				m = new Message();
				u.setIduser(result.getLong(1));
				m.setIdMessage(result.getString(2));
				ne.setName(result.getString(3));
				ne.setUri(result.getString(4));
				ne.setRelevance(result.getDouble(5));
				try {
					ne.setCreation_time(result.getDate(6));
				} catch (SQLException ex) {
					ne.setCreation_time(null);
				}
				ne.setNumberInstance(result.getInt(7));
				ne.setUser(u);
				ne.setTweet(m);
				lista.add(ne);
			}
		}catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		}
		finally {
			try {
				if (result != null)
					result.close();
				if (statement != null) 
					statement.close();
				if (connection!= null)
					connection.close();
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
		return lista;
	}

	@Override
	public List<TweetTopic> retrieveByTopic(String topic) throws PersistenceException
	{
		List<TweetTopic> lista = new LinkedList<TweetTopic>();
		Map<String, TweetTopic> mappa = new HashMap<String, TweetTopic>();
		TweetTopic ne = null;
		User u = null;
		Message m = null;
		String name;
		DataSource ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "select userId, tweetId, topic, uri, relevance, creationTime, Count(*) "
					+ "from new_news_recommendation.semanticsTweetsTopic "
					+ "where topic=?";
			statement = connection.prepareStatement(retrieve);
			statement.setString(1, topic);
			result = statement.executeQuery();
			while(result.next()){
				ne = new TweetTopic();
				u = new User();
				m = new Message();
				u.setIduser(result.getLong(1));
				m.setIdMessage(result.getString(2));
				ne.setName(result.getString(3));
				ne.setUri(result.getString(4));
				ne.setRelevance(result.getDouble(5));
				try {
					ne.setCreation_time(result.getDate(6));
				} catch (SQLException ex) {
					ne.setCreation_time(null);
				}
				ne.setNumberInstance(result.getInt(7));
				ne.setUser(u);
				ne.setTweet(m);
				lista.add(ne);
			}
		}catch (SQLException e1) {
			throw new PersistenceException(e1.getMessage());
		}
		finally {
			try {
				if (result != null)
					result.close();
				if (statement != null) 
					statement.close();
				if (connection!= null)
					connection.close();
			} catch (SQLException e1) {
				throw new PersistenceException(e1.getMessage());
			}
		}
		return lista;
	}

	@Override
	public List<Enrichment> filter(int tHRESHOLD) throws PersistenceException {
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		List<Enrichment> listnewsentity =new LinkedList<Enrichment>();
		Enrichment e = null;
		try {
			connection = ds.getConnection();
			String retrieve = "select topic, tmpcount "
					+ "from ("
					+ "SELECT topic ,COUNT(*) as tmpcount "
					+ "FROM new_news_recommendation.semanticsTweetsTopic "
					+ "GROUP BY topic) "
					+ "as tmp "
					+ "where tmpcount < ?";
			statement = connection.prepareStatement(retrieve);
			statement.setInt(1, tHRESHOLD);
			result = statement.executeQuery();
			while(result.next()){
				e = new Enrichment();
				e.setName(result.getString(1));
				e.setNumberInstance(result.getInt(2));
				listnewsentity.add(e);	
			}
		}catch (SQLException e1) {
			throw new PersistenceException(e1.getMessage());
		}
		finally {
			try {
				if (result != null)
					result.close();
				if (statement != null) 
					statement.close();
				if (connection!= null)
					connection.close();
			} catch (SQLException e1) {
				throw new PersistenceException(e1.getMessage());
			}
		}
		return listnewsentity;
	}

	@Override
	public void delete(Enrichment enr) throws PersistenceException {
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ds.getConnection();
			String update = "DELETE FROM semanticsTweetsTopic " +
					"WHERE topic=?";
			statement = connection.prepareStatement(update);
			statement.setString(1, enr.getName());
			statement.executeUpdate();
		}
		catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		}
		finally {
			try {
				if (statement != null) 
					statement.close();
				if (connection!= null)
					connection.close();
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
	}

	@Override
	public List<Enrichment> retrieveAll() throws PersistenceException {
		List<Enrichment> lista = new LinkedList<Enrichment>();
		Enrichment ne = null;
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "SELECT topic, COUNT(*) FROM new_news_recommendation.semanticsTweetsTopic GROUP BY topic";
			statement = connection.prepareStatement(retrieve);
			result = statement.executeQuery();
			while(result.next()){
				ne = new Enrichment();
				ne.setName(result.getString(1));
				ne.setNumberInstance(result.getInt(2));
				lista.add(ne);
			}
		}catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		}
		finally {
			try {
				if (result != null)
					result.close();
				if (statement != null) 
					statement.close();
				if (connection!= null)
					connection.close();
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
		return lista;
	}

	@Override
	public Map<String, Enrichment> retrieve4UserAndPeriod(User user, int periodid) throws PersistenceException {
		Enrichment ne = null;
		Map<String, Enrichment> mappa = new HashMap<String, Enrichment>();
		DataSource ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "SELECT topic , count(*) "
					+ "FROM new_news_recommendation.semanticsTweetsTopic, new_news_recommendation.tweets_sample "
					+ "where tweets_sample.userid=? and id=tweetId "
					+ "AND tweets_sample.creationTime > "
					+ "(Select enddate FROM new_news_recommendation.period Where id=?) "
					+ "Group BY topic";
			statement = connection.prepareStatement(retrieve);
			statement.setLong(1, user.getIduser());
			statement.setInt(2, periodid-1);
			result = statement.executeQuery();
			while(result.next()){
				ne = new Enrichment();
				ne.setName(result.getString(1));
				ne.setNumberInstance(result.getInt(2));
				mappa.put(ne.getName(), ne);
			}
		}catch (SQLException e1) {
			throw new PersistenceException(e1.getMessage());
		}
		finally {
			try {
				if (result != null)
					result.close();
				if (statement != null) 
					statement.close();
				if (connection!= null)
					connection.close();
			} catch (SQLException e1) {
				throw new PersistenceException(e1.getMessage());
			}
		}
		return mappa;
	}
	
	
	public List<Message> doRetrieveByUserIdAndDate(User user,Period period) throws PersistenceException {
		String dataInizio=period.getDataInizioPeriodo();
		String dataFine=period.getDataFinePeriodo();
		Message tweet = null;
		List<Message> listaTweet = new ArrayList<Message>();
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "select id,content,creationTime from tweets_sample where creationTime>=? and creationTime<=? and userId=?";
			statement = connection.prepareStatement(retrieve);
			statement.setTimestamp(1, Timestamp.valueOf(dataInizio));
			statement.setTimestamp(2, Timestamp.valueOf(dataFine));
			statement.setLong(3, user.getIduser());
			result = statement.executeQuery();
			while (result.next()) {
				tweet = new Message();
				tweet.setIdMessage(result.getString(1));
				//oppure posso creare un UserProxy
				tweet.setUser(user);
				tweet.setText(result.getString(2));
				tweet.setDate(result.getDate(3).toString());
				listaTweet.add(tweet);
			}
		}
		catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		}
		finally {
			try {
				if (result != null)
					result.close();
				if (statement != null) 
					statement.close();
				if (connection!= null)
					connection.close();
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
		return listaTweet;
	}
}



