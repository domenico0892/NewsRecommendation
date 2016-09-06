package org.rm3umf.persistenza.postgreSQL;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.rm3umf.domain.Message;
import org.rm3umf.domain.Period;
import org.rm3umf.domain.News;
import org.rm3umf.domain.User;
import org.rm3umf.persistenza.DataSource;
import org.rm3umf.persistenza.PersistenceException;
import org.rm3umf.persistenza.NewsDAO;


public class NewsDAOpostgreSQL implements NewsDAO{

	private Logger logger = Logger.getLogger(NewsDAOpostgreSQL.class);

	public News doRetrieveNewsById(String id) throws PersistenceException {
		News n=null;
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "select * from news where id=?";
			statement = connection.prepareStatement(retrieve);
			statement.setString(1, id);
			result = statement.executeQuery();
			if(result.next()){
				DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
				n = new News();
				n.setId(result.getString(1));
				n.setSource(result.getString(2));
				n.setCategory(result.getString(3));
				n.setUrl(result.getString(4));
				n.setTitle(result.getString(5));
				n.setDescription(result.getString(6));
				n.setNewscontent(result.getString(7));
				String text = df.format(result.getDate(8));
				n.setPublish_date(text);
				String text1 = df.format(result.getDate(9));
				n.setUpdate_date(text1);
				String text2 = df.format(result.getDate(10));
				n.setCrawl_date(text2);
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
		return n;
	}

	/**
	 * Restituisce le news che sono state referenziati da meno di una certa soglia
	 * dai SignalComponent
	 */ 
	public List<News> doRetrieveNewsInrilenvate(int soglia) throws PersistenceException {
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		List<News> listNews =new LinkedList<News>();
		News n = null;
		try {
			connection = ds.getConnection();
			String retrieve = "SELECT * " +
					"FROM news " +
					"WHERE numinstance<?";
			statement = connection.prepareStatement(retrieve);
			statement.setInt(1, soglia);
			result = statement.executeQuery();
			while(result.next()){
				DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
				n = new News();
				n.setId(result.getString(1));
				n.setSource(result.getString(2));
				n.setCategory(result.getString(3));
				n.setUrl(result.getString(4));
				n.setTitle(result.getString(5));
				n.setDescription(result.getString(6));
				n.setNewscontent(result.getString(7));
				String text = df.format(result.getDate(8));
				n.setPublish_date(text);
				String text1 = df.format(result.getDate(9));
				n.setUpdate_date(text1);
				String text2 = df.format(result.getDate(10));
				n.setCrawl_date(text2);
				listNews.add(n);	
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
		return listNews;
	}

	public List<News> doRetrieveAll() throws PersistenceException {
		List<News> newsForPeriod=new LinkedList<News>();
		News n=null;
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "select * " +
					"from news";
			statement = connection.prepareStatement(retrieve);

			result = statement.executeQuery();
			while(result.next()){
				DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
				n = new News();
				n.setId(result.getString(1));
				n.setSource(result.getString(2));
				n.setCategory(result.getString(3));
				n.setUrl(result.getString(4));
				n.setTitle(result.getString(5));
				n.setDescription(result.getString(6));
				n.setNewscontent(result.getString(7));
				String text = df.format(result.getDate(8));
				n.setPublish_date(text);
				String text1 = df.format(result.getDate(9));
				n.setUpdate_date(text1);
				String text2 = df.format(result.getDate(10));
				n.setCrawl_date(text2);
				newsForPeriod.add(n);
			}
		}catch (SQLException e) {
			logger.error("errore durante il recupero degli url");
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
		return newsForPeriod;
	}

	public void delete(News news) throws PersistenceException {
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = ds.getConnection();
		PreparedStatement statement = null;
		try {
			String update = "DELETE FROM news " +
					"WHERE id=?";
			statement = connection.prepareStatement(update);
			statement.setString(1, news.getId());
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

	public void deleteAll() throws PersistenceException {
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = ds.getConnection();
		PreparedStatement statement = null;
		try {
			String update = "delete from news";
			statement = connection.prepareStatement(update);
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
	public News doRetrieveNewsByUrl(String url) throws PersistenceException {
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		News n = null;
		try {
			connection = ds.getConnection();
			String retrieve = "SELECT * " +
					"FROM news " +
					"WHERE url=?";
			statement = connection.prepareStatement(retrieve);
			statement.setString(1, url);
			result = statement.executeQuery();
			if(result.next()){
				DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				n = new News();
				n.setId(result.getString(1));
				n.setSource(result.getString(2));
				n.setCategory(result.getString(3));
				n.setUrl(result.getString(4));
				n.setTitle(result.getString(5));
				n.setDescription(result.getString(6));
				n.setNewscontent(result.getString(7));
				n.setPublish_date(String.valueOf(result.getDate(8)));
				n.setUpdate_date(String.valueOf(result.getDate(9)));
				n.setCrawl_date(String.valueOf(result.getDate(10)));
				//dovre mettere il type quando necessario
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
		return n;
	}

	@Override
	public void deleteByNewsid(String id) throws PersistenceException {
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = ds.getConnection();
		PreparedStatement statement = null;
		try {
			String update = "delete from news where id=?";
			statement = connection.prepareStatement(update);
			statement.setString(1, id);
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

	public List<News> doRetrieveNewsByUser(long user) throws PersistenceException {
		List<News> n= new LinkedList<News>();
		News m = null;
		String s1,s2,s3;
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		String newscontent;
		try {
			connection = ds.getConnection();
			String retrieve = "select news.id, title, description, newscontent from news, user_news where id_user=? and id=id_news";
			statement = connection.prepareStatement(retrieve);
			statement.setLong(1, user);
			result = statement.executeQuery();
			while(result.next()){
				m = new News();
				s1=result.getString(2);
				s2=result.getString(3);
				s3= result.getString(4);
				if(s1==null)
					s1="";
				if(s2==null)
					s2="";
				if(s3==null)
					s3="";
				m.setId(result.getString(1));
				m.setTitle(s1);
				m.setDescription(s2);
				m.setNewscontent(s3);
				n.add(m);
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
		return n;
	}

	@Override
	public List<News> doRetrieveNewsCompleteByUser(Long user,  List<Period> listaPeriodiTest) throws PersistenceException {
		List<News> n= new LinkedList<News>();
		News m = null;
		String s1, s2, s3;
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "select news.id, title, description, newscontent from news, user_news, tweets_sample where id_user=? and news.id=id_news and  tweets_sample.id=id_tweet "
					+ "and creationTime >= (Select startdate from period where id=?)";
			statement = connection.prepareStatement(retrieve);
			statement.setLong(1, user);
			statement.setInt(2, listaPeriodiTest.get(0).getIdPeriodo());
			result = statement.executeQuery();
			while(result.next()){
				m = new News();
				s1=result.getString(2);
				s2=result.getString(3);
				s3= result.getString(4);
				if(s1==null)
					s1="";
				if(s2==null)
					s2="";
				if(s3==null)
					s3="";
				m.setId(result.getString(1));
				m.setTitle(s1);
				m.setDescription(s2);
				m.setNewscontent(s3);
				n.add(m);
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
		return n;
	}

	@Override
	public List<News> doRetrieveNewsTrainingByUser(long iduser, List<Period> listaPeriodiTest) throws PersistenceException {
		List<News> n= new LinkedList<News>();
		News m = null;
		String text="";
		String s1, s2, s3;
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "select news.id, title, description, newscontent from news, user_news, tweets_sample where id_user=? and news.id=id_news and  tweets_sample.id=id_tweet "
					+ "and creationTime < (Select startdate from period where id=?)";
			statement = connection.prepareStatement(retrieve);
			statement.setLong(1, iduser);
			statement.setInt(2, listaPeriodiTest.get(0).getIdPeriodo());
			result = statement.executeQuery();
			while(result.next()){
				m = new News();
				s1=result.getString(2);
				s2=result.getString(3);
				s3= result.getString(4);
				if(s1==null)
					s1="";
				if(s2==null)
					s2="";
				if(s3==null)
					s3="";
				m.setId(result.getString(1));
				m.setTitle(s1);
				m.setDescription(s2);
				m.setNewscontent(s3);
				n.add(m);
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
		return n;
	}

}

