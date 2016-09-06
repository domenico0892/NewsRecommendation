package org.rm3umf.framework.eval.valutation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.rm3umf.framework.eval.ValutationFunction;
import org.rm3umf.framework.eval.Result;
import org.rm3umf.persistenza.AAFacadePersistence;
import org.rm3umf.persistenza.PersistenceException;

import util.UtilMath;

/**
 * NDCG = DCG/IDCG
 * DCG lo calcolo come Sommatoria((2^score(i)-1)/log_2(i+1))
 * In pratica prendo il ranking del training (chiamo T) e quello del test (chiamo S)
 * se l'utente i-esimo di T è anche in S prendo la posizione i da T e lo score
 * associato all'utente in esame da S.
 * 
 * IDCG lo calcolo come il DCG ma ordino in base allo score S
 * 
 * Poi: rapporto tra dcg/idcg (Chiamo NDCG_parziale)
 * 
 * In fine divido NDCG_parziale/numero di utenti (1619)
 * 
 * 

 *
 */

public class NDCG implements ValutationFunction{

	private String name;
	public NDCG(){
		this.name="NDCG";
	}


	public double valutate(Result training, Result test) {



		//mi restituisce gli utenti del dataset
		Set<Long> listUserTraining = training.getUser();
		int indice =0;

		double numUsers = listUserTraining.size(); 
		double dcg = 0.0;
		double idcg = 0.0;
		double ndcg_parziale = 0.0;
		double n = training.getN();

		//Setto il nome della funzione a seconda di n del result
		name = "NDCG@"+n;

		for(Long userid:listUserTraining){


			indice++;
			System.out.println("USER : ("+indice+")"+ userid);		


			//Aggiungo followers e followed alla lista dei rilevant user
			Map<Long, Double> relevantUsers = test.getBestUsers(userid);
			//			Map<Long, Double> relevantUsers_order = UtilMath.sortMapByValues(relevantUsers);
			if(relevantUsers != null) {
				if(relevantUsers.size() > 0){
					//Recupero il ranking da result rispetto all'utente corrente
					Map<Long, Double> ranking = training.getBestUsers(userid);
					ranking = UtilMath.sortMapByValues(ranking);
					//								if(ranking.size() < relevantUsers.size()) {
					//									int l = ranking.size();
					//									int i = 0;
					//									Map<Long, Double> relevantUsers_n = new HashMap<Long, Double>();
					//									for(Long u: relevantUsers.keySet()){
					//										if(i<l) {
					//											i++;
					//											relevantUsers_n.put(u, relevantUsers.get(u));		
					//										} else {
					//											break;
					//										}
					//									}
					//									relevantUsers = relevantUsers_n;
					//								}
//					while(ranking.size() <= 10) {
//						ranking.put((long) 0, 0.0);
//					}

					System.out.println(this.name);
					int i=0;//memorizza a che punto siamo
					for(Long u:ranking.keySet()){
						i++;
						boolean  isContain=relevantUsers.keySet().contains(u);
						System.out.println("     ("+i+")"+u);
						//Se è stato trovato influnezo il valore di dcg con lo score
						if(isContain){
							dcg+=(Math.pow(2, ranking.get(u)) - 1)/ UtilMath.log_base2(i+1);
						} else {
							dcg+=(Math.pow(2, 0) - 1)/ UtilMath.log_base2(i+1);
						}
					}


					i=0;
					int l = ranking.size();
					// questo è il nostro ground truth
					for(Long b:relevantUsers.keySet()){
						if(i<l){
							i++;
							idcg+=(Math.pow(2, relevantUsers.get(b)) - 1)/ UtilMath.log_base2(i+1);
						} else {
							break;
						}
					}

					double ndg_utente= dcg/idcg;

					ndcg_parziale += ndg_utente;
					System.out.println("success="+ndg_utente);	
				}
			}
		}
		double ndcg=ndcg_parziale/numUsers;
		return ndcg ;



	}

	public String getNameFunction() {
		// TODO Auto-generated method stub
		return this.name;
	}

}
