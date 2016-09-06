package org.rm3umf.persistenza;

import java.util.List;

import org.rm3umf.domain.News;
import org.rm3umf.domain.Signal;
import org.rm3umf.domain.User;
import org.rm3umf.domain.UserModel;


public interface SignalDAO {
	
	public void save(Signal signal) throws PersistenceException ;
	
	public void deleteAll(String c) throws PersistenceException ;
	
	public void delete(Signal signal) throws PersistenceException ;
	
	public List<Long> retriveUserid() throws PersistenceException;
	
	public List<Signal> doRetrieveByUserAndCriterio(User user, String criterio) throws PersistenceException ;

	public List<Signal> doRetrieveByUser(User user) throws PersistenceException ;
	
	public List<Signal> signalRetriveByNewsNotUser(User user, News news) throws PersistenceException;

	public void deleteAll4user(User u) throws PersistenceException;

	public List<Signal> signalRetriveByUser(User user) throws PersistenceException;

	public List<Long> retriveUseridWithCriterio(String criterio) throws PersistenceException;

}
