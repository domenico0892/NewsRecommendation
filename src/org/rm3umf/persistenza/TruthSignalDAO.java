package org.rm3umf.persistenza;

import java.util.List;

import org.rm3umf.domain.News;
import org.rm3umf.domain.TruthSignal;
import org.rm3umf.domain.User;
import org.rm3umf.domain.UserModel;

public interface TruthSignalDAO {

	public void save(TruthSignal signal) throws PersistenceException ;
	
	public void deleteAll(String c) throws PersistenceException ;
	
	public void delete(TruthSignal signal) throws PersistenceException ;
	
	public List<UserModel> doRetrieveByCriterio(String criterio) throws PersistenceException ;
		
	public TruthSignal doRetrieveByUserAndCriterio(User user, String criterio) throws PersistenceException ;

	public List<TruthSignal> doRetrieveByUser(User user) throws PersistenceException ;
	
	public void delete4user(User u) throws PersistenceException;
}
