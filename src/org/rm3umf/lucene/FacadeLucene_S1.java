package org.rm3umf.lucene;


import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.*;
import org.apache.lucene.queryParser.ParseException;
import org.rm3umf.domain.User;



/***
 * Questa classe mi permette di utilizzare i servizi offerti da Lucene
 * @author Giulz
 *
 */
public class FacadeLucene_S1 implements FacadeLucene{
	
	private Logger logger =Logger.getLogger(FacadeLucene.class);
	private static FacadeLucene instance;
	private Indexer indexer;
	private Search searcher; 
	private String pathIndice;

	
	public static FacadeLucene getInstance(){
		if(instance==null)
			instance=new FacadeLucene_S1("./index/s1");
		return instance;
	}


	public FacadeLucene_S1(String pathIndice){
		this.pathIndice=pathIndice;
	}

	/**
	 * Inizia la fase di indicizzazione creando l'Indexer
	 * @param pathIndex
	 * @throws IndexException 
	 */
	public void iniziaIndicizzazione() {
		//salvo il path dell'indice in modo tale da creare il searcher 

		logger.info("inizia indicizzazione");
		try {
			this.indexer= new Indexer_s1(this.pathIndice);
		}catch(Exception e){
			logger.info("errore durante la creazione dell'indice");
			e.getStackTrace();
		}

	}

	public void prepareSearching(){
		System.err.println("prepare to seracing");
		this.searcher=new Search(this.pathIndice);
	}
	
	
	/**
	 * Finisci la fase di indicizzazione
	 */
	public void fineIndicizzazione(){
		logger.info("fine indicizzazione");
		this.indexer.close();
	}
	
	
	
	
	/**
	 *Questo metodo serve per aggiungere una linea all'indice che � in costruzione 
	 * @throws IOException 
	 **/
	public void addDocument(List list) {
		logger.info("aggiundi riga all'indice"+ list.get(1));
		String stringFollowed=list2string((Set<Long>) list.get(2));
		String stringFollower=list2string((Set<Long>) list.get(3));
		LinkedList<String> list2 = new LinkedList<String>();
		list2.add(String.valueOf(list.get(1)));
		list2.add(stringFollowed);
		list2.add(stringFollower);
		list2.add((String) list.get(4));
		list2.add((String) list.get(5));
		try {
			this.indexer.index(list2);
		} catch (IOException e) {
			logger.info("errore durante l'inserimento di un document");
			e.printStackTrace();
		}
		
	}
	
	
	
	/**
	 * Metodo che traforma una lista di stringhe in un'unica stringa separata da spazi bianchi
	 * @param listaStringhe
	 * @return stringa
	 */
	public   String list2string(Set<Long> lista){
		String stringa="";
		for(Long iduser:lista){
			stringa=stringa+" "+String.valueOf(iduser);	
		}
		return stringa;
		
			
	}
	

	
	/**
	 * Dato uno userid mi restituisce i gli utenti rilevanti secondo la metrica 
	 * followed/followers 
	 */
	public List<Long> retriveByPseudodocument(String pseudodocument){
		List<Long> listUser=null;

		if(searcher!=null){

			try{
			   	listUser=this.searcher.searchPseudoDocument(pseudodocument);
			}catch (Exception e) {
					logger.info("errore durante la quersy a lucine");
					e.printStackTrace();
			}
		}else{
			throw new RuntimeException("Il searcher non è stato creato");
		}

		return listUser;
	} 
	

	/**
	 * Recupero la pseudodocument associato al all'utente userid
	 * @param userid
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public String getPseudodocument(long userid) throws ParseException, IOException  {
		String	output = searcher.getPseudodocument(userid);			
		return output;
	}

	
}
		
		
		
		
	
	
	


