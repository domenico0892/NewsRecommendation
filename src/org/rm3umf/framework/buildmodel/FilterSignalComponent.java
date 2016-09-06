package org.rm3umf.framework.buildmodel;

import java.util.List;

import org.rm3umf.domain.Enrichment;
import org.rm3umf.domain.News;
import org.rm3umf.framework.buildmodel.extractor.StrategyExtraction;
import org.rm3umf.persistenza.AAFacadePersistence;
import org.rm3umf.persistenza.PersistenceException;

import twitter4j.internal.logging.Logger;

/**
 * Fase 2 :  Eliminiomo le compoenti che si riferiscono    
 *  
 * Questa filtro individua e cancella le signalComponent che si riferiscono ad una news
 * referenziato una sola volta. Questo perchè essendo stato utilizzato una sola volta
 * potrebbe essere una news errata o comunque non influente.
 * 
 * @author Sirian
 *
 */

public class FilterSignalComponent {

	private static Logger logger = Logger.getLogger(FilterSignalComponent.class);


	public void filter(int THRESHOLD, List<StrategyExtraction> listaEnr) throws PersistenceException{
		logger.info("avviato filtroSignalComponent");
		for(StrategyExtraction se : listaEnr) {
			if(!se.getCriterion().equals("Topic")) {
				//recupero tutte le news inrilevanti
				List<Enrichment> listaEnrichment = se.filter(THRESHOLD);

				for(Enrichment enr:listaEnrichment){
					//				logger.info("cancello tutti i signalCompoent relativi all'arricchimento : "+enr.getName());
					//				AAFacadePersistence.getInstance().signalComponentDeleteByEnrichment(enr, se.getCriterion());
					logger.info("cancello l'arricchimento : "+enr.getName());
					se.delete(enr);
				}
			}
		}
		logger.info("fine filtroSignalComponent");



	}

}
