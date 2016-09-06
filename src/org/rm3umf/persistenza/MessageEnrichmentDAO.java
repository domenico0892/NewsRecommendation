package org.rm3umf.persistenza;

public interface MessageEnrichmentDAO {
	
	public void saveMessEnr(int id_enr, String id_message, String type) throws PersistenceException;

	public void deleteAll() throws PersistenceException;

}
