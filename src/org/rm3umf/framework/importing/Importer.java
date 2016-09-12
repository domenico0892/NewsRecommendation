package org.rm3umf.framework.importing;



import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.rm3umf.domain.*;
import org.rm3umf.framework.buildmodel.extractor.ExtractorException;
import org.rm3umf.framework.buildmodel.extractor.UrlExtractor;
import org.rm3umf.lucene.FacadeLuceneOld;
import org.rm3umf.net.downloader.QueueException;
import org.rm3umf.net.twitter.FacadeTwitter4j;
import org.rm3umf.persistenza.AAFacadePersistence;
import org.rm3umf.persistenza.PersistenceException;



















import twitter4j.TwitterException;
import util.MD5;

/**
 * Questa classi si occupa di memorizzare gli utenti, prelevandoli dalla tabella
 * tweets_sample e effettua le richieste a twitter per prendere i follower.
 * @author Sirian
 *
 */


public class Importer {

	private static final Logger logger = Logger.getLogger(Importer.class);



	private DatasetAdapter dataset;
	private UrlExtractor urlextractor;


	public Importer(){
		this.urlextractor = new UrlExtractor();
	}

	public void start() throws DatasetException, PersistenceException, QueueException, IOException, InterruptedException, TwitterException, NoSuchAlgorithmException{

		//inizializzo il DB
		//potrei mettere tutte queste operazioni in una classe prepareDB che cancella tutto
		//se � necessario ed eventualmente crea le tabelle
		//	AAFacadePersistence.getInstance().prepareDBImporting();

		//FILTRO IMPORTING
		dataset = new DatasetUmap();
		SocialEnricherFromDB modelEnricher = new SocialEnricherFromDB();

		// MI PRENDO DA dataset_umap la lista di USER
		List<User> utentiDataset= (List<User>) dataset.getObject();

		int sizeUsers=utentiDataset.size();
		int iterazione=0;
		//Scorro tutti gli utenti

		for(User user:utentiDataset){
			try {
				iterazione++;
				System.err.println("Siamo all'utente :"+user+"["+iterazione +"su" +sizeUsers+"]");
				//serve per evitare di fare richieste inutili a twitter	

				//	boolean isPublicProfine=false;
				Set<Long> listaFollower=null;
				Set<Long> listaFollowed=null;
				//Set<Long> rilevantFollowers=null;

				//Recupero i follower e followed dell'utente corrente	
				listaFollower = modelEnricher.getFollower(user.getIduser());
				listaFollowed = modelEnricher.getFollowed(user.getIduser());

				if (listaFollower.size() != 0 && listaFollower.size() != 0) {

					//salvo l'utente con tutte le sue relazioni
					AAFacadePersistence.getInstance().userSave(user);

					//salvo followed e followers dell'utente user
					AAFacadePersistence.getInstance().userSaveFollowed(user, listaFollowed);
					AAFacadePersistence.getInstance().userSaveFollower(user, listaFollower);


					List<Message> listaMessaggi=null;
					listaMessaggi = dataset.getMessagesByUser(user);

					for(Message message:listaMessaggi){
						//salvo il messaggio con le risorse che sono state trovate
						AAFacadePersistence.getInstance().messageSave(message);
						logger.info("salvato messaggio:"+message);				
					}

					Map<News, String> listanews = urlextractor.getNews(listaMessaggi);
					for(News n : listanews.keySet() ) {
						try{
							AAFacadePersistence.getInstance().newsUserSave(n.getId(), user.getIduser(), listanews.get(n));
						} 
						catch(PersistenceException e) {
							System.err.println("Persistance Exception");
						}
					}
				}
			}
			catch (Exception e) {
				
			}
		}
	}
}
		