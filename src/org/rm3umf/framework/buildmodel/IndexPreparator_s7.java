package org.rm3umf.framework.buildmodel;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.rm3umf.domain.Message;
import org.rm3umf.domain.User;
import org.rm3umf.domain.UserModel;
import org.rm3umf.lucene.FacadeLucene;
import org.rm3umf.lucene.FacadeLucene_S1;
import org.rm3umf.lucene.FacadeLucene_s7;
import org.rm3umf.persistenza.AAFacadePersistence;
import org.rm3umf.persistenza.PersistenceException;

import util.UtilText;

public class IndexPreparator_s7 implements IndexPreparator {
	
	
	private String pathIndex;
	
	
	public IndexPreparator_s7(String pathIndex){
		this.pathIndex=pathIndex;
		
	}
	
	public void prepareIndex(List<UserModel> usermodels) throws PersistenceException{
		
		/*
		 * Preparo il sistema alla validazione del modello costruito
		 * E' qui che devo indicizzare con Lucene perchè le rappresentazioni utente senza una cerata soglia di segnali
		 * non le devo neanche indicizzare perchè se non ho abbastanza segnali non riesco a confrontare un UM  
		 * efficacemente con gli altri. 
		 */
		
		FacadeLucene facadeLucene_s7=new FacadeLucene_s7(this.pathIndex+"/s7");
		facadeLucene_s7.iniziaIndicizzazione();
		
		//recupero tutti i modelli utente rrelativi agli utente che hanno almento un segnale
		List<UserModel> modelliUtente = AAFacadePersistence.getInstance().userModelRetriveAll(); 
		for(UserModel um: modelliUtente){
			
			//verifico quanti sengali ha la rappresentazione
			//int numOfSignal = um.getSignals().size();
			//Se um ha più segnali della soglia allora indicizzalo 
		
				//se è maggiore indicizziamo 
				User user = um.getUser();
				//recupero dal db i followers e i followed 
				Set<Long> listaFollower=AAFacadePersistence.getInstance().userGetFollower(user.getIduser());
				Set<Long> listaFollowed=AAFacadePersistence.getInstance().userGetFollowed(user.getIduser());
				
				List list = new LinkedList<Object>();
				list.add(user);
				list.add(listaFollower);
				list.add(listaFollowed);
				
				
				facadeLucene_s7.addDocument(list);
				facadeLucene_s7.toString();
				um=null;	
				
			}
		
		//chiudo l'indice
		facadeLucene_s7.fineIndicizzazione();
	}

}
