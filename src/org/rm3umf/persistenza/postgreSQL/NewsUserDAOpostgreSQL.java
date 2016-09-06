package org.rm3umf.persistenza.postgreSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

import org.rm3umf.persistenza.NewsUserDao;
import org.rm3umf.persistenza.PersistenceException;

public class NewsUserDAOpostgreSQL implements NewsUserDao {

	@Override
	public void save(String id_news, long id_user, String id_tweet)
			throws PersistenceException, ParseException {
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = ds.getConnection();

		PreparedStatement statement = null;
		try {			
			String insert = "insert into user_news(id_news, id_user, id_tweet) values (?,?,?)";
			statement = connection.prepareStatement(insert);
			statement.setString(1, id_news);
			statement.setLong(2, id_user);
			statement.setString(3, id_tweet);
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
			String update = "delete from user_news";
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


}

