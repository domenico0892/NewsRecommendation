package org.rm3umf.framework.buildmodel;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.rm3umf.domain.Message;
import org.rm3umf.domain.News;
import org.rm3umf.domain.Period;
import org.rm3umf.domain.User;
import org.rm3umf.domain.UserModel;
import org.rm3umf.lucene.FacadeLucene;
import org.rm3umf.persistenza.AAFacadePersistence;
import org.rm3umf.persistenza.PersistenceException;

import util.UtilText;

public class IndexPreparator {
	
	
	private String pathIndex;
	
	
	public IndexPreparator(String pathIndex){
		this.pathIndex=pathIndex;
		
	}
	
	public void prepareIndex(List<UserModel> usermodels, List<Period> listaPeriodiTest) throws PersistenceException{
		
		/*
		 * Preparo il sistema alla validazione del modello costruito
		 * E' qui che devo indicizzare con Lucene perchè le rappresentazioni utente senza una cerata soglia di segnali
		 * non le devo neanche indicizzare perchè se non ho abbastanza segnali non riesco a confrontare un UM  
		 * efficacemente con gli altri. 
		 */
		
		FacadeLucene facadeLucene=new FacadeLucene(this.pathIndex);
		facadeLucene.iniziaIndicizzazione();
		//recupero tutti i modelli utente rrelativi agli utente che hanno almento un segnale
		List<UserModel> modelliUtente = AAFacadePersistence.getInstance().userModelRetriveAll(); 
		for(UserModel um: modelliUtente){
			
			//verifico quanti sengali ha la rappresentazione
			//int numOfSignal = um.getSignals().size();
			//Se um ha più segnali della soglia allora indicizzalo 
		
				//se è maggiore indicizziamo 
				User user = um.getUser();
				//recupero dal db i followers e i followed 
				//s7
				Set<Long> listaFollower=AAFacadePersistence.getInstance().userGetFollower(user.getIduser());
				Set<Long> listaFollowed=AAFacadePersistence.getInstance().userGetFollowed(user.getIduser());
//				Set<Long> listaFollower= new HashSet<Long>();
//				Set<Long> listaFollowed= new HashSet<Long>();
				//s1 -> concetti - tutto il db
				String pseudodocument = getPseudoDocument(um.getUser());
//				String pseudodocument ="";
				//s1 -> versione per le news - tutto il db
				List<News> pseudodocument_news = getPseudoDocumentNews(um.getUser());
//				List<News> pseudodocument_news= new LinkedList<News>();
				// sperimentazione 70-30 s1, per il 30% per segnali e twittomender
				List<News> pseudodocument_news_title = getPseudoDocumentNewsComplete(um.getUser(), listaPeriodiTest);
//				List<News> pseudodocument_news_title = new LinkedList<News>();
				//sperimentazione 70-30 s1, per il 70% per s1
				List<News> pseudodocument_news_training = getPseudoDocumentTraining(um.getUser(),listaPeriodiTest);
//				List<News> pseudodocument_news_training =  new LinkedList<News>();

				
				facadeLucene.addDocument(user,listaFollower,listaFollowed,pseudodocument, pseudodocument_news, pseudodocument_news_title, pseudodocument_news_training);
				facadeLucene.toString();
				//faccio una free
				um=null;	
				
			}
		
		//chiudo l'indice
		facadeLucene.fineIndicizzazione();
		
		
	}
	
	
	
	private List<News> getPseudoDocumentTraining(User user, List<Period> listaPeriodiTest) throws PersistenceException {
		List<News> listMessage=AAFacadePersistence.getInstance().newsTrainingRetriveByUser(user.getIduser(), listaPeriodiTest);
		List<News> listnews = new LinkedList<News>();
		String pseudoDocument = "";
		News n;
		for(News m:listMessage){
			n = new News();
			n.setId(m.getId());
			n.setTitle(UtilText.getInstance().removeStopWord(m.getTitle()));
			n.setDescription(UtilText.getInstance().removeStopWord(m.getDescription()));
			n.setNewscontent(UtilText.getInstance().removeStopWord(m.getNewscontent()));
			listnews.add(n);
		}
		
		return listnews;
	}

	private List<News> getPseudoDocumentNews(User user) throws PersistenceException {
		List<News> listMessage=AAFacadePersistence.getInstance().newsRetriveByUser(user.getIduser());
		List<News> listNews = new LinkedList<News>();
		String pseudoDocument = "";
		News n = new News();
		for(News m:listMessage){
			//pseudoDocument=pseudoDocument+" \n"+m.getText();
			n.setId(m.getId());
			n.setTitle(UtilText.getInstance().removeStopWord(m.getTitle()));
			n.setDescription(UtilText.getInstance().removeStopWord(m.getDescription()));
			n.setNewscontent(UtilText.getInstance().removeStopWord(m.getNewscontent()));
			listNews.add(n);
		}
		
		//System.out.println("---------------\n"+pseudoDocument+"---------------\n");
		//return UtilText.getInstance().removeStopWord(pseudoDocument);
		return listNews;
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
	
	private List<News> getPseudoDocumentNewsComplete(User user,  List<Period> listaPeriodiTest) throws PersistenceException {
		List<News> listMessage=AAFacadePersistence.getInstance().newsCompleteRetriveByUser(user.getIduser(), listaPeriodiTest);
		List<News> listnews = new LinkedList<News>();
		News n;
		String pseudoDocument = "";
		for(News m:listMessage){
			n = new News();
			n.setId(m.getId());
			n.setTitle(UtilText.getInstance().removeStopWord(m.getTitle()));
			n.setDescription(UtilText.getInstance().removeStopWord(m.getDescription()));
			n.setNewscontent(UtilText.getInstance().removeStopWord(m.getNewscontent()));
			listnews.add(n);
		}
		return listnews;
	}
	
	
	
	

}
