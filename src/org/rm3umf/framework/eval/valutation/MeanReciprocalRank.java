package org.rm3umf.framework.eval.valutation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rm3umf.framework.eval.Result;
import org.rm3umf.framework.eval.ValutationFunction;
import org.rm3umf.persistenza.AAFacadePersistence;
import org.rm3umf.persistenza.PersistenceException;

public class MeanReciprocalRank implements ValutationFunction {

	private String name="MEAN_RECIPROCAL_RANK";

	public MeanReciprocalRank(){

	}


	public double valutate(Result training, Result test) {



		//mi restituisce gli utenti del dataset
		Set<Long> listUser = training.getUser();
		int indice =0;

		double numUsers = listUser.size(); 
		double rRank=0.0;
		
		for(Long userid:listUser){
			
			indice++;

			//Aggiungo followers e followed alla lista dei rilevant user
			List<Long> relevantUsers = (List<Long>) test.getBestUsers(userid).keySet();

			//Recupero il ranking da result rispetto all'utente corrente
			List<Long> ranking = (List<Long>) training.getBestUsers(userid).keySet();

			
			int pos=0;//memorizza a che punto siamo
			
			for(Long u:ranking){
				pos++;
				boolean  isContain=relevantUsers.contains(u);
				System.out.println("     ("+pos+")"+u);
				//Se è stato trovato incremento success e vado al prossimo utente
				if(isContain){
					System.out.println("trovato utente rilevante a pos "+pos);
					rRank+=1./pos;
					break;
				}

			}
			System.out.println("reciprocal rank="+rRank);	
		}

		double mrr=(1/numUsers)*(rRank);
		return mrr ;



	}

	public String getNameFunction() {
		// TODO Auto-generated method stub
		return this.name;
	}

}
