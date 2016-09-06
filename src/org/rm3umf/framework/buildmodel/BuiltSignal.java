package org.rm3umf.framework.buildmodel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.rm3umf.domain.Enrichment;
import org.rm3umf.domain.News;
import org.rm3umf.domain.Signal;
import org.rm3umf.domain.SignalComponent;
import org.rm3umf.domain.TruthSignal;
import org.rm3umf.domain.User;
import org.rm3umf.framework.buildmodel.extractor.StrategyExtraction;
import org.rm3umf.math.VectorUtil;
import org.rm3umf.persistenza.AAFacadePersistence;
import org.rm3umf.persistenza.PersistenceException;
/**
 * FASE 2: a partire dagli utenti ottengo tutte le signal compoent  e crea i segnali  
 * 
 * In pratica questa classe data un utente crea la rappresetazione di esso costruendo un insieme di segnali
 * utilizzando le signal componente precedentemente memorizzate.
 * 
 * @author giulz
 *
 */
public class BuiltSignal {

	private int lenghtSignalTraining;

	private int SOGLIASEGNALI;

	private FactorySmoothing factorySmoothing;
	private int ordineSmooth;
	private boolean doSmoothing;


	public BuiltSignal(int lenghtSignalTraining, int sogliasegnali,int ordineSmooth){

		this.lenghtSignalTraining=lenghtSignalTraining;
		this.SOGLIASEGNALI=sogliasegnali;
		this.ordineSmooth=ordineSmooth;
		this.factorySmoothing=new FactorySmoothing(ordineSmooth);
		if(ordineSmooth==0)
			doSmoothing=false;
		else
			doSmoothing=true;
	}

	public void buildSignal(List<User> listaUser, List<StrategyExtraction> listaEnrichment) throws PersistenceException {

		for (StrategyExtraction s : listaEnrichment) {
			buildSignalTraining(listaUser,s);
//			buildSignalTest(listaUser, s);
		}

	}

	public void buildSignalTraining(List<User> listaUser, StrategyExtraction s) throws PersistenceException{

		//AAFacadePersistence.getInstance().signalDelete();

		//VA fatto sia per il training che per il test
		int i=0;
		for(User user :listaUser){
//		User user = new User();
//		user.setIduser(17268481);
			i++;
			System.out.println("("+i+")salvo i segnal dell'utente : "+user);
			List<Signal> signalToUser=builtSignalByUser(user, s);
			int numSignals=signalToUser.size();


			//se user ha un num di segnali > THRESHOLD salvo i segnali altrimenti no
			if(numSignals>SOGLIASEGNALI){

				for(Signal signal : signalToUser){
					//effettuo lo smoothing
					if(doSmoothing){
						double[] smoothsingnal=this.factorySmoothing.mediaMobile(signal.getSignal());
						signal.setSignal(smoothsingnal);
					}

					AAFacadePersistence.getInstance().signalSave(signal);

				}
			}else{
				System.err.println("Non salvo i segnali di "+user+ " perche' inferiori alla soglia ("+numSignals+")");
			}

		}

	}

	/**
	 * Questo metodo crea tutti quanti i segnali per uno specifico utente.
	 * Recupera tutte le signal component dell'utente dal DB  e le aggrega in base al periodo 
	 * e alla news.
	 * 
	 * @param user 
	 * @return listSignal - la lista di segnali che forma il profilo
	 * @throws PersistenceException
	 */
	public List<Signal> builtSignalByUser(User user, StrategyExtraction s) throws PersistenceException{

		List<SignalComponent> signalCompByUser = AAFacadePersistence.getInstance().signaComponentRetriveByUser(user, s.getCriterion(),this.lenghtSignalTraining);
		Map<String,Signal> newsid2signal=new HashMap<String,Signal>();
		for(SignalComponent signalComponent:signalCompByUser){
			Enrichment enr =signalComponent.getEnrichment();
			String idEnr=enr.getName();
			Signal signal=newsid2signal.get(idEnr);
			if(signal==null){
				signal=new Signal();
				signal.setUser(user);
				signal.setEnrichment(enr);
				signal.setSignal(new double[this.lenghtSignalTraining]);
				signal.setType(signalComponent.getType());
			}
			int pos = signalComponent.getPeriod().getIdPeriodo();
			//				double value = 0;
			//				if(signalComponent.getIdf() != 0) {
			//					value = signalComponent.getTf() * (Math.log(signalComponent.getIdf()));
			//				}
			signal.getSignal()[pos]=(Math.floor(signalComponent.getTfidf()*1000)/1000);
			newsid2signal.put(idEnr, signal);
		}

		return new LinkedList<Signal>(newsid2signal.values());
	}


	public void buildSignalTest(List<User> listaUser, StrategyExtraction se) throws PersistenceException{

		//AAFacadePersistence.getInstance().signalDelete();

		int i=0;
		for(User user :listaUser){
			i++;
			System.out.println("("+i+")salvo i segnali di verità dell'utente : "+user);
			TruthSignal signal = builtSignalTestByUser(user, se);
			try {
				if(signal != null)
					AAFacadePersistence.getInstance().truthsignalsave(signal);
			} catch(PersistenceException e) {

			}
		}

	}

	private TruthSignal builtSignalTestByUser(User user, StrategyExtraction se) throws PersistenceException {

		List<Enrichment> allEnr = se.retrieveAllEnrichment();
		//Map<String, Enrichment> enrUser = se.retrieveEnrichmentUserAndPeriod(user, this.lenghtSignalTraining);
		Map<String, Long> enrUser = AAFacadePersistence.getInstance().signalComponentUserIdTypeMap(user.getIduser(),se.getCriterion());
		TruthSignal signal = null; 
		if(allEnr != null) {
			signal = new TruthSignal();
			signal.setSignal(new double[allEnr.size()]);
			signal.setUser(user);
			signal.setType(se.getCriterion());
			int i = 0;
			for(Enrichment e : allEnr) {
				if(enrUser.containsKey(e.getName())){
					//Enrichment enr = enrUser.get(e.getName());
					//Metodo binario
					//signal.getSignal()[i]=1;
					//Metodo della somma
					//signal.getSignal()[i] = enr.getNumberInstance();
					//Metodo dell tfidf
					signal.getSignal()[i] = enrUser.get(e.getName());
				} else {
					signal.getSignal()[i]=0;
				}
				i++;
			}
		}
		return signal;
	}

}


