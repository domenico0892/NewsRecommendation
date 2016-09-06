package org.rm3umf.framework.eval;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.queryParser.ParseException;
import org.rm3umf.domain.Message;
import org.rm3umf.domain.Signal;
import org.rm3umf.domain.User;
import org.rm3umf.domain.UserModel;
import org.rm3umf.lucene.FacadeLucene;
import org.rm3umf.lucene.FacadeLucene_S1;
import org.rm3umf.lucene.FacadeLucene_s7;
import org.rm3umf.persistenza.AAFacadePersistence;
import org.rm3umf.persistenza.PersistenceException;
import org.rm3umf.xml.FactoryXml;

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
public class TwittomenderResultCreator_s7 extends TwittomenderResultCreator{
	
		
	public TwittomenderResultCreator_s7(int n){
		FacadeLucene_s7.getInstance().prepareSearching();
		super.n=n;
	}
	
	/**
	 * Applica la funzione di similarità ai modelli utenti e restituisce un oggetto che rappresenta
	 * il risultato dell'applicazione di tale funzione
	 * @throws java.text.ParseException 
	 * @throws ParseException 
	 * @throws java.text.ParseException 
	 */
	public Result applyFunction(List<UserModel> userModels,int limit) throws ParseException, ParseException, java.text.ParseException {

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
			List<Long> bestUsers=FacadeLucene_s7.getInstance().retriveByPseudodocument(getPseudoDocument(umCorr.getUser()));
			//mostro tutti gli utenti
//			for(Long id: bestUsers)
//				System.out.println("    "+id);
			long useridCorr=umCorr.getUser().getIduser();
			//salvo il risultato 
			if(bestUsers.size()>n){
				bestUsers=bestUsers.subList(0,this.n);
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
	
	
	public String getPseudoDocument(User user) throws ParseException, ParseException, org.apache.lucene.queryParser.ParseException, java.text.ParseException{
		String pseudodocument="";
		try {
			pseudodocument=FacadeLucene_s7.getInstance().getPseudodocument(user.getIduser());

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

}
