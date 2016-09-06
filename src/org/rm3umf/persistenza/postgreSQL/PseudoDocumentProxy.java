package org.rm3umf.persistenza.postgreSQL;

import java.util.List;

import org.apache.log4j.Logger;
import org.rm3umf.domain.Message;
import org.rm3umf.domain.PseudoFragment;
import org.rm3umf.persistenza.MessageDAO;
import org.rm3umf.persistenza.PersistenceException;


public class PseudoDocumentProxy extends PseudoFragment{
	
	private static Logger logger = Logger.getLogger(PseudoDocumentProxy.class);
	
	/**
	 * ATTENZIONE : Questo è un proxy particolare perchè non memorizza la lista dei messaggi ma la 
	 * restituisce solo in quanto uno pseudo-document verrà utilizzato solo per costruire le 
	 * signal component non ha senso conserversi tutta la lista dei messaggi in memoria.
	 */
	public List<Message> getMessages() { 
			List<Message> listaMessaggi=null;
			logger.debug("ProxyPseudoDocument called ");
			try {
				listaMessaggi=super.getType().doRetrieveByUserIdAndDate(this.getUser(),this.getPeriod());
			}
			catch (PersistenceException e) {
				throw new RuntimeException(e.getMessage() + "");
			}
//			logger.info("size messages:"+listaMessaggi.size());
//			for(Message m:listaMessaggi){
//				System.err.println(m);
//			}
		return listaMessaggi; 
	}

}
