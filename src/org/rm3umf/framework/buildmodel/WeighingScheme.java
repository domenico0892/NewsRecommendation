package org.rm3umf.framework.buildmodel;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.rm3umf.domain.Enrichment;
import org.rm3umf.domain.SignalComponent;
import org.rm3umf.domain.News;
import org.rm3umf.persistenza.AAFacadePersistence;
import org.rm3umf.persistenza.PersistenceException;

/**
 * Questa classe avrà la responsabilità di pesare i SignalComponent. In pratica conoscendo i signalCompoent
 * di un singolo pseudoDocument setta la compoenent df mentre conoscendo tutti i singnalComponent dell'itero
 * periodo setterà gli idf.
 * 
 * ATTENZIONE: vorrei applicare il pattern Template Method
 * @author giulz
 *
 */

public   class WeighingScheme {

	public WeighingScheme(){
	}




	/**
	 * Questo metodo va eseguito ogni volta che si estraggono i signalComponent dallo pseudodocument 
	 * @param listSignalComp - lista sig comp estratti da pseudo-doc
	 */
	public int findMaxOccurenceinPeriod(List<SignalComponent> listSignalComp, int maxOccurence){
		//System.out.println("#document:"+this.numberOfDocument);
		//this.numberOfDocument++;
		//calcolo la massima occorrenza
		for(SignalComponent sigCompCorr:listSignalComp ){
			int sigOccur=sigCompCorr.getOccorence();
			if(sigOccur>maxOccurence){
				maxOccurence=sigOccur;
			}
		}	
		return maxOccurence;
	}

	public void setTf(Map<String, SignalComponent> listSignalComp, int maxOccurence) {
		for(SignalComponent e:listSignalComp.values()){
			double tf=((double)e.getOccorence())/((double)maxOccurence);
			e.setTf(tf);
		}
	}

	/**
	 * Questo metodo va eseguito alla fine di tutti i periodi
	 * @param periodSignalComp -tutti  i signalcompoent estratti dal periodo corrente
	 * @throws PersistenceException 
	 * @throws NoSuchAlgorithmException 
	 */
	public void setIDF(int periodi, int periodioccorrenze, List<SignalComponent> scs, String keyEnrichment, int SOGLIACONCEPT) throws PersistenceException, NoSuchAlgorithmException{
		double idf = (double) periodi / (double) periodioccorrenze;
		for(SignalComponent sc : scs) {
			if(sc.getEnrichment().getName().equals(keyEnrichment)) {
				System.out.println(sc.getPeriod().getIdPeriodo());
				sc.setIdf(idf);
				setTFIDF(sc, SOGLIACONCEPT);
			}
		}
		
		
	}
	
	public void setTFIDF(SignalComponent sigComp, int SOGLIACONCEPT) throws NoSuchAlgorithmException, PersistenceException {
		Double tfidf = sigComp.getTf() *( Math.log(sigComp.getIdf()));
		sigComp.setTfidf(tfidf);
		System.out.println("salvo :"+sigComp);
		AAFacadePersistence.getInstance().signalComponentSave(sigComp);
	}
}
