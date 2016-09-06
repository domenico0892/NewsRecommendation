package org.rm3umf.persistenza.postgreSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.rm3umf.persistenza.MessageEnrichmentDAO;
import org.rm3umf.persistenza.PersistenceException;

public class MessageEnrichmentDAOpostgreSQL implements MessageEnrichmentDAO {

	@Override
	public void saveMessEnr(int id_enr, String id_message, String type) throws PersistenceException {
		DataSourcePostgreSQL ds = DataSourcePostgreSQL.getInstance();
		Connection connection = ds.getConnection();
		PreparedStatement statement = null;
		try {			
			String insert = "insert into message_enrichment(id_enr, id_message, type) values (?,?,?)";
			statement = connection.prepareStatement(insert);
			statement.setInt(1, id_enr);
			statement.setString(2, id_message);
			statement.setString(3,type);
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
			String update = "delete from message_enrichment";
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
