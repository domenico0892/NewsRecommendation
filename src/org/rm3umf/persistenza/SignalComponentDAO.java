package org.rm3umf.persistenza;

import java.util.List;
import java.util.Map;

import org.rm3umf.domain.Enrichment;
import org.rm3umf.domain.Signal;
import org.rm3umf.domain.SignalComponent;
import org.rm3umf.domain.User;


public interface SignalComponentDAO {
	
	/*SAVE*/
	public void save(SignalComponent sigComp) throws PersistenceException ;
	
	/*DELETE*/
	public void deleteAll(String c)throws PersistenceException;
	
	public void delete(SignalComponent signalComponent)throws PersistenceException;
	
	public void deleteByEnrichmentid(String enrichmentid, String type) throws PersistenceException;
	
	/*RETRIVE*/
	public List<SignalComponent> doRetrieveByPeriodid(int periodid) throws PersistenceException;
	
	public List<SignalComponent> doRetrieveByUseridAndPeriod(long userid, int start, int end) throws PersistenceException;
	
	public List<SignalComponent> doRetrieveByEnrichmentid(String enrichmentid) throws PersistenceException;

	public void deleteAll4User(User u) throws PersistenceException;

	public List<SignalComponent> doRetrieveByUserid(long iduser, String s, int periodo) throws PersistenceException;

	public Map<String, Long> doRetrieveByUseridReturnMap(long iduser, String s) throws PersistenceException;
	
	
}
