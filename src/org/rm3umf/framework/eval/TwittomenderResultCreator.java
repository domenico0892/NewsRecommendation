package org.rm3umf.framework.eval;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.queryParser.ParseException;
import org.rm3umf.domain.Message;
import org.rm3umf.domain.Signal;
import org.rm3umf.domain.User;
import org.rm3umf.domain.UserModel;
import org.rm3umf.lucene.FacadeLucene;
import org.rm3umf.persistenza.AAFacadePersistence;
import org.rm3umf.persistenza.PersistenceException;
import org.rm3umf.xml.FactoryXml;

import util.UtilMath;
import util.UtilText;

/**
 * Crea il Resualt a partire da una implementazione di basata su Lucene della funzione s1
 * che si trova sull'articolo Twittomender 
 * 
 * S@10 = 0.16211878009630817 (202) 
 * MKT  = 0.12358201086533352 (156)
 * @author giulz
 *
 */
public class TwittomenderResultCreator {


	private int n;

	public TwittomenderResultCreator(int n){
		FacadeLucene.getInstance().prepareSearching();
		this.n=n;
	}


	/**
	 * Applica la funzione di similarità ai modelli utenti e restituisce un oggetto che rappresenta
	 * il risultato dell'applicazione di tale funzione
	 */
	public Result applyS1Function(List<UserModel> userModels,int limit){

		if(limit<0){
			limit=userModels.size();
		}

		//Creo il risultato
		Result result = new Result();
		Map<Long, List<Double>> user_bestUsers = new HashMap<Long, List<Double>>();

		//setto il nome della funzione da utilizzare 
		result.setFunctionSimilarity("S1 Twittomender"); 
		result.setN(n);

		//Prendo il tempo di inizio in millisec 
		GregorianCalendar gcStart = new GregorianCalendar();
		long timeStart=gcStart.getTimeInMillis();

		//Data corrente
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MM-HH_mm");
		String date=sdf.format(gc.getTime());
		result.setDate(date);

		//Ordino la lista
		//Collections.sort(userModels,new UserModelComparator());

		/*Per ogni utente calcolo lo score con tutti gli altri utenti e li ordino in base a questo score*/
		int iteration=0;
		for(int i=0 ; i<limit && i<userModels.size(); i++){
			UserModel umCorr = userModels.get(i);
			iteration++;
			System.out.println("ITERATION "+iteration+":"+umCorr);
			Map<Long, Double> bestUsers=FacadeLucene.getInstance().retriveByPseudodocument(getPseudoDocument(umCorr.getUser()));
			//mostro tutti gli utenti
			//			for(Long id: bestUsers)
			//				System.out.println("    "+id);
			long useridCorr=umCorr.getUser().getIduser();
			//salvo il risultato 
			Map<Long, Double> bestUser_n = new HashMap<Long, Double>();
			if(bestUsers.size()>n){
				int j = 0;
				for(Long user:bestUsers.keySet() ){
					if(j<=n){
						j++;
						bestUser_n.put(user,bestUsers.get(user));
					}
				}
			}
			result.addListBestUser(useridCorr, UtilMath.sortMapByValues(bestUsers));
		}

		//setto il tempo impiegato in millisecondi per l'applicazione dell'algoritmo
		GregorianCalendar gcEnd = new GregorianCalendar();
		long timeEnd=gcEnd.getTimeInMillis();
		long duration = timeEnd-timeStart;
		result.setDuration(duration);

		return result;

	}



	/**
	 * Applica la funzione di similarità s7 presenta in twittomender. Per ogni utente viene 
	 * generato uno pseudodocument contente gli id dei seguaci e dei seguito. Vengono poi 
	 * estratti i top 10 utenti simili utilizzando l'indicizzazione di Lucene.
	 */
	public Result applyS7Function(List<UserModel> userModels,int limit){

		if(limit<0){
			limit=userModels.size();
		}

		//Creo il risultato
		Result result = new Result();

		//setto il nome della funzione da utilizzare 
		result.setFunctionSimilarity("S7 Twittomender"); 
		result.setN(n);

		//Prendo il tempo di inizio in millisec 
		GregorianCalendar gcStart = new GregorianCalendar();
		long timeStart=gcStart.getTimeInMillis();

		//Data corrente
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MM-HH_mm");
		String date=sdf.format(gc.getTime());
		result.setDate(date);

		//Ordino la lista
		//Collections.sort(userModels,new UserModelComparator());

		/*Per ogni utente calcolo lo score con tutti gli altri utenti e li ordino in base a questo score*/
		int iteration=0;
		for(int i=0 ; i<limit && i<userModels.size(); i++){
			UserModel umCorr = userModels.get(i);
			iteration++;
			System.out.println("ITERATION "+iteration+":"+umCorr);
			Map<Long, Double> bestUsers=FacadeLucene.getInstance().retriveByPseudodocument_FolloweeFollower(getPseudoDocument_Followee_Follower(umCorr.getUser()));
			//mostro tutti gli utenti
			//			for(Long id: bestUsers)
			//				System.out.println("    "+id);
			long useridCorr=umCorr.getUser().getIduser();
			//salvo il risultato
			Map<Long, Double> bestUser_n = new HashMap<Long, Double>();
			if(bestUsers != null) {
				if(bestUsers.size()>n){
					int j = 1;
					for(Long user:bestUsers.keySet() ){
						if(j<n){
							j++;
							bestUser_n.put(user,bestUsers.get(user));
						}
					}
				}
			}
			result.addListBestUser(useridCorr, bestUser_n);
		}

		//setto il tempo impiegato in millisecondi per l'applicazione dell'algoritmo
		GregorianCalendar gcEnd = new GregorianCalendar();
		long timeEnd=gcEnd.getTimeInMillis();
		long duration = timeEnd-timeStart;
		result.setDuration(duration);

		return result;

	}

	public Result applyS7FunctionTraining(List<UserModel> userModels,int limit){

		if(limit<0){
			limit=userModels.size();
		}

		//Creo il risultato
		Result result = new Result();

		//setto il nome della funzione da utilizzare 
		result.setFunctionSimilarity("S7 Training"); 
		result.setN(n);

		//Prendo il tempo di inizio in millisec 
		GregorianCalendar gcStart = new GregorianCalendar();
		long timeStart=gcStart.getTimeInMillis();

		//Data corrente
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MM-HH_mm");
		String date=sdf.format(gc.getTime());
		result.setDate(date);

		//Ordino la lista
		//Collections.sort(userModels,new UserModelComparator());

		/*Per ogni utente calcolo lo score con tutti gli altri utenti e li ordino in base a questo score*/
		int iteration=0;
		for(int i=0 ; i<limit && i<userModels.size(); i++){
			UserModel umCorr = userModels.get(i);
			iteration++;
			System.out.println("ITERATION "+iteration+":"+umCorr);
			Map<Long, Double> bestUsers=FacadeLucene.getInstance().retriveByPseudodocument_FolloweeFollower(getPseudoDocument_Followee_Follower(umCorr.getUser()));
			//mostro tutti gli utenti
			//			for(Long id: bestUsers)
			//				System.out.println("    "+id);
			long useridCorr=umCorr.getUser().getIduser();
			//salvo il risultato
//			Map<Long, Double> bestUser_n = new HashMap<Long, Double>();
			if(bestUsers != null) {
				bestUsers = UtilMath.sortMapByValues(bestUsers);
//				if(bestUsers.size()>n){
//					int j = 1;
//					for(Long user:bestUsers.keySet() ){
//						if(j<n){
//							j++;
//							bestUser_n.put(user,bestUsers.get(user));
//						}
//					}
				}
			result.addListBestUser(useridCorr, bestUsers);

		}

		//setto il tempo impiegato in millisecondi per l'applicazione dell'algoritmo
		GregorianCalendar gcEnd = new GregorianCalendar();
		long timeEnd=gcEnd.getTimeInMillis();
		long duration = timeEnd-timeStart;
		result.setDuration(duration);

		return result;

	}



	/**
	 * Genero la lista dei best User per il 30% dei dati (test).
	 * Ho indicizzato con lucene i titoli, descrizioni e contenuti delle news
	 * per effettuare un confronto con i segnali.
	 */
	public Result newsRecommender_Test(List<UserModel> userModels,int limit){

		if(limit<0){
			limit=userModels.size();
		}

		//Creo il risultato
		Result result = new Result();

		//setto il nome della funzione da utilizzare 
		result.setFunctionSimilarity("News_Recommender"); 
		result.setN(n);

		//Prendo il tempo di inizio in millisec 
		GregorianCalendar gcStart = new GregorianCalendar();
		long timeStart=gcStart.getTimeInMillis();

		//Data corrente
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MM-HH_mm");
		String date=sdf.format(gc.getTime());
		result.setDate(date);

		//Ordino la lista
		//Collections.sort(userModels,new UserModelComparator());

		/*Per ogni utente calcolo lo score con tutti gli altri utenti e li ordino in base a questo score*/
		int iteration=0;
		for(int i=0 ; i<limit && i<userModels.size(); i++){
			UserModel umCorr = userModels.get(i);
			iteration++;
			//System.out.println("ITERATION "+iteration+":"+umCorr);
			Map<Long, Double> bestUsers=FacadeLucene.getInstance().retriveByNews(getNews(umCorr.getUser()));
			//mostro tutti gli utenti
			//			for(Long id: bestUsers)
			//				System.out.println("    "+id);
			long useridCorr=umCorr.getUser().getIduser();
			//salvo il risultato 
			Map<Long, Double> bestUser_n = new HashMap<Long, Double>();
			if(bestUsers != null) {
				bestUsers= UtilMath.sortMapByValues(bestUsers);
				if(bestUsers.size()>n){
					int j = 0;
					for(Long user:bestUsers.keySet() ){
						if(j<=n){
							j++;
							bestUser_n.put(user,bestUsers.get(user));
						}
					}
				}
			}
			result.addListBestUser(useridCorr, bestUsers);
		}

		//setto il tempo impiegato in millisecondi per l'applicazione dell'algoritmo
		GregorianCalendar gcEnd = new GregorianCalendar();
		long timeEnd=gcEnd.getTimeInMillis();
		long duration = timeEnd-timeStart;
		result.setDuration(duration);

		return result;

	}

	public Result applyS1TrainingFunction(List<UserModel> userModels,int limit){

		if(limit<0){
			limit=userModels.size();
		}

		//Creo il risultato
		Result result = new Result();
		Map<Long, List<Double>> user_bestUsers = new HashMap<Long, List<Double>>();

		//setto il nome della funzione da utilizzare 
		result.setFunctionSimilarity("S1_Training"); 
		result.setN(n);

		//Prendo il tempo di inizio in millisec 
		GregorianCalendar gcStart = new GregorianCalendar();
		long timeStart=gcStart.getTimeInMillis();

		//Data corrente
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MM-HH_mm");
		String date=sdf.format(gc.getTime());
		result.setDate(date);

		//Ordino la lista
		//Collections.sort(userModels,new UserModelComparator());

		/*Per ogni utente calcolo lo score con tutti gli altri utenti e li ordino in base a questo score*/
		int iteration=0;
		for(int i=0 ; i<limit && i<userModels.size(); i++){
			UserModel umCorr = userModels.get(i);
			iteration++;
			System.out.println("ITERATION "+iteration+":"+umCorr);
			Map<Long, Double> bestUsers=FacadeLucene.getInstance().retriveByPseudodocumentTraining(getPseudoDocumentTraining(umCorr.getUser()));
			//mostro tutti gli utenti
			//			for(Long id: bestUsers)
			//				System.out.println("    "+id);
			long useridCorr=umCorr.getUser().getIduser();
			//salvo il risultato 
			Map<Long, Double> bestUser_n = new HashMap<Long, Double>();
			if(bestUsers != null){
				if(bestUsers.size()>n){
					int j = 0;
					for(Long user:bestUsers.keySet() ){
						if(j<=n){
							j++;
							bestUser_n.put(user,bestUsers.get(user));
						}
					}
				}
				result.addListBestUser(useridCorr, bestUser_n);
			}
		}

		//setto il tempo impiegato in millisecondi per l'applicazione dell'algoritmo
		GregorianCalendar gcEnd = new GregorianCalendar();
		long timeEnd=gcEnd.getTimeInMillis();
		long duration = timeEnd-timeStart;
		result.setDuration(duration);

		return result;

	}


	//	private String getPseudoDocument(User user){
	//		List<Message> listMessage=null;
	//		try{
	//			listMessage=AAFacadePersistence.getInstance().messageRetriveByUser(user);
	//		}catch(PersistenceException pe){
	//			throw new RuntimeException("errore durante la creazione dello pseudo-document");
	//		}
	//		String pseudoDocument = "";
	//		for(Message m:listMessage){
	//			pseudoDocument=pseudoDocument+" \n"+m.getText();
	//		}
	//		
	//		//System.out.println(pseudoDocument);
	//		return pseudoDocument;
	//		
	//	}


	private String getPseudoDocument(User user){
		String pseudodocument="";
		try {
			pseudodocument=FacadeLucene.getInstance().getPseudodocument(user.getIduser());

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//trasformo tutto in lowercase
		//pseudodocument=pseudodocument.toLowerCase();

		//per diminuire il numero dei query term elimino le stopwords
		pseudodocument=UtilText.getInstance().removeStopWord(pseudodocument);	

		if(pseudodocument.length()>1000000)
			pseudodocument=pseudodocument.substring(0,1000000);

		//System.out.println(pseudodocument);
		return pseudodocument;

	}

	private String getPseudoDocumentTraining(User user){
		String pseudodocument="";
		try {
			pseudodocument=FacadeLucene.getInstance().getPseudodocumentTraining(user.getIduser());

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//trasformo tutto in lowercase
		//pseudodocument=pseudodocument.toLowerCase();

		//per diminuire il numero dei query term elimino le stopwords
		pseudodocument=UtilText.getInstance().removeStopWord(pseudodocument);	

		if(pseudodocument.length()>1000000)
			pseudodocument=pseudodocument.substring(0,1000000);

		//System.out.println(pseudodocument);
		return pseudodocument;

	}


	/*Dato un utente restituisce tramite l'indice di Lucene lo pseudodocument
	 * per l'applicazione della funzione S7 di Twittomender
	 */

	private String getPseudoDocument_Followee_Follower(User user){
		String pseudodocument="";
		try {
			pseudodocument=FacadeLucene.getInstance().getPseudodocument_Followee_Follower(user.getIduser());

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//trasformo tutto in lowercase
		//pseudodocument=pseudodocument.toLowerCase();

		//per diminuire il numero dei query term elimino le stopwords
		pseudodocument=UtilText.getInstance().removeStopWord(pseudodocument);	

		if(pseudodocument.length()>1000000)
			pseudodocument=pseudodocument.substring(0,1000000);

		System.out.println(pseudodocument);
		return pseudodocument;

	}

	private String getNews(User user){
		String pseudodocument="";
		try {
			pseudodocument=FacadeLucene.getInstance().getNews(user.getIduser());

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//trasformo tutto in lowercase
		//pseudodocument=pseudodocument.toLowerCase();

		//per diminuire il numero dei query term elimino le stopwords
		pseudodocument=UtilText.getInstance().removeStopWord(pseudodocument);	

		if(pseudodocument.length()>1000000)
			pseudodocument=pseudodocument.substring(0,1000000);

		//System.out.println(pseudodocument);
		return pseudodocument;

	}



	public static void main(String args[]) throws IOException, PersistenceException {

		User user1 = new User();
		User user2 = new User();
		Long iduser1 = (long) 681473;
		Long iduser2 = (long) 681473;

		user1.setIduser(iduser1);
		user2.setIduser(iduser2);

		UserModel u1 = AAFacadePersistence.getInstance().userModelRetriveByUser(user1);
		UserModel u2 = AAFacadePersistence.getInstance().userModelRetriveByUser(user2);

		List<UserModel> lista = new LinkedList<UserModel>();
		lista.add(u1);
		lista.add(u2);		
		FactoryXml xmlFactory=new FactoryXml();
		TwittomenderResultCreator twittomenderResultCreator = new TwittomenderResultCreator(10);
		Result result2 = twittomenderResultCreator.applyS7Function(lista,100);
		xmlFactory.saveResult(result2);




	}

}
