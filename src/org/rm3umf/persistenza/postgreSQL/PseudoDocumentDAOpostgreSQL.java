package org.rm3umf.persistenza.postgreSQL;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import org.rm3umf.domain.Message;
import org.rm3umf.domain.Period;
import org.rm3umf.domain.PseudoFragment;
import org.rm3umf.domain.User;
import org.rm3umf.persistenza.DataSource;
import org.rm3umf.persistenza.MessageDAO;
import org.rm3umf.persistenza.PersistenceException;
import org.rm3umf.persistenza.PseudoDocumentDAO;


public class PseudoDocumentDAOpostgreSQL implements PseudoDocumentDAO{

	private MessageDAO messageDAO;

	public PseudoDocumentDAOpostgreSQL(){
		this.messageDAO=new MessageDAOpostgreSQL();
	}


	public PseudoFragment doRetrieve(User user, Period period) throws PersistenceException {
		PseudoFragment pseudo=new PseudoFragment();
		pseudo.setUser(user);
		pseudo.setPeriod(period);
		//effettuo la query 
		List<Message> messages=messageDAO.doRetrieveByUserIdAndDate(user,period);
		pseudo.setMessages(messages);
		return pseudo;
	}

	public List<PseudoFragment> doRetriveByPeriod(Period period) throws PersistenceException {
		String dataInizio=period.getDataInizioPeriodo();
		String dataFine=period.getDataFinePeriodo();
		List<PseudoFragment> listaPseudo = new LinkedList<PseudoFragment>();
		DataSource ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "SELECT distinct (userId) " +
					"FROM tweets_sample AS m " +
					"WHERE m.creationTime>=? and m.creationTime<=?";
			statement = connection.prepareStatement(retrieve);
			statement.setDate(1, Date.valueOf(dataInizio));
			statement.setDate(2, Date.valueOf(dataFine));
			result = statement.executeQuery();
			while (result.next()) {
				UserProxy user= new UserProxy();
				user.setIduser(result.getInt(1));
				PseudoFragment pseudo = new PseudoDocumentProxy();
				pseudo.setUser(user);
				pseudo.setPeriod(period);
				listaPseudo.add(pseudo);
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
		return listaPseudo;
	}


	@Override
	public List<PseudoFragment> doRetriveByPeriodUser(Period period, User u) throws PersistenceException {
		String dataInizio=period.getDataInizioPeriodo();
		String dataFine=period.getDataFinePeriodo();
		List<PseudoFragment> listaPseudo = new LinkedList<PseudoFragment>();
		DataSource ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "SELECT distinct (userId) " +
					"FROM tweets_sample AS m " +
					"WHERE m.creationTime>=? and m.creationTime<=? and m.userId=?";
			statement = connection.prepareStatement(retrieve);
			statement.setTimestamp(1, Timestamp.valueOf(dataInizio));
			statement.setTimestamp(2, Timestamp.valueOf(dataFine));
			statement.setLong(3, u.getIduser());
			result = statement.executeQuery();
			while (result.next()) {
				UserProxy user= new UserProxy();
				user.setIduser(result.getInt(1));
				PseudoFragment pseudo = new PseudoDocumentProxy();
				pseudo.setUser(user);
				pseudo.setPeriod(period);
				listaPseudo.add(pseudo);
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
		return listaPseudo;
	}



}
