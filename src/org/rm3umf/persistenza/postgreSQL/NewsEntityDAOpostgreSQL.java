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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.rm3umf.domain.Enrichment;
import org.rm3umf.domain.News;
import org.rm3umf.domain.NewsEntity;
import org.rm3umf.domain.NewsTopic;
import org.rm3umf.domain.PseudoFragment;
import org.rm3umf.domain.TweetEntity;
import org.rm3umf.domain.Message;
import org.rm3umf.domain.Period;
import org.rm3umf.domain.User;
import org.rm3umf.framework.buildmodel.extractor.ExtractorException;
import org.rm3umf.persistenza.AAFacadePersistence;
import org.rm3umf.persistenza.DataSource;
import org.rm3umf.persistenza.NewsEntityDAO;
import org.rm3umf.persistenza.PersistenceException;


public class NewsEntityDAOpostgreSQL implements NewsEntityDAO{

	private Logger logger = Logger.getLogger(NewsEntityDAOpostgreSQL.class);

	@Override
	public List<NewsEntity> getNewsEntityFromNewsId(String newsId, String type) throws PersistenceException {
		List<NewsEntity> lista = new LinkedList<NewsEntity>();
		NewsEntity ne = null;
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		String s = null;
		try {
			connection = ds.getConnection();
			String retrieve = "SELECT semanticsNewsEntity.type, typeURI, semanticsNewsEntity.name, uri, relevance, publish_date, COUNT(*) "
					+ "FROM new_news_recommendation.semanticsNewsEntity "
					+ "WHERE newsId=? "
					+ "GROUP BY semanticsNewsEntity.name";

			statement = connection.prepareStatement(retrieve);
			statement.setString(1, newsId);
			result = statement.executeQuery();
			while(result.next()){
				s = result.getString(3);
				if(s != null){
					ne = new NewsEntity();
					ne.setType(result.getString(1));
					ne.setTypeURI(result.getString(2));
					ne.setName(s);
					ne.setUri(result.getString(4));
					ne.setRelevance(result.getDouble(5));
					try {
						ne.setPublish_date(result.getTimestamp(6));
					} catch (SQLException ex) {
						ne.setPublish_date(null);
					}
					ne.setNumberInstance(result.getInt(7));
					lista.add(ne);
				}
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
	public NewsEntity retrieveByName(String name) throws PersistenceException {
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		NewsEntity ne = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();		
			String retrieve = "SELECT semanticsNewsEntity.type, typeURI, semanticsNewsEntity.name, uri, relevance, publish_date, COUNT(*) "
					+ "FROM new_news_recommendation.semanticsNewsEntity "
					+ "WHERE name=? "
					+ "GROUP BY semanticsNewsEntity.name";

			statement = connection.prepareStatement(retrieve);
			statement.setString(1, name);
			result = statement.executeQuery();
			if(result.next()){
				ne = new NewsEntity();
				ne.setType(result.getString(1));
				ne.setTypeURI(result.getString(2));
				ne.setName(result.getString(3));
				ne.setUri(result.getString(4));
				ne.setRelevance(result.getDouble(5));
				try {
					ne.setPublish_date(result.getTimestamp(6));
				} catch (SQLException ex) {
					ne.setPublish_date(null);
				}
				ne.setNumberInstance(result.getInt(7));
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
		String s = null;
		try {
			connection = ds.getConnection();
			String retrieve = "select name, tmpcount "
					+ "from ("
					+ "SELECT name ,COUNT(*) as tmpcount "
					+ "FROM new_news_recommendation.semanticsNewsEntity "
					+ "GROUP BY name) "
					+ "as tmp "
					+ "where tmpcount < ?";
			statement = connection.prepareStatement(retrieve);
			statement.setInt(1, tHRESHOLD);
			result = statement.executeQuery();
			while(result.next()){
				s = result.getString(1);
				if(s != null){
					e = new Enrichment();
					e.setName(s);
					e.setNumberInstance(result.getInt(2));
					listnewsentity.add(e);
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
		return listnewsentity;
	}

	@Override
	public void delete(Enrichment enr) throws PersistenceException {
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = ds.getConnection();
			String update = "DELETE FROM new_news_recommendation.semanticsNewsEntity " +
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
		String s = null;
		try {
			connection = ds.getConnection();
			String retrieve = "SELECT semanticsNewsEntity.name, COUNT(*) "
					+ "FROM new_news_recommendation.semanticsNewsEntity "
					+ "GROUP BY semanticsNewsEntity.name";
			statement = connection.prepareStatement(retrieve);
			result = statement.executeQuery();
			while(result.next()){
				s = result.getString(1);
				if(s!=null){
					ne = new Enrichment();
					ne.setName(s);
					ne.setNumberInstance(result.getInt(2));
					lista.add(ne);
				}
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
			String retrieve = "SELECT name, count(*) "
					+ "FROM new_news_recommendation.semanticsNewsEntity, "
					+ "new_news_recommendation.user_news, "
					+ "new_news_recommendation.tweets_sample "
					+ "where userid=? AND newsId=id_news AND id_user= userId AND tweets_sample.creationTime > "
					+ "(Select enddate FROM new_news_recommendation.period Where id=?) "
					+ "Group BY name";			
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

	@Override
	public List<Message> doRetrieveByUserIdAndDate(User user, Period period) throws PersistenceException {
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
			String retrieve = "select news.id,content,creationTime "
					+ "from tweets_sample , user_news, news "
					+ "where creationTime>=? and creationTime<=? and userId=? and userId=id_user and id_news=news.id";
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

