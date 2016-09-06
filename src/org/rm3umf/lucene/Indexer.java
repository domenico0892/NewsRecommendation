package org.rm3umf.lucene;





import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Set;

import org.apache.log4j.*;

import org.apache.lucene.document.Field;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.rm3umf.domain.News;
import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;


public class Indexer{

	private IndexWriter writer;
	protected Logger logger = Logger.getLogger("fceval.algorithms.Arru_Zeppi.JaccarSimilality:Index");


	public Indexer(String pathIndex) throws IndexException{
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
	public void index(String idUser,Set<Long> follower,Set<Long> followed,String pseudodocument, List<News> pseudodocument_news, List<News> pseudodocument_news_title, List<News> pseudodocument_news_training) throws IOException{
		// Add some Document objects containing quotes
		logger.debug("prova ad aggiungere document all'indice");
		Document doc = new Document();
		doc.add(new Field("userid",idUser,Field.Store.YES, Field.Index.TOKENIZED)); 
		doc.add(new Field("pseudodocument",pseudodocument,Field.Store.YES,Field.Index.ANALYZED)); 
		for(long l: follower) {
			doc.add(new Field("follower",String.valueOf(l),Field.Store.YES, Field.Index.ANALYZED));
			doc.add(new Field("s7",String.valueOf(l),Field.Store.YES,Field.Index.ANALYZED)); 

		}
		for(long l: followed) {
			doc.add(new Field("followed",String.valueOf(l),Field.Store.YES, Field.Index.ANALYZED));
			doc.add(new Field("s7",String.valueOf(l),Field.Store.YES,Field.Index.ANALYZED)); 
		}
		String s = "";
		for(News n: pseudodocument_news) {
			s = n.getTitle()+n.getDescription()+n.getNewscontent();
			doc.add(new Field("pseudodocument_news",s,Field.Store.YES, Field.Index.ANALYZED));
			doc.add(new Field("s1",s,Field.Store.YES, Field.Index.ANALYZED));

		}
		for(News n: pseudodocument_news_title) {
			s = n.getTitle()+n.getDescription()+n.getNewscontent();
			doc.add(new Field("news_rec",s,Field.Store.YES, Field.Index.ANALYZED));
		}
		for(News n: pseudodocument_news_training) {
			s = n.getTitle()+n.getDescription()+n.getNewscontent();
			doc.add(new Field("s1_training",s,Field.Store.YES, Field.Index.ANALYZED));
		}
		writer.addDocument(doc);

		this.logger.debug("aggiunta pagina all'indice");
	}
	
}
