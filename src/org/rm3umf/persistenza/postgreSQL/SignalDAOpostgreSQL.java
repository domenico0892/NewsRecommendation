package org.rm3umf.persistenza.postgreSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import util.Cronometro;

import java.sql.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.rm3umf.domain.Enrichment;
import org.rm3umf.domain.News;
import org.rm3umf.domain.Signal;
import org.rm3umf.domain.User;
import org.rm3umf.domain.UserModel;
import org.rm3umf.persistenza.NewsDAO;
import org.rm3umf.persistenza.PersistenceException;
import org.rm3umf.persistenza.SignalDAO;

import com.mysql.jdbc.Blob;

public class SignalDAOpostgreSQL implements SignalDAO{

	private NewsDAO newsDAO=new NewsDAOpostgreSQL();

	public void save(Signal signal) throws PersistenceException {
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = ds.getConnection();

		PreparedStatement statement = null;
		try {			
			String insert = "insert into new_news_recommendation.signal(enrichmentid,userid,`signal`,type) values (?,?,?,?)";
			statement = connection.prepareStatement(insert);
			System.out.println(signal.getEnrichment().getName());
			statement.setString(1, signal.getEnrichment().getName());
			statement.setLong(2, signal.getUser().getIduser());
			double[] arrayFloat=signal.getSignal();
			String arr = arrayToString(arrayFloat);
			System.out.println(arr);
			statement.setString(3,arr);
			statement.setString(4, signal.getType());
			statement.executeUpdate();		
		}
		catch (SQLException e) {
			e.printStackTrace();
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


	public static String arrayToString(double[] arrayFloat) {
		String separator = ",";

		StringBuilder result = new StringBuilder();
		if (arrayFloat.length > 0) {
			result.append(arrayFloat[0]);
			for (int i=1; i<arrayFloat.length; i++) {
				result.append(separator);
				result.append(arrayFloat[i]);
			}
		}
		return result.toString();
	}



	/**
	 * Cancella la relazione signal
	 */

	public void deleteAll(String c) throws PersistenceException {
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = ds.getConnection();
		PreparedStatement statement = null;
		try {
			String update = "delete from new_news_recommendation.signal where type=? ";
			statement = connection.prepareStatement(update);
			statement.setString(1, c);

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



	public List<Signal> doRetrieveByUserAndCriterio(User user, String criterio) throws PersistenceException {
		Signal signal=null;
		List<Signal> signalUser=new LinkedList<Signal>();
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "select enrichmentid,`signal` from new_news_recommendation.`signal` where userid=? and type = ?";
			statement = connection.prepareStatement(retrieve);
			statement.setLong(1, user.getIduser());
			statement.setString(2, criterio);
			result = statement.executeQuery();
			while (result.next()) {
				//System.out.println("creo oggetti signal:"+Cronometro.getInstance());
				signal = new Signal();
				//Setto il news
				Enrichment en=new EnrichmentProxy();
				en.setName(result.getString(1));
				signal.setEnrichment(en);
				//setto l'utente
				signal.setUser(user);

				//XXX vorrei trovare un modon po pi� efficiente ma non so come fare

				String arraySignal = result.getString(2);

				String[] signalSplit = arraySignal.split(",");

				double arrayFloat[] = new double[signalSplit.length];

				for(int i=0; i<signalSplit.length; i++){

					arrayFloat[i] = Double.valueOf(signalSplit[i]);



				}


				// ARRU STYLE

				//				Array array=result.getArray(2);
				//				ResultSet rs=array.getResultSet();
				//				List<Float> list= new LinkedList<Float>(); 
				//				while(rs.next()){
				//					list.add(rs.getFloat(2));
				//				}
				//				double arrayFloat[]=new double[list.size()];
				//
				//				for(int i=0; i<list.size();i++){
				//					arrayFloat[i]=list.get(i);
				//				}


				signal.setSignal(arrayFloat);
				signalUser.add(signal);
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
		return signalUser;
	}
	
	public List<Signal> doRetrieveByUser(User user) throws PersistenceException {
		Signal signal=null;
		List<Signal> signalUser=new LinkedList<Signal>();
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "select enrichmentid,`signal` from new_news_recommendation.`signal` where userid=?";
			statement = connection.prepareStatement(retrieve);
			statement.setLong(1, user.getIduser());
			result = statement.executeQuery();
			while (result.next()) {
				//System.out.println("creo oggetti signal:"+Cronometro.getInstance());
				signal = new Signal();
				//Setto il news
				Enrichment en=new EnrichmentProxy();
				en.setName(result.getString(1));
				signal.setEnrichment(en);
				//setto l'utente
				signal.setUser(user);

				//XXX vorrei trovare un modon po pi� efficiente ma non so come fare

				String arraySignal = result.getString(2);

				String[] signalSplit = arraySignal.split(",");

				double arrayFloat[] = new double[signalSplit.length];

				for(int i=0; i<signalSplit.length; i++){

					arrayFloat[i] = Double.valueOf(signalSplit[i]);



				}


				// ARRU STYLE

				//				Array array=result.getArray(2);
				//				ResultSet rs=array.getResultSet();
				//				List<Float> list= new LinkedList<Float>(); 
				//				while(rs.next()){
				//					list.add(rs.getFloat(2));
				//				}
				//				double arrayFloat[]=new double[list.size()];
				//
				//				for(int i=0; i<list.size();i++){
				//					arrayFloat[i]=list.get(i);
				//				}


				signal.setSignal(arrayFloat);
				signalUser.add(signal);
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
		return signalUser;
	}

	@Override
	public void delete(Signal signal) throws PersistenceException {
		//Cronometro.getInstance().avanza();

		//Identifica il sengnale da cancellare
		long userid = signal.getUser().getIduser();
		String en_id=signal.getEnrichment().getName();

		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "DELETE FROM new_news_recommendation.signal " +
					"WHERE userid=? and newsid=?";
			statement = connection.prepareStatement(retrieve);
			statement.setLong(1, userid);
			statement.setString(2, en_id);
			statement.executeUpdate();
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
	}


	public List<Long> retriveUserid() throws PersistenceException {
		List<Long> users=new LinkedList<Long>();
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "SELECT distinct( userid ) " +
					"FROM new_news_recommendation.signal";
			
			statement = connection.prepareStatement(retrieve);
			result = statement.executeQuery();

			while (result.next()) {
				Long userid  = result.getLong(1);
				users.add(userid);
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
		return users;
	}

	@Override
	public List<Signal> signalRetriveByNewsNotUser(User user, News news) throws PersistenceException {
		Signal signal=null;
		List<Signal> signalUser=new LinkedList<Signal>();
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "select * from new_news_recommendation.`signal` where userid!=? AND newsid=?";
			statement = connection.prepareStatement(retrieve);
			statement.setLong(1, user.getIduser());
			statement.setString(2, news.getId());
			result = statement.executeQuery();
			while (result.next()) {
				//System.out.println("creo oggetti signal:"+Cronometro.getInstance());
				signal = new Signal();
				//Setto il news
				Enrichment en1=new EnrichmentProxy();
				en1.setName(result.getString(2));
				signal.setEnrichment(en1);
				//setto l'utente
				User u = new User();
				u.setIduser(Long.parseLong(result.getString(1)));
				signal.setUser(u);

				//XXX vorrei trovare un modon po pi� efficiente ma non so come fare

				String arraySignal = result.getString(3);
				String[] signalSplit = arraySignal.split(",");

				double arrayFloat[] = new double[signalSplit.length];
				for(int i=0; i<signalSplit.length; i++){

					arrayFloat[i] = Double.valueOf(signalSplit[i]);



				}


				// ARRU STYLE

				//				Array array=result.getArray(2);
				//				ResultSet rs=array.getResultSet();
				//				List<Float> list= new LinkedList<Float>(); 
				//				while(rs.next()){
				//					list.add(rs.getFloat(2));
				//				}
				//				double arrayFloat[]=new double[list.size()];
				//
				//				for(int i=0; i<list.size();i++){
				//					arrayFloat[i]=list.get(i);
				//				}


				signal.setSignal(arrayFloat);
				signalUser.add(signal);
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
		return signalUser;
	}


	@Override
	public void deleteAll4user(User u) throws PersistenceException {
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = ds.getConnection();
		PreparedStatement statement = null;
		try {
			String update = "delete from new_news_recommendation.signal where userid=? ";
			statement = connection.prepareStatement(update);
			statement.setLong(1, u.getIduser());
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
	public List<Signal> signalRetriveByUser(User user) throws PersistenceException {
		Signal signal=null;
		List<Signal> signalUser=new LinkedList<Signal>();
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "select * from new_news_recommendation.`signal` where userid=?";
			statement = connection.prepareStatement(retrieve);
			statement.setLong(1, user.getIduser());
			result = statement.executeQuery();
			while (result.next()) {
				//System.out.println("creo oggetti signal:"+Cronometro.getInstance());
				signal = new Signal();
				//Setto il news
				Enrichment en1=new EnrichmentProxy();
				en1.setName(result.getString(2));
				signal.setEnrichment(en1);
				//setto l'utente
				User u = new User();
				u.setIduser(Long.parseLong(result.getString(1)));
				signal.setUser(u);

				//XXX vorrei trovare un modon po pi� efficiente ma non so come fare

				String arraySignal = result.getString(3);
				String[] signalSplit = arraySignal.split(",");

				double arrayFloat[] = new double[signalSplit.length];
				for(int i=0; i<signalSplit.length; i++){

					arrayFloat[i] = Double.valueOf(signalSplit[i]);

				}


				// ARRU STYLE

				//				Array array=result.getArray(2);
				//				ResultSet rs=array.getResultSet();
				//				List<Float> list= new LinkedList<Float>(); 
				//				while(rs.next()){
				//					list.add(rs.getFloat(2));
				//				}
				//				double arrayFloat[]=new double[list.size()];
				//
				//				for(int i=0; i<list.size();i++){
				//					arrayFloat[i]=list.get(i);
				//				}


				signal.setSignal(arrayFloat);
				signalUser.add(signal);
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
		return signalUser;
	}



	@Override
	public List<Long> retriveUseridWithCriterio(String criterio)
			throws PersistenceException {
		List<Long> users=new LinkedList<Long>();
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "SELECT distinct( userid ) " +
					"FROM new_news_recommendation.signal where type=?";
			
			statement = connection.prepareStatement(retrieve);
			statement.setString(1, criterio);
			result = statement.executeQuery();

			while (result.next()) {
				Long userid  = result.getLong(1);
				users.add(userid);
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
		return users;
	}

}


