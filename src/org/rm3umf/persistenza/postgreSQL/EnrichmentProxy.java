package org.rm3umf.persistenza.postgreSQL;

import java.util.List;

import org.apache.log4j.Logger;
import org.rm3umf.domain.Enrichment;
import org.rm3umf.domain.News;
import org.rm3umf.persistenza.NewsDAO;
import org.rm3umf.persistenza.PersistenceException;



public class EnrichmentProxy extends Enrichment{
	
	private Logger logger = Logger.getLogger("persistenza.postgreSQL.EnrichmentProxy");
	private boolean caricato = false;
	
//	public String getNews() { 
//        
//        logger.info("NewsProxy called");
//		if (!this.caricato) {
//			NewsDAO dao = new NewsDAOpostgreSQL();
//			try {
//				this.setUrl(dao.doRetrieveUrlById(this.getId()));
//				this.caricato = true;
//			}
//			catch (PersistenceException e) {
//				throw new RuntimeException(e.getMessage() + "");
//			}
//		}
//		return super.getUrl(); 
//	}

}
