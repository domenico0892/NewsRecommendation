package org.rm3umf.persistenza.postgreSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.rm3umf.domain.Enrichment;
import org.rm3umf.domain.News;
import org.rm3umf.domain.Signal;
import org.rm3umf.domain.TruthSignal;
import org.rm3umf.domain.User;
import org.rm3umf.domain.UserModel;
import org.rm3umf.persistenza.PersistenceException;
import org.rm3umf.persistenza.TruthSignalDAO;

public class TruthSignalDAOpostgreSQL implements TruthSignalDAO{

	@Override
	public void save(TruthSignal signal) throws PersistenceException {
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = ds.getConnection();

		PreparedStatement statement = null;
		try {			
			String insert = "insert into new_news_recommendation.truthsignal(userid,`signal`,type) values (?,?,?)";
			statement = connection.prepareStatement(insert);
			statement.setLong(1, signal.getUser().getIduser());
			double[] arrayFloat=signal.getSignal();
			String arr = arrayToString(arrayFloat);
			statement.setString(2,arr);
			statement.setString(3, signal.getType());
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

	@Override
	public void deleteAll(String c) throws PersistenceException {
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = ds.getConnection();
		PreparedStatement statement = null;
		try {
			String update = "delete from new_news_recommendation.truthsignal where type=?";
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

	@Override
	public void delete(TruthSignal signal) throws PersistenceException {
		long userid = signal.getUser().getIduser();

		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "DELETE FROM new_news_recommendation.truthsignal " +
					"WHERE userid=?";
			statement = connection.prepareStatement(retrieve);
			statement.setLong(1, userid);
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

	@Override
	public List<UserModel> doRetrieveByCriterio(String criterio) throws PersistenceException {
		TruthSignal signal=null;
		UserModel um = null;
		List<UserModel> signalUser=new LinkedList<UserModel>();
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "select id, userid, `signal` from new_news_recommendation.`truthsignal` where type=?";
			statement = connection.prepareStatement(retrieve);
			statement.setString(1, criterio);
			result = statement.executeQuery();
			while (result.next()) {
				signal = new TruthSignal();
				User u = new User();
				signal.setId(result.getInt(1));
				u.setIduser(result.getLong(2));
				signal.setUser(u);
				String arraySignal = result.getString(3);
				String[] signalSplit = arraySignal.split(",");
				double arrayFloat[] = new double[signalSplit.length];
				for(int i=0; i<signalSplit.length; i++){
					arrayFloat[i] = Double.valueOf(signalSplit[i]);
				}
				signal.setSignal(arrayFloat);
				signal.setType(criterio);
				um = new UserModel(u, signal);
				signalUser.add(um);
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
	public TruthSignal doRetrieveByUserAndCriterio(User user, String criterio) throws PersistenceException {
		TruthSignal signal=null;
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "select id, `signal` from new_news_recommendation.`truthsignal` where type=? AND userid=?";
			statement = connection.prepareStatement(retrieve);
			statement.setString(1, criterio);
			statement.setLong(2, user.getIduser());
			result = statement.executeQuery();
			if (result.next()) {
				signal = new TruthSignal();
				User u = new User();
				signal.setId(result.getInt(1));
				u.setIduser(user.getIduser());
				signal.setUser(u);
				String arraySignal = result.getString(2);
				String[] signalSplit = arraySignal.split(",");
				double arrayFloat[] = new double[signalSplit.length];
				for(int i=0; i<signalSplit.length; i++){
					arrayFloat[i] = Double.valueOf(signalSplit[i]);
				}
				signal.setSignal(arrayFloat);
				signal.setType(criterio);
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
		return signal;
	}

	@Override
	public List<TruthSignal> doRetrieveByUser(User user)throws PersistenceException {
		TruthSignal signal=null;
		List<TruthSignal> signalUser=new LinkedList<TruthSignal>();
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "select id, `signal`, type from new_news_recommendation.`truthsignal` where userid=?";
			statement = connection.prepareStatement(retrieve);
			statement.setLong(1, user.getIduser());
			result = statement.executeQuery();
			while (result.next()) {
				signal = new TruthSignal();
				User u = new User();
				signal.setId(result.getInt(1));
				u.setIduser(user.getIduser());
				signal.setUser(u);
				String arraySignal = result.getString(2);
				String[] signalSplit = arraySignal.split(",");
				double arrayFloat[] = new double[signalSplit.length];
				for(int i=0; i<signalSplit.length; i++){
					arrayFloat[i] = Double.valueOf(signalSplit[i]);
				}
				signal.setSignal(arrayFloat);
				signal.setType(result.getString(3));
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
	public void delete4user(User u) throws PersistenceException {
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = ds.getConnection();
		PreparedStatement statement = null;
		try {
			String update = "delete from new_news_recommendation.truthsignal where userid=? ";
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

}
