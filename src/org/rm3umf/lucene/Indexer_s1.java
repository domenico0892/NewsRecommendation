package org.rm3umf.lucene;


import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.*;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.rm3umf.domain.User;


public class Indexer_s1 implements Indexer{

	private IndexWriter writer;
	protected Logger logger = Logger.getLogger("fceval.algorithms.Arru_Zeppi.JaccarSimilality:Index_s1");


	public Indexer_s1(String pathIndex) throws IndexException{
		logger.info("creo l'indice");
		File f=new File(pathIndex);
		if(!f.exists()){
			this.logger.error("non esiste la directory dove creare l'inice");
			throw new IndexException("non esiste la directory in cui creare l'indice");
		}
		
		String idx=pathIndex;
		
		
		// Make an writer to create the index
		try{
			
			PerFieldAnalyzerWrapper aWrapper=new PerFieldAnalyzerWrapper(new WhitespaceAnalyzer());
			aWrapper.addAnalyzer("userid", new WhitespaceAnalyzer());
			aWrapper.addAnalyzer("follower", new WhitespaceAnalyzer());
			aWrapper.addAnalyzer("followed", new WhitespaceAnalyzer());
			aWrapper.addAnalyzer("link", new WhitespaceAnalyzer());
			aWrapper.addAnalyzer("pseudodocument", new StandardAnalyzer());
			
			this.writer = new IndexWriter(idx, aWrapper , true);
			this.logger.info("creato IndexerWriter nel path "+ idx);
		}catch(IOException e){
			throw new IndexException("errore durante la creazione dell'IndexWriter");

		}
	}

/**
 * Chiudi indice
 */
	
	public void close(){
		try {
			writer.optimize();
			writer.close();
			this.logger.info("chiuso IndexWriter");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	/**
	 * Metodo che inserisce un document nell'indice
	 * @param html
	 * @param links
	 * @throws IOException
	 */
	public void index(List list) throws IOException{
		// Add some Document objects containing quotes
		logger.debug("prova ad aggiungere document all'indice");
		writer.addDocument(createDocument(list));
		this.logger.debug("aggiunta pagina all'indice");
	}

	/**
	 * Creo il documento da aggiungere all'indice
	 * @param html
	 * @param links
	 * @return
	 */
	public  Document createDocument(List list){
		Document doc = new Document();
		doc.add(new Field("userid",(String) list.get(0),Field.Store.YES, Field.Index.TOKENIZED)); 
		//aggiungo il campo links
		doc.add(new Field("follower",(String)list.get(1),Field.Store.YES, Field.Index.ANALYZED));
		doc.add(new Field("followed",(String)list.get(2),Field.Store.YES,Field.Index.ANALYZED)); 
		doc.add(new Field("pseudodocument",(String)list.get(3),Field.Store.YES,Field.Index.ANALYZED)); 
		doc.add(new Field("pseudodocument_news",(String)list.get(4),Field.Store.YES,Field.Index.ANALYZED)); 
		return doc;
	}

}
