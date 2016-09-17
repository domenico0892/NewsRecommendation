package org.rm3umf.framework.importing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.rm3umf.domain.Message;
import org.rm3umf.domain.News;
import org.rm3umf.domain.User;
import org.rm3umf.persistenza.PersistenceException;
import org.rm3umf.persistenza.postgreSQL.DataSourcePostgreSQL;

import com.mysql.jdbc.jdbc2.optional.ConnectionWrapper;


/**
 * Questa classe implementa il dataset di umap 2011 
 * @author Giulz
 *
 */

public class DatasetUmap implements DatasetAdapter{
	
	private String nameDataset;
	private int chiamate = 0;
	private int MAX_CHIAMATE;
	private Connection connection = null;
	
	
	private static final Logger log = Logger.getLogger(DatasetUmap.class); 
	
	public String getMaxDate() throws PersistenceException {
		String maxDate=null;
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			try {
				connection = getConnection();
			} catch (DatasetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String retrieve = "SELECT max(creationTime) from tweets_sample";
			statement = connection.prepareStatement(retrieve);
			result = statement.executeQuery();
			if(result.next()){
			maxDate =result.getDate(1).toString();
			}
		}
		catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
			
		}
		/*finally {
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
		}*/
		return maxDate;
	}
	
	public News doRetrieveNewsByUrl(String url) throws PersistenceException {
		//DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		News n = null;
		try {
			try {
				connection = getConnection();
			} catch (DatasetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
				n.setPublish_date(result.getDate(8));
				n.setUpdate_date(result.getDate(9));
				n.setCrawl_date(result.getDate(10));
				//dovre mettere il type quando necessario
			}
		}catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		}
		/*finally {
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
		}*/
		return n;
	}
	
	public String retriveJsonTextFromId(String id) throws PersistenceException {
		//DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		String json_str = null;
		try {
			try {
				connection = getConnection();
			} catch (DatasetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println("id json:" + id);
			String retrieve = "select json from tweets_sample where id=?";
			statement = connection.prepareStatement(retrieve);
			statement.setString(1, id);
			result = statement.executeQuery();
			if (result.next()) {
				json_str = result.getString(1);
				//System.out.println(json_str);
			}
		}
		catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		}
		/*finally {
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
		}*/
		return json_str;
	}
	
	public List<User> getObject() throws DatasetException{
		log.info("recupero utenti dal Dataset");
		List<User> listaUser=new ArrayList<User>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = getConnection();
			String retrieve = "select distinct userId  from tweets_sample";
			statement = connection.prepareStatement(retrieve);
			result = statement.executeQuery();
			result.last();
			MAX_CHIAMATE = result.getRow();
			result.beforeFirst();
			while(result.next()){
				int userId=result.getInt(1);
				//effettuo un'altra query per recuperare gli username
				List<String> usernames=getUsernameByUserid(userId);
				log.info("recuperato dal dataset utente "+userId);
				User u= new User();
				u.setIduser(userId);
				//setto gli username
				u.setUsernames(usernames);
				//aggiungo l'utente alla lista
				listaUser.add(u);
				}
			
			log.info("fine recupero utenti da Dataset");
		}
		catch (SQLException e) {
			throw new DatasetException(e.getMessage());
		}catch (DatasetException e) {
			throw new DatasetException(e.getMessage());
		}
		/*finally {
			try {
				if (result != null)
					result.close();
				if (statement != null) 
					statement.close();
				if (connection!= null)
					connection.close();
			} catch (SQLException e) {
				throw new DatasetException(e.getMessage());
			}
		}*/
		return listaUser;
	}


	public List<Message> getMessagesByUser(User user) throws DatasetException{
		log.info("recupero dal dataset i messaggi dell'utente "+user.getIduser());
		Message message = null;
		List<Message> listaMessages = new ArrayList<Message>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = getConnection();
			String retrieve = "select id as idmessage,content,creationTime from tweets_sample where userId=?";
			statement = connection.prepareStatement(retrieve);
			statement.setLong(1, user.getIduser());
			result = statement.executeQuery();
			while (result.next()) {
				message = new Message();
				message.setIdMessage(result.getString(1));
				message.setText(result.getString(2));
				//devo aggiungere il campo data
				message.setDate(result.getDate(3).toString());
				message.setUser(user);
				listaMessages.add(message);
			}
		}
		catch (SQLException e) {
			log.error("errore durante il recupero dei messaggi dal dataset");
			throw new DatasetException(e.getMessage());
		}
		
		/*finally {
			try {
				if (result != null)
					result.close();
				if (statement != null) 
					statement.close();
				if (connection!= null)
					connection.close();
			} catch (SQLException e) {
				throw new DatasetException(e.getMessage());
			}
		}*/
		return listaMessages;
	}

	
	public List<String> getUsernameByUserid(int userid) throws DatasetException{
		
		List<String> listaUsername=new ArrayList<String>();
		PreparedStatement statement = null;
		ResultSet result = null;
		Connection connection=null;
		try {
			connection=getConnection();
			String retrieve = "select distinct username from tweets_sample where userId=?";
			statement = connection.prepareStatement(retrieve);
			statement.setInt(1, userid);
			result = statement.executeQuery();
			while(result.next()){
				String username=result.getString(1);
				listaUsername.add(username);
			}
		}
		catch (SQLException e) {
			log.error("errore durante il recupero degli username dell'utente "+userid);
			throw new DatasetException(e.getMessage());
		}
		/*finally {
			try {
				if (result != null)
					result.close();
				if (statement != null) 
					statement.close();
				if (connection!= null)
					connection.close();
			} catch (SQLException e) {
				throw new DatasetException(e.getMessage());
			}
		}*/
		return listaUsername;
	}



	/**
	 * Da la connessione verso il database da cui si voglio importare i dati
	 * @return
	 * @throws DatasetException
	 */
	private Connection getConnection() throws DatasetException {
		
//		this.chiamate++;
//		if (this.chiamate == MAX_CHIAMATE || this.connection == null) {
//			if (this.connection != null) {
//				try {
//					this.connection.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
		if (connection == null) {
		String driver = "com.mysql.jdbc.Driver";
		String dbURI = "jdbc:mysql://localhost/twitter";
		String userName = "root";
		String password = "ai-lab";

		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(dbURI,userName, password);
			System.err.println("Ottenuta nuova connessione");
		} catch (ClassNotFoundException e) {
			throw new DatasetException(e.getMessage());
		} catch(SQLException e) {
			throw new DatasetException(e.getMessage());
		}
		catch(Exception e) {
			throw new DatasetException(e.getMessage());
		}
		}		
		return connection;
	}
}
