package org.rm3umf.framework.buildmodel;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.*;
import org.rm3umf.domain.Enrichment;
import org.rm3umf.domain.Message;
import org.rm3umf.domain.News;
import org.rm3umf.domain.Period;
import org.rm3umf.domain.PseudoFragment;
import org.rm3umf.domain.SignalComponent;
import org.rm3umf.domain.User;
import org.rm3umf.framework.buildmodel.extractor.ExtractorException;
import org.rm3umf.framework.buildmodel.extractor.NewsEntitySignalComponent;
import org.rm3umf.framework.buildmodel.extractor.StrategyExtraction;
import org.rm3umf.persistenza.AAFacadePersistence;
import org.rm3umf.persistenza.PersistenceException;

/**
 * FASE 1:  Analizza il periodo e crea le signal component.
 * 
 * Questa classe analizze un periodo. In pratica significa che recupera tutti gli pseudo-fragment relativi al
 * periodo li analizza e ci estrae le signal component che verranno memorizzate sul DB per la successiva 
 * fase.
 * @author giulz
 *
 */
public class BuiltSignalComponent {

	private static Logger logger = Logger.getLogger(BuiltSignalComponent.class);

	private List<StrategyExtraction> listaEnrichment;
	private WeighingScheme ws;

	public BuiltSignalComponent(List<StrategyExtraction> listaEnrichment){
		this.listaEnrichment=listaEnrichment;
		this.ws=new WeighingScheme();
	}



	/**
	 * A partire dai lista dei periodi crea tutti quante le Signal Component 
	 * 
	 * @param listaPeriodi
	 * @throws PersistenceException
	 * @throws ExtractorException
	 * @throws NoSuchAlgorithmException 
	 */

	public void createSignalComponent(List<Period> listaPeriodiTraining, List<Period> listaPeriodiTest, int SOGLIACONCEPT) throws PersistenceException, ExtractorException, NoSuchAlgorithmException{/*
	 *Creo i signal componente relativi relative ai periodi per il training
	 */

		for(Period period :listaPeriodiTraining.subList(0, listaPeriodiTraining.size())){
			System.out.println("Costruisco i SignalComponent per il periodo di training "+period);
			//			//salvo il periodo
			try {
				AAFacadePersistence.getInstance().periodSave(period);
			} catch(PersistenceException e) {
				logger.info("periodo già memorizzato");
			}
		}

		for(StrategyExtraction sc: this.listaEnrichment) {
			extractSignalComponent(listaPeriodiTraining, sc, SOGLIACONCEPT);
		}


		//Creo i signal component relativi ai periodi di test
		for(Period period :listaPeriodiTest.subList(0, listaPeriodiTest.size())){
			System.out.println("Costruisco i SignalComponent per il periodo di test "+period);
			//salvo il periodo
			try {
				AAFacadePersistence.getInstance().periodSave(period);
			} catch(PersistenceException e) {
				logger.info("periodo già memorizzato");
			}
		}
		
		//Da eseguire solo per il metodo tfidf sul segnale verità
//		for(StrategyExtraction sc: this.listaEnrichment) {
//			extractSignalComponent(listaPeriodiTest, sc, SOGLIACONCEPT);
//		}
	}


	/**
	 * Questo metodo 
	 * @param period
	 * @throws ExtractorException
	 * @throws PersistenceException
	 * @throws NoSuchAlgorithmException 
	 */
	public void extractSignalComponent(List<Period> periods, StrategyExtraction enrichment, int SOGLIACONCEPT) throws ExtractorException, PersistenceException, NoSuchAlgorithmException{
		logger.info("creo i signal component");
		List<PseudoFragment> listaPseudo = null;
		List<User> listaUser = AAFacadePersistence.getInstance().userRetriveAll();
		int c = 0;
		for(User u : listaUser) {
//		User u = new User();
//		u.setIduser(681473);
			c++;
			logger.info("Utente: " + c + " su: " + listaUser.size());
			logger.info("Analizzo l'utente: " + u.getIduser());
			Map<String, Integer> listaEnrichmentNelPerido = new HashMap<String, Integer>(); //conta in quanti periodi si è parlato di un arricchimento
			List<SignalComponent> listaSignalComponentUtente = new LinkedList<SignalComponent>();
			for(Period period :periods.subList(0, periods.size())){
				listaPseudo = AAFacadePersistence.getInstance().pseudoDocumentGetByPeriodAndUser(period, u);
				//Memorizzerà tutti i signal component del periodo 
				//i signalComp da passare alla classe WeighingScheme 
				Map<String, SignalComponent> periodSigComp = new HashMap<String, SignalComponent>(); 
				int maxOccurence = 0;
				List<SignalComponent> sigCompForPseudo=null;
				//Per ogni utente
				for(PseudoFragment pseudo: listaPseudo){
					pseudo.setType(enrichment);
					logger.info("analizzo pseudoDocument : "+pseudo);
					//estraggo dallo pseudo-document i signalComponent presenti
					List<SignalComponent> sigCompForEnrichment = enrichment.extract(pseudo);
					//maxOccurence = ws.findMaxOccurenceinPeriod(sigCompForUrlEnrichment, maxOccurence);
					SignalComponent e = null;
					for(SignalComponent sc : sigCompForEnrichment) {
						e = periodSigComp.get(sc.getEnrichment().getName());
						if(e == null) {
							e = new SignalComponent();
							e.setEnrichment(sc.getEnrichment());
							e.setOccorence(sc.getOccorence());
							e.setUser(u);
							e.setPeriod(period);
							e.setType(enrichment.getCriterion());
							periodSigComp.put(sc.getEnrichment().getName(), e);
							listaSignalComponentUtente.add(e);

						} else {
							int cont = e.getOccorence();
							cont += sc.getOccorence();
							e.setOccorence(cont);
						}
					}
				}
				//maxoccurence nel periodo
				for(SignalComponent d : periodSigComp.values()) {
					int occ = d.getOccorence();
					if(occ > maxOccurence) {
						maxOccurence = occ;
					}
				}

				//Setto TF - o componente A
				ws.setTf(periodSigComp, maxOccurence);

				//Memorizzo il numero di periodi in cui compare l'arricchimento per un utente
				List<String> appo = new LinkedList<String>(); //Lista che memorizza gli id degli arricchimenti già contati nel periodo
				for(SignalComponent sc : periodSigComp.values()) {
					String id = sc.getEnrichment().getName();
					if(!appo.contains(id)) {
						if(listaEnrichmentNelPerido.containsKey(id)) {
							int x = listaEnrichmentNelPerido.get(id);
							x ++;
							listaEnrichmentNelPerido.put(id, x);
						} else {
							listaEnrichmentNelPerido.put(id, 1);
						}	
						appo.add(id);
					}
				}			
			}
			saveSignalComponent (periods.size(), listaEnrichmentNelPerido, listaSignalComponentUtente, SOGLIACONCEPT);
		}
	}

	public void saveSignalComponent (int numeroPeriodi,Map<String, Integer> listaEnrichmentNelPerido , List<SignalComponent> listaSignalComponentUtente, int SOGLIACONCEPT) throws PersistenceException, NoSuchAlgorithmException {
		for(String keyEnrichment : listaEnrichmentNelPerido.keySet()){
			int periodi = listaEnrichmentNelPerido.get(keyEnrichment);
			ws.setIDF(numeroPeriodi, periodi, listaSignalComponentUtente, keyEnrichment, SOGLIACONCEPT);
		}
	}

}
