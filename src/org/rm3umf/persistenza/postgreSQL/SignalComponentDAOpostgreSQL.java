package org.rm3umf.persistenza.postgreSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rm3umf.domain.Enrichment;
import org.rm3umf.domain.Period;
import org.rm3umf.domain.Signal;
import org.rm3umf.domain.SignalComponent;
import org.rm3umf.domain.User;
import org.rm3umf.persistenza.DataSource;
import org.rm3umf.persistenza.PeriodDAO;
import org.rm3umf.persistenza.PersistenceException;
import org.rm3umf.persistenza.SignalComponentDAO;





public class SignalComponentDAOpostgreSQL implements SignalComponentDAO{


	public void save(SignalComponent sigComp) throws PersistenceException {
		DataSource ds = DataSourcePostgreSQL.getInstance();
		Connection connection = ds.getConnection();
//		if (this.doRetrieveByKey(sigComp.getUser(),sigComp.getPeriod(),sigComp.getEnrichment()) !=  null) {
//			this.doUpdate(sigComp, connection);
//		}
//		else
			this.doInsert(sigComp, connection);
	}

	private void doInsert(SignalComponent sigComp, Connection connection) throws PersistenceException {
		PreparedStatement statement = null;
		try {			
			String insert = "INSERT INTO signalcomponent(userid,enrichmentid,periodid,tf,idf,tfidf,value, type) VALUES (?,?,?,?,?,?,?,?)";
			statement = connection.prepareStatement(insert);
			statement.setLong(1, sigComp.getUser().getIduser());
			statement.setString(2,sigComp.getEnrichment().getName());
			statement.setInt(3,sigComp.getPeriod().getIdPeriodo());
			statement.setDouble(4,sigComp.getTf());
			statement.setDouble(5,sigComp.getIdf());
			statement.setDouble(6, sigComp.getTfidf());
			statement.setInt(7,sigComp.getOccorence());
			statement.setString(8, sigComp.getType());
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



	private void doUpdate(SignalComponent sigComp, Connection connection) throws PersistenceException {
		PreparedStatement statement = null;
		try {
			String update = "UPDATE signalcomponent SET tf=?,idf=?,tfidf=?,value=? " +
					"WHERE userid=? AND periodid=? AND enrichmentid=?";
			statement = connection.prepareStatement(update);
			statement.setDouble(1, sigComp.getTf());
			statement.setDouble(2, sigComp.getIdf());
			statement.setDouble(3, sigComp.getTfidf());
			statement.setInt(4,sigComp.getOccorence());
			statement.setLong(5, sigComp.getUser().getIduser());
			statement.setInt(6, sigComp.getPeriod().getIdPeriodo());
			statement.setString(7, sigComp.getEnrichment().getName());
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

	/**
	 * Questo metodo restituisce tutti i SignalComponent presenti all'interno del periodo
	 * identificato da periodid
	 */

	public List<SignalComponent> doRetrieveByPeriodid(int periodid) throws PersistenceException {
		List<SignalComponent> listaSignalComp = new ArrayList<SignalComponent>();
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "SELECT  userid , enrichmentid, type " +
					"FROM signalcomponent " +
					"WHERE periodid=? ";
			statement = connection.prepareStatement(retrieve);
			statement.setInt(1, periodid);
			result = statement.executeQuery();
			while (result.next()) {
				SignalComponent signalComp =new SignalComponent();
				//USER
				User user = new UserProxy();
				user.setIduser(result.getInt(1));//userid
				signalComp.setUser(user);
				//PERIOD
				Period period = new PeriodProxy();
				period.setIdPeriodo(periodid);
				//Enrichment
				Enrichment n=new EnrichmentProxy();
				n.setName(result.getString(2)); //recupero il enrichmentid
				signalComp.setEnrichment(n);
				signalComp.setType(result.getString(3));

				//aggiungo il signalComp alla lista
				listaSignalComp.add(signalComp);
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
		return listaSignalComp;
	}


	public List<SignalComponent> doRetrieveByUseridAndPeriod(long userid, int start, int end) throws PersistenceException {
		List<SignalComponent> listaSignalComp = new ArrayList<SignalComponent>();
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "select idf,tf, tfidf,value,periodid,enrichmentid,type from signalcomponent where userid=? AND periodid >=? AND periodid<?";

			statement = connection.prepareStatement(retrieve);
			statement.setLong(1, userid);
			statement.setInt(2, start);
			statement.setInt(3, end);
			result = statement.executeQuery();
			while (result.next()) {
				SignalComponent signalComponent =new SignalComponent();
				signalComponent.setIdf(result.getFloat(1));
				signalComponent.setTf(result.getFloat(2));
				signalComponent.setTfidf(result.getDouble(3));
				signalComponent.setOccorence(result.getInt(4));

				//Periodo
				int idPeriod=result.getInt(5);
				Period period=new PeriodProxy();
				period.setIdPeriodo(idPeriod);
				signalComponent.setPeriod(period);
				//Enrichment
				String idEnrichment=result.getString(6);
				Enrichment enrichment = new EnrichmentProxy();
				enrichment.setName(idEnrichment);
				signalComponent.setEnrichment(enrichment);
				signalComponent.setType(result.getString(7));


				listaSignalComp.add(signalComponent);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
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
		return listaSignalComp;
	}

	public List<SignalComponent>  doRetrieveByEnrichmentid(String enrichmentid) throws PersistenceException {
		List<SignalComponent> listaSignalComp = new ArrayList<SignalComponent>();
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "SELECT idf,tf,periodid,value, type " +
					"FROM signalcomponent " +
					"WHERE enrichmentid=?";

			statement = connection.prepareStatement(retrieve);
			statement.setString(1, enrichmentid);
			result = statement.executeQuery();
			while (result.next()) {

				SignalComponent signalComponent =new SignalComponent();
				signalComponent.setIdf(result.getFloat(1));
				signalComponent.setTf(result.getFloat(2));
				//signalComponent.setOccorence(result.getInt(3));
				//Periodo
				int idPeriod=result.getInt(3);
				Period period=new PeriodProxy();
				period.setIdPeriodo(idPeriod);
				signalComponent.setPeriod(period);
				signalComponent.setOccorence(result.getInt(4));
				signalComponent.setType(result.getString(5));

				//Enrichment
				String idEnrichment=result.getString(4);
				Enrichment enrichment = new EnrichmentProxy();
				enrichment.setName(idEnrichment);
				signalComponent.setEnrichment(enrichment);


				listaSignalComp.add(signalComponent);
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
		return listaSignalComp;
	}


	/**
	 * Restituisce in base al user period e enrichment il singnalComponent identificato univocamente
	 * @param user
	 * @param period
	 * @param enrichment
	 * @return signalComponent - il signal componente identificato univocamente
	 * @throws PersistenceException
	 */
	public SignalComponent doRetrieveByKey(User user,Period period, Enrichment enrichment) throws PersistenceException {

		SignalComponent signalComponent=null;
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "select idf,tf,periodid,value, type from signalcomponent where userid=? and enrichmentid=? and periodid=?";
			statement = connection.prepareStatement(retrieve);
			statement.setLong(1, user.getIduser());
			statement.setString(2, enrichment.getName());
			statement.setInt(3,period.getIdPeriodo() );
			result = statement.executeQuery();
			if(result.next()) {
				signalComponent =new SignalComponent();
				signalComponent.setPeriod(period);
				signalComponent.setEnrichment(enrichment);
				signalComponent.setUser(user);
				signalComponent.setIdf(result.getFloat("idf"));
				signalComponent.setTf(result.getFloat("tf"));
				signalComponent.setOccorence(result.getInt("value"));
				signalComponent.setType(result.getString("type"));
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
		return signalComponent;
	}

	/**
	 * DELETE
	 */

	/**
	 * Cancella un singolo SignalComponent 
	 */
	public void delete(SignalComponent signalComponent)	throws PersistenceException {
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "DELETE FROM signalcomponent where userid=? and enrichmentid=? and periodid=?";
			statement = connection.prepareStatement(retrieve);
			statement.setLong(1, signalComponent.getUser().getIduser());
			statement.setString(2,signalComponent.getEnrichment().getName());
			statement.setInt(3,signalComponent.getPeriod().getIdPeriodo() );
			statement.executeUpdate();
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
	}

	public void deleteByEnrichmentid(String enrichmentid, String type)	throws PersistenceException {
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "DELETE FROM signalcomponent " +
					"WHERE enrichmentid=? AND type=?";
			statement = connection.prepareStatement(retrieve);
			statement.setString(1,enrichmentid );
			statement.setString(2, type);
			statement.executeUpdate();
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
	}

	public void deleteAll(String c) throws PersistenceException {
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = ds.getConnection();
		PreparedStatement statement = null;
		try {
			String update = "delete from signalcomponent where type=?";
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



	//aggiunto da Sirian
	public SignalComponent doRetrieveByUserEnrichment(User user, Enrichment enrichment) throws PersistenceException {

		SignalComponent signalComponent=null;
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "select tfidf from signalcomponent where userid=? and enrichmentid=?";
			statement = connection.prepareStatement(retrieve);
			statement.setLong(1, user.getIduser());
			statement.setString(2, enrichment.getName());
			result = statement.executeQuery();
			if(result.next()) {
				signalComponent =new SignalComponent();
				signalComponent.setEnrichment(enrichment);
				signalComponent.setUser(user);
				signalComponent.setIdf(result.getFloat("tfidf"));
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
		return signalComponent;
	}

	@Override
	public void deleteAll4User(User u) throws PersistenceException {
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = ds.getConnection();
		PreparedStatement statement = null;
		try {
			String update = "delete from signalcomponent where userid=?";
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
	public List<SignalComponent> doRetrieveByUserid(long iduser, String s, int periodo) throws PersistenceException {
		List<SignalComponent> listaSignalComp = new ArrayList<SignalComponent>();
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "select idf,tf, tfidf,value,periodid,enrichmentid,type from signalcomponent where userid=? and type=? and periodid<=?";
			statement = connection.prepareStatement(retrieve);
			statement.setLong(1, iduser);
			statement.setString(2, s);
			statement.setInt(3, periodo);
			result = statement.executeQuery();
			while (result.next()) {
				SignalComponent signalComponent =new SignalComponent();
				signalComponent.setIdf(result.getFloat(1));
				signalComponent.setTf(result.getFloat(2));
				signalComponent.setTfidf(result.getDouble(3));
				signalComponent.setOccorence(result.getInt(4));

				//Periodo
				int idPeriod=result.getInt(5);
				Period period=new PeriodProxy();
				period.setIdPeriodo(idPeriod);
				signalComponent.setPeriod(period);
				//Enrichment
				String idEnrichment=result.getString(6);
				Enrichment enrichment = new EnrichmentProxy();
				enrichment.setName(idEnrichment);
				signalComponent.setEnrichment(enrichment);
				signalComponent.setType(result.getString(7));


				listaSignalComp.add(signalComponent);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
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
		return listaSignalComp;
	}

	@Override
	public Map<String, Long> doRetrieveByUseridReturnMap(long iduser, String s) throws PersistenceException {
		Map<String, Long> listaSignalComp = new HashMap<String, Long>();
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			connection = ds.getConnection();
			String retrieve = "select enrichmentid, tfidf from signalcomponent where userid=? and type=?";
			statement = connection.prepareStatement(retrieve);
			statement.setLong(1, iduser);
			statement.setString(2, s);
			result = statement.executeQuery();
			while (result.next()) {
				listaSignalComp.put(result.getString(1), result.getLong(2));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
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
		return listaSignalComp;
	}
}

