package org.rm3umf.framework.buildmodel;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.rm3umf.domain.Message;
import org.rm3umf.domain.News;
import org.rm3umf.domain.User;
import org.rm3umf.domain.UserModel;
import org.rm3umf.lucene.FacadeLuceneOld;
import org.rm3umf.lucene.FacadeLucene_S1;
import org.rm3umf.persistenza.AAFacadePersistence;
import org.rm3umf.persistenza.PersistenceException;

import util.UtilText;

public class IndexPreparator_s1 extends IndexPreparator {
	
	
	private String pathIndex;
	
	
	public IndexPreparator_s1(String pathIndex){
		super(pathIndex);
		this.pathIndex=pathIndex;
		
	}
	
	public void prepareIndex(List<UserModel> usermodels) throws PersistenceException{
		
		/*
		 * Preparo il sistema alla validazione del modello costruito
		 * E' qui che devo indicizzare con Lucene perchè le rappresentazioni utente senza una cerata soglia di segnali
		 * non le devo neanche indicizzare perchè se non ho abbastanza segnali non riesco a confrontare un UM  
		 * efficacemente con gli altri. 
		 */
		
		FacadeLucene_S1 facadeLucene_s1=new FacadeLucene_S1(this.pathIndex+"/s1");
		facadeLucene_s1.iniziaIndicizzazione();
		
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
				String pseudodocument = getPseudoDocument(um.getUser());
				List<News> pseudodocument_news = getPseudoDocumentNews(um.getUser());
				
				List<Object> list = new LinkedList<Object>();
				list.add(user);
				list.add(listaFollower);
				list.add(listaFollowed);
				list.add(pseudodocument);
				list.add(pseudodocument_news);
				
				
				facadeLucene_s1.addDocument(list);
				facadeLucene_s1.toString();
				um=null;	
				
			}
		
		//chiudo l'indice
		facadeLucene_s1.fineIndicizzazione();
	}
	
	
	
	private List<News> getPseudoDocumentNews(User user) throws PersistenceException {
		List<News> listMessage=AAFacadePersistence.getInstance().newsRetriveByUser(user.getIduser());
		List<News> listNews = new LinkedList<News>();
		//String pseudoDocument = "";
		News n = new News();
		for(News m:listMessage){
			//pseudoDocument=pseudoDocument+" \n"+m.getText();
			n.setId(m.getId());
			n.setTitle(UtilText.removeStopWord(m.getTitle()));
			n.setDescription(UtilText.removeStopWord(m.getDescription()));
			n.setNewscontent(UtilText.removeStopWord(m.getNewscontent()));
			listNews.add(n);
		}
		
		//System.out.println("---------------\n"+pseudoDocument+"---------------\n");
		//return UtilText.getInstance().removeStopWord(pseudoDocument);
		return listNews;
	}

	private String getPseudoDocumentFollowerFollowee(Set<Long> listaFollower, Set<Long> listaFollowed) {
		String pseudodocument = "";
		for(Long follower: listaFollower) {
			pseudodocument = pseudodocument.concat(""+follower+"\n");
		}
		for(Long followed: listaFollowed) {
			pseudodocument = pseudodocument.concat(""+followed+"\n");
		}
		return UtilText.getInstance().removeStopWord(pseudodocument);
	}

	private String getPseudoDocument(User user) throws PersistenceException{
		List<Message> listMessage=AAFacadePersistence.getInstance().messageRetriveByUser(user);
		String pseudoDocument = "";
		for(Message m:listMessage){
			pseudoDocument=pseudoDocument+" \n"+m.getText();
		}
		
		System.out.println("---------------\n"+pseudoDocument+"---------------\n");
		return UtilText.getInstance().removeStopWord(pseudoDocument);
		
	}

}
