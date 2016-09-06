package org.rm3umf.framework.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.rm3umf.domain.Signal;
import org.rm3umf.domain.User;
import org.rm3umf.domain.UserModel;
import org.rm3umf.framework.buildmodel.extractor.EntitySignalComponent;
import org.rm3umf.framework.buildmodel.extractor.NewsEntitySignalComponent;
import org.rm3umf.framework.buildmodel.extractor.NewsTopicSignalComponent;
import org.rm3umf.framework.buildmodel.extractor.NewsTypeEntitySignalComponent;
import org.rm3umf.framework.buildmodel.extractor.StrategyExtraction;
import org.rm3umf.framework.buildmodel.extractor.TopicSignalComponent;
import org.rm3umf.framework.buildmodel.extractor.TweetEntitySignalComponent;
import org.rm3umf.framework.buildmodel.extractor.TweetTopicSignalComponent;
import org.rm3umf.framework.buildmodel.extractor.TweetTypeEntitySignalComponent;
import org.rm3umf.framework.buildmodel.extractor.TypeSignalComponent;
import org.rm3umf.framework.eval.Result;
import org.rm3umf.framework.eval.SimilarityFunction;
import org.rm3umf.framework.eval.TwittomenderResultCreator;
import org.rm3umf.framework.eval.ValutationEngine;
import org.rm3umf.framework.eval.Valutator;
import org.rm3umf.framework.eval.ValutationFunction;
import org.rm3umf.framework.eval.similarity.CosineSimilarity;
import org.rm3umf.framework.eval.similarity.CosineSimilarityWithNull;
import org.rm3umf.framework.eval.similarity.FunctionMultiresolution;
import org.rm3umf.framework.eval.similarity.HaarSimilarity;
import org.rm3umf.framework.eval.similarity.JaccardNoWeight;
import org.rm3umf.framework.eval.similarity.PatternSimilarity;
import org.rm3umf.framework.eval.similarity.PatternSimilarity2;
import org.rm3umf.framework.eval.similarity.RandomSimilarity;
import org.rm3umf.framework.eval.similarity.TemporalCosineSimilarity;
import org.rm3umf.framework.eval.similarity.VectorScalarProd;
import org.rm3umf.framework.eval.similarity.VectorSpaceModel;
import org.rm3umf.framework.eval.similarity.VectorSum;
import org.rm3umf.framework.eval.valutation.LuceneFriendFollower;
import org.rm3umf.framework.eval.valutation.MeanKendallTau;
import org.rm3umf.framework.eval.valutation.MeanReciprocalRank;
import org.rm3umf.framework.eval.valutation.MeanReciprocalRank_FF;
import org.rm3umf.framework.eval.valutation.NDCG;
import org.rm3umf.framework.eval.valutation.SuccessAtRankK;
import org.rm3umf.framework.eval.valutation.SuccessAtRank_FF;
import org.rm3umf.math.VectorUtil;
import org.rm3umf.persistenza.AAFacadePersistence;
import org.rm3umf.persistenza.PersistenceException;

import util.FactoryFile;

public class Startup {




	public static void main(String[] args) throws PersistenceException, IOException{

		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		int sizeRecList=10;
		Date dt=new Date();
		dt.toString();

		String modelDesc="dayPerPeriod=7,schemaPesatura=tf*idf,sogliaSegnali=10,sogliaConcept=2,percentualeTraining=70,percentualeTest=30,";



		Calendar gc = GregorianCalendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date=sdf.format(gc.getTime());


		List<StrategyExtraction> lista_criteri = new LinkedList<StrategyExtraction>();
//				lista_criteri.add(new NewsEntitySignalComponent());
//				lista_criteri.add(new NewsTopicSignalComponent());
//				lista_criteri.add(new NewsTypeEntitySignalComponent());
//		lista_criteri.add(new TweetTopicSignalComponent());
//		lista_criteri.add(new TweetTypeEntitySignalComponent());
//		lista_criteri.add(new TopicSignalComponent());
//		lista_criteri.add(new TypeSignalComponent());
		lista_criteri.add(new EntitySignalComponent());
		


		List<SimilarityFunction> listaFunzioniSimilarita=new LinkedList<SimilarityFunction>();


		//valutation

		listaFunzioniSimilarita.add(new RandomSimilarity());
		listaFunzioniSimilarita.add( new VectorSpaceModel());


		listaFunzioniSimilarita.add( new HaarSimilarity(0,0));
		listaFunzioniSimilarita.add( new HaarSimilarity(0,1));
		listaFunzioniSimilarita.add( new HaarSimilarity(0,2));
		listaFunzioniSimilarita.add( new HaarSimilarity(0,3));
		listaFunzioniSimilarita.add( new HaarSimilarity(0,4));
		listaFunzioniSimilarita.add( new HaarSimilarity(0,5));
		listaFunzioniSimilarita.add( new HaarSimilarity(0,6));
		listaFunzioniSimilarita.add( new HaarSimilarity(0,6));

		listaFunzioniSimilarita.add( new HaarSimilarity(0,0));
		listaFunzioniSimilarita.add( new HaarSimilarity(1,1));
		listaFunzioniSimilarita.add( new HaarSimilarity(2,2));
		listaFunzioniSimilarita.add( new HaarSimilarity(3,3));
		listaFunzioniSimilarita.add( new HaarSimilarity(4,4));
		listaFunzioniSimilarita.add( new HaarSimilarity(5,5));
		listaFunzioniSimilarita.add( new HaarSimilarity(6,6));




		listaFunzioniSimilarita.add(new VectorSum(6));
		listaFunzioniSimilarita.add(new VectorSum(5));
		listaFunzioniSimilarita.add(new VectorSum(4));
		listaFunzioniSimilarita.add(new VectorSum(3));
		listaFunzioniSimilarita.add(new VectorSum(2));
		listaFunzioniSimilarita.add(new VectorSum(1));
		listaFunzioniSimilarita.add(new VectorSum(0));
		listaFunzioniSimilarita.add(new FunctionMultiresolution(0,5));
		listaFunzioniSimilarita.add(new FunctionMultiresolution(1,5));
		listaFunzioniSimilarita.add(new FunctionMultiresolution(0,4));
		listaFunzioniSimilarita.add(new FunctionMultiresolution(1,4));
		//		


		listaFunzioniSimilarita.add(new TemporalCosineSimilarity());
		listaFunzioniSimilarita.add(new VectorScalarProd(0));
		listaFunzioniSimilarita.add(new VectorScalarProd(1));
		listaFunzioniSimilarita.add(new VectorScalarProd(2));
		listaFunzioniSimilarita.add(new VectorScalarProd(3));
		listaFunzioniSimilarita.add(new VectorScalarProd(4));
		listaFunzioniSimilarita.add(new VectorScalarProd(5));
		listaFunzioniSimilarita.add(new VectorScalarProd(6));

		listaFunzioniSimilarita.add(new CosineSimilarity());
		listaFunzioniSimilarita.add(new CosineSimilarityWithNull());
		



		/*
		 *Valuto il risultato ottenuto applicado una certa funzione di similarita 
		 */

		List<ValutationFunction> listValFun=new LinkedList<ValutationFunction>();
		ValutationEngine valutationEngine = new ValutationEngine();
		//AnalyzerResult resultAnalyzer  = new  MeanKendallTau(new LuceneFriendFollower());
		//		listValFun.add(new  SuccessAtRankK());
		//		listValFun.add(new   MeanReciprocalRank());
		listValFun.add(new  SuccessAtRank_FF());
		listValFun.add(new   MeanReciprocalRank_FF());
//		listValFun.add(new   NDCG());
		//listValFun.add(new  MeanKendallTau(new LuceneFriendFollower()));






		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy MM dd - hh:mm");
		String date2=sdf2.format(gc.getTime());




		for(StrategyExtraction strategy : lista_criteri) {

			String criterio = strategy.getCriterion();
			//	for(int i=5; i<=30;i=i+5){
			String pathReport="report"+date+"["+modelDesc+" criterio= "+criterio+"].txt";
			String newReport="["+date2+", |recList|="+sizeRecList+"]";
			FactoryFile.getInstance().writeLineOnFile("report"+date+"["+modelDesc+criterio+"].txt", newReport);
			//Creo il modulo di valutazione
			Valutator eval = new Valutator(sizeRecList,listValFun , pathReport);

			//Da commentare se si sperimenta con follower
			//List<UserModel> listTruthSignal = AAFacadePersistence.getInstance().truthsignaldoRetrieveByCriterio(criterio);
	//sperimentazione nostro sistema
			List<UserModel> userModels= AAFacadePersistence.getInstance().userModelRetriveAllWithCriterio(criterio);
//			 sperimentazione per s1
//			List<UserModel> userModels= AAFacadePersistence.getInstance().twittomender_tests1();
//			sperimentazione per s7
//			List<UserModel> userModels= AAFacadePersistence.getInstance().twittomender_tests7();

			/*
			 * twittomender
			 */
			TwittomenderResultCreator twittomenderResultCreator = new TwittomenderResultCreator(10);
			Result test;
			/*	
			Result result2 = twittomenderResultCreator.applyS1Function(userModels,100);
//			xmlFactory.saveResult(result2);
			String resultValutation="S1 : ";
			for(ValutationFunction vf:this.valutationFunction){
				double scoreResult=vf.valutate(result2, null);
				resultValutation+=vf.getNameFunction()+"="+scoreResult+", ";
			}
			FactoryFile.getInstance().writeLineOnFile(this.pathReport,resultValutation );

			Result result3 = twittomenderResultCreator.applyS7Function(userModels,100);

//			xmlFactory.saveResult(result3);
			resultValutation="S7 : ";
			for(ValutationFunction vf:this.valutationFunction){
				double scoreResult=vf.valutate(result3, null);
				resultValutation+=vf.getNameFunction()+"="+scoreResult+", ";
			}
			FactoryFile.getInstance().writeLineOnFile(this.pathReport,resultValutation );
			 */ 

			/*
			 * Per avere la raccomandazione sulle news, ossia 70% di segnali bag-of-signals e 
			 *30% inidicizzato con lucene
			 */
//			test = twittomenderResultCreator.newsRecommender_Test(userModels,100);

			/*
			 * Per avere la raccomandazione sulle news, ossia 70% con s1 e 
			 *30% inidicizzato con lucene
			 */
//				Result	training = twittomenderResultCreator.applyS1TrainingFunction(userModels,100);

			/*
			 * Per avere la raccomandazione sulle news, s7 è sempre lo stesso
			 * perchè indicizza follower e followed, cambio funzione per
			 * regolarmi la lunghezza del ranking del test
			 */
//			Result	training = twittomenderResultCreator.applyS7FunctionTraining(userModels,100);

			//valuta tutte le funzioni di similarita 
			//Da commentare se si sperimenta con follower
			//Result test = eval.valuateUM(new CosineSimilarity(), listTruthSignal);
			for(SimilarityFunction function : listaFunzioniSimilarita){
				//				eval.valutate(function, userModels, test);
				eval.valutate(function, userModels, null, null);
			}
		}
	}
}