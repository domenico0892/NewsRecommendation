package org.rm3umf.framework.buildmodel.extractor;


import java.util.List;
import java.util.Map;
import java.util.Set;

import org.rm3umf.domain.Enrichment;
import org.rm3umf.domain.Message;
import org.rm3umf.domain.Period;
import org.rm3umf.domain.PseudoFragment;
import org.rm3umf.domain.SignalComponent;
import org.rm3umf.domain.User;
import org.rm3umf.persistenza.PersistenceException;



public interface StrategyExtraction {
	
	public String getCriterion();
	
	public void exploreResource(boolean exploreResource);
	
	public List<SignalComponent> extract(PseudoFragment pseudo) throws ExtractorException, PersistenceException;

	public List<Enrichment> filter(int tHRESHOLD) throws PersistenceException;

	public void delete(Enrichment enr) throws PersistenceException;

	public List<Enrichment> retrieveAllEnrichment() throws PersistenceException;

	public Map<String, Enrichment> retrieveEnrichmentUserAndPeriod(User user, int periodid) throws PersistenceException;

	public List<Message> doRetrieveByUserIdAndDate(User user, Period period) throws PersistenceException;


}
