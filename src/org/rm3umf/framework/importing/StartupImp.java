package org.rm3umf.framework.importing;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.rm3umf.domain.Message;
import org.rm3umf.domain.User;
import org.rm3umf.math.VectorUtil;
import org.rm3umf.net.downloader.QueueException;
import org.rm3umf.persistenza.AAFacadePersistence;
import org.rm3umf.persistenza.PersistenceException;
import org.xml.sax.SAXException;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import util.LoadProperties;

/*
 * 
 * Si occupa solo di salvare gli utenti, i follower e i followed relativi all'utente 
 * memoriazza anche i messaggi. (E' un semplice import.)
 * 
 */
public class StartupImp {
	
	public static void main(String[] args) throws DatasetException, PersistenceException, QueueException, IOException, InterruptedException, TwitterException, NoSuchAlgorithmException{

		Logger root = Logger.getRootLogger();
		BasicConfigurator.configure();
		root.setLevel(Level.INFO);
		Importer importing = new Importer();
		//System.out.println(java.lang.Runtime.getRuntime().maxMemory()); 
		importing.start();
	} 
}
