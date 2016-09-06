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
import org.rm3umf.domain.NewsEntity;
import org.rm3umf.domain.TweetEntity;
import org.rm3umf.domain.TweetEntity;
import org.rm3umf.domain.Message;
import org.rm3umf.domain.Period;
import org.rm3umf.domain.TweetTopic;
import org.rm3umf.domain.User;
import org.rm3umf.persistenza.DataSource;
import org.rm3umf.persistenza.PersistenceException;
import org.rm3umf.persistenza.TweetsEntityDAO;


public class TweetsEntityDAOpostgreSQL implements TweetsEntityDAO{

	private Logger logger = Logger.getLogger(TweetsEntityDAOpostgreSQL.class);

	@Override
	public List<TweetEntity> getTweetEntityFromTweetId(String tweetId, String type) throws PersistenceException {
		List<TweetEntity> lista = new LinkedList<TweetEntity>();
		TweetEntity ne = null;
		User u = null;
		Message m = null;
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "select userId, tweetId, semanticsTweetsEntity.type, typeURI, semanticsTweetsEntity.name, uri, relevance, creationTime, Count(*) "
					+ "from new_news_recommendation.semanticsTweetsEntity "
					+ "where tweetId=? "
					+ "Group By semanticsTweetsEntity.name";
			statement = connection.prepareStatement(retrieve);
			statement.setString(1, tweetId);
			result = statement.executeQuery();
			while(result.next()){
					ne = new TweetEntity();
					u = new User();
					m = new Message();
					u.setIduser(result.getLong(1));
				//
					m.setIdMessage(result.getString(2));
					ne.setType(result.getString(3));
					ne.setTypeURI(result.getString(4));
					ne.setName(result.getString(5));
					ne.setUri(result.getString(6));
					ne.setRelevance(result.getDouble(7));
					try {
						ne.setCreationTime(result.getDate(8));
					} catch (SQLException ex) {
						ne.setCreationTime(null);
					}
					ne.setNumberInstance(result.getInt(9));
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
	public TweetEntity retrieveByName(String name) throws PersistenceException {
boolean check = false;
TweetEntity ne = null;
		User u = null;
		Message m = null;
		DataSource ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "select userId, tweetId, semanticsTweetsEntity.type, typeURI, semanticsTweetsEntity.name, uri, relevance, creationTime, Count(*) "
					+ "from new_news_recommendation.semanticsTweetsEntity "
					+ "where semanticsTweetsEntity.name=?";
			statement = connection.prepareStatement(retrieve);
			statement.setString(1, name);
			result = statement.executeQuery();
			while(result.next()){
				if(!check) {
					ne = new TweetEntity();
					u = new User();
					m = new Message();
					u.setIduser(result.getLong(1));
					m.setIdMessage(result.getString(2));
					ne.setType(result.getString(3));
					ne.setTypeURI(result.getString(4));
					ne.setName(result.getString(5));
					ne.setUri(result.getString(6));
					ne.setRelevance(result.getDouble(7));
					try {
						ne.setCreationTime(result.getDate(8));
					} catch (SQLException ex) {
						ne.setCreationTime(null);
					}
					ne.setNumberInstance(1);
					ne.setTweet(m);
					check = true;
				} else {
					int c = ne.getNumberInstance();
					c++;
					ne.setNumberInstance(c);
				}
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
		return ne;
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
			String retrieve = "select name, tmpcount "
					+ "from ("
					+ "SELECT name ,COUNT(*) as tmpcount "
					+ "FROM new_news_recommendation.semanticsTweetsEntity "
					+ "GROUP BY name) "
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
			String update = "DELETE FROM semanticsTweetsEntity " +
					"WHERE name=?";
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
			String retrieve = "SELECT semanticsTweetsEntity.name, COUNT(*) FROM new_news_recommendation.semanticsTweetsEntity GROUP BY semanticsTweetsEntity.name;";
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
	public Map<String, Enrichment> retrieve4UserAndPeriod(User user,
			int periodid) throws PersistenceException {
		Enrichment e = null;
		Map<String, Enrichment> mappa = new HashMap<String, Enrichment>();
		DataSource ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "SELECT semanticsTweetsEntity.name, count(*) "
					+ "FROM new_news_recommendation.semanticsTweetsEntity, new_news_recommendation.tweets_sample "
					+ "where tweets_sample.userid=? and id=tweetId "
					+ "AND tweets_sample.creationTime > "
					+ "(Select enddate FROM new_news_recommendation.period Where id=?)"
					+ "Group BY semanticsTweetsEntity.name";

			statement = connection.prepareStatement(retrieve);
			statement.setLong(1, user.getIduser());
			statement.setInt(2, periodid-1);
			result = statement.executeQuery();
			while(result.next()){
				e = new Enrichment();
				e.setName(result.getString(1));
				e.setNumberInstance(result.getInt(2));
				mappa.put(e.getName(), e);
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



