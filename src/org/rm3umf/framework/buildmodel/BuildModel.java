package org.rm3umf.framework.buildmodel;

import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import twitter4j.*;

import org.rm3umf.domain.Period;
import org.rm3umf.domain.PseudoFragment;
import org.rm3umf.domain.Signal;
import org.rm3umf.domain.SignalComponent;
import org.rm3umf.domain.User;
import org.rm3umf.domain.UserModel;
import org.rm3umf.framework.buildmodel.extractor.EntitySignalComponent;
import org.rm3umf.framework.buildmodel.extractor.ExtractorException;
import org.rm3umf.framework.buildmodel.extractor.NewsEntitySignalComponent;
import org.rm3umf.framework.buildmodel.extractor.NewsTopicSignalComponent;
import org.rm3umf.framework.buildmodel.extractor.NewsTypeEntitySignalComponent;
import org.rm3umf.framework.buildmodel.extractor.StrategyExtraction;
import org.rm3umf.framework.buildmodel.extractor.TopicSignalComponent;
import org.rm3umf.framework.buildmodel.extractor.TweetEntitySignalComponent;
import org.rm3umf.framework.buildmodel.extractor.TweetTopicSignalComponent;
import org.rm3umf.framework.buildmodel.extractor.TweetTypeEntitySignalComponent;
import org.rm3umf.framework.buildmodel.extractor.TypeSignalComponent;
import org.rm3umf.lucene.FacadeLucene;
import org.rm3umf.persistenza.AAFacadePersistence;
import org.rm3umf.persistenza.MessageDAO;
import org.rm3umf.persistenza.PersistenceException;


/**
 * Questa classe del framework costruisce i segnali in base al paramentro DAYPERIOD.
 * Inoltre � sempre in questo componente in cui vengono estratti i concept a partire dagli pseudoDocument 
 * Inoltre questo modulo potr� essere rilanciato pi� volte nel ciclo di vita dell'applicazione per valutare 
 * come cambiano le performace scegliendo un intervallo di tempo differente per la costruzione dei SignalComponent
 * 
 * ATTENZIONE:prima di avviare questo modulo � necessario che sia stato ultimato l'importing
 * 
 * @author Giulz
 *
 */


public class BuildModel {


	/*PARAMETRI*/

	//giorni con cui si costruiscono i segnali
	private final int DAYPERIOD=1;  //giorni del periodo 

	//rappresenta il numero minimo di segnali che rendono un profilo utente valido	
	private int SOGLIASEGNALI = 1;  //al di sotto di questa soglia di segnali il profilo utente viene scartato

	private int SOGLIACONCEPT = 2;  //al di sotto di questa soglia di concept il concept e tutti i sig comp 
	//vengono scartati
	
	private double trainingPeriod = 0.24; //prendo il 70% dei periodi per la fase di training
	private double testPeriod = 0.76; //prendo il 30% dei periodi per la fase di test

	//VARIABILI ISTANZA
	private List<User> listaUser;
	private BuiltSignalComponent signalComponetCreator;
	private List<StrategyExtraction> listaEnrichment;


	public BuildModel() throws PersistenceException{
		//
		this.listaUser=AAFacadePersistence.getInstance().userRetriveAll();
	
		listaEnrichment = new LinkedList<StrategyExtraction>();
//		listaEnrichment.add(new NewsEntitySignalComponent());
//		listaEnrichment.add(new NewsTopicSignalComponent());
//		listaEnrichment.add(new NewsTypeEntitySignalComponent());
//		listaEnrichment.add(new TweetEntitySignalComponent());
//		listaEnrichment.add(new TweetTopicSignalComponent());
//		listaEnrichment.add(new TweetTypeEntitySignalComponent());
//		listaEnrichment.add(new TopicSignalComponent());
//		listaEnrichment.add(new TypeSignalComponent());
		listaEnrichment.add(new EntitySignalComponent());
		
		this.signalComponetCreator=new BuiltSignalComponent(listaEnrichment);

	}

	public void start () throws  PersistenceException, ExtractorException, BuildModelException, NoSuchAlgorithmException{

		/*
		 *=============================================
		 * FASE -1 : Preparo il DB 
		 *=============================================
		 * */

		/*prepara il database*/
		//COMMENTA X ESEGUIRE VELOCE
//		AAFacadePersistence.getInstance().prepareDBBuilderSignal(listaEnrichment);

		/*
		 *=============================================
		 * FASE 1.0 : Creazione dei periodi 
		 *=============================================
		 * */

		//FILTRO PERIODI
		FactoryPeriod factrotyPeriod=new FactoryPeriod();

		//recupero la massima e minima data di pubblicazione dei tweets
//		String startDate=AAFacadePersistence.getInstance().periodGetMinDate();
//		String startDate = "2010-11-01";
		//Per news recommender
		String startDate = "2010-11-10";
		String endDate=AAFacadePersistence.getInstance().periodGetMaxDate();


		//creo la lista dei periodi in base alla data massima e minima dei tweet
		List<Period> listaPeriodi=factrotyPeriod.createPeriods(startDate,endDate,this.DAYPERIOD);
		double countPeriodTraining = listaPeriodi.size()*this.trainingPeriod;
		int periodi_training = (int) Math.round(countPeriodTraining);
		List<Period> listaPeriodiTraining = listaPeriodi.subList(0, periodi_training);
		List<Period> listaPeriodiTest = listaPeriodi.subList(periodi_training, listaPeriodi.size());

		/*
		 *=============================================
		 * FASE 1.5 :  Eliminiano dal sistema le signal component inrrilevanti
		 *=============================================
		 * */

		/* Elimino i signal componente relativi ad un concept referenziato solo una volta
		 * perchè probabilmente sono relativi ad errori o cmq non sono rilevanti 
		 */
//		FilterSignalComponent filterSigComp = new FilterSignalComponent();
//		filterSigComp.filter(SOGLIACONCEPT,this.listaEnrichment);

		
		/*
		 *=============================================
		 * FASE 1.9 : creazione delle signal component 
		 *=============================================
		 * */

		//Crea le signal component relativi a tutti i periodi

		//COMMENTA X ESEGUIRE VELOCE
//		signalComponetCreator.createSignalComponent(listaPeriodiTraining, listaPeriodiTest, SOGLIACONCEPT);

		
		/*
		 *===================================
		 * FASE 2 : creazione dei segnali 
		 *===================================
		 * */

//		List<Period> listaPeriodi = AAFacadePersistence.getInstance().periodRetriveAll();
//		AAFacadePersistence.getInstance().signalDelete();
//		BuiltSignal signalCreator = new BuiltSignal(listaPeriodiTraining.size(), SOGLIASEGNALI, 1); //smoothing
//		signalCreator.buildSignal(listaUser, listaEnrichment);

		/*
		 *===================================
		 * FASE 3 : creazione dell'indice
		 *===================================
		 * */

		List<UserModel> modelliUtente = AAFacadePersistence.getInstance().userModelRetriveAll(); 
		IndexPreparator indexPreparator = new IndexPreparator("./index");
		indexPreparator.prepareIndex(modelliUtente, listaPeriodiTest);

		/*
		 *===================================
		 * FASE 3 : creazione dell'indice
		 *===================================
		 * */
	}

}