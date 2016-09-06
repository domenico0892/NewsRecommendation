package org.rm3umf.framework.eval;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.rm3umf.domain.TruthSignal;
import org.rm3umf.domain.UserModel;
import org.rm3umf.persistenza.AAFacadePersistence;
import org.rm3umf.persistenza.PersistenceException;
import org.rm3umf.xml.FactoryXml;

import util.FactoryFile;

/**
 * Permette di valutare una funzione di similarità in base alle funzioni di valutazione 
 * specificate
 * 
 * @author giulz
 *
 */



public class Valutator {

	private  int dimBuffer ; 
	private List<ValutationFunction> valutationFunction;
	private String pathReport;

	public Valutator(int dimBuffer,List<ValutationFunction> valutationFunction, String pathReport){
		this.valutationFunction=valutationFunction;
		this.dimBuffer=dimBuffer;
		this.pathReport=pathReport;

	}

	/**
	 * Vorrei 
	 * @param function
	 * @throws PersistenceException
	 * @throws IOException
	 */
	public  void valutate(SimilarityFunction function, List<UserModel> userModels, Result test, Result training) throws PersistenceException, IOException{

		//		FactoryXml xmlFactory=new FactoryXml(); 

		/*
		 * Creo e appplico la funzione di similarità ottenendo il result relativo alla fun di sim applicata
		 */

		/*
		 * twittomender
		 */


//		TwittomenderResultCreator twittomenderResultCreator = new TwittomenderResultCreator(10);
		/*	
		Result result2 = twittomenderResultCreator.applyS1Function(userModels,100);
//		xmlFactory.saveResult(result2);
		String resultValutation="S1 : ";
		for(ValutationFunction vf:this.valutationFunction){
			double scoreResult=vf.valutate(result2, null);
			resultValutation+=vf.getNameFunction()+"="+scoreResult+", ";
		}
		FactoryFile.getInstance().writeLineOnFile(this.pathReport,resultValutation );

		Result result3 = twittomenderResultCreator.applyS7Function(userModels,100);

//		xmlFactory.saveResult(result3);
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
//		test = twittomenderResultCreator.newsRecommender_Test(userModels,100);

		/*
		 * Per avere la raccomandazione sulle news, ossia 70% con s1 e 
		 *30% inidicizzato con lucene
		 */
		//		test = twittomenderResultCreator.applyS1TrainingFunction(userModels,100);

		/*
		 * Per avere la raccomandazione sulle news, s7 è sempre lo stesso
		 * perchè indicizza follower e followed, cambio funzione per
		 * regolarmi la lunghezza del ranking del test
		 */
		//				test = twittomenderResultCreator.applyS7FunctionTraining(userModels,100);


		 training = valuateUM(function, userModels);
		//		xmlFactory.saveResult(training);

		String resultValutation=function.getNameFunction()+" : ";
		//Valuta risultato
		for(ValutationFunction vf:this.valutationFunction){
			double scoreResult=vf.valutate(training, test);
			resultValutation+=vf.getNameFunction()+"="+scoreResult+", ";
		}


		FactoryFile.getInstance().writeLineOnFile(this.pathReport,resultValutation );

/*
		String ranking = "\n\nRanking dell'utente Training: 183317130\n ";
		long user = 183317130;
		Map<Long,Double> lista = training.getBestUsers(user);
		if(lista != null){
			for(Long x : lista.keySet()) {
				ranking += x+": "+lista.get(x)+"\n";
			}
		}

		FactoryFile.getInstance().writeLineOnFile(this.pathReport,ranking );


		String ranking2 = "\n\nRanking dell'utente Test: 183317130\n ";

		Map<Long,Double> lista2 = test.getBestUsers(user);
		if(lista2 != null){
			for(Long x : lista2.keySet()) {
				ranking2 += x+": "+lista2.get(x)+"\n";
			}
		}


		FactoryFile.getInstance().writeLineOnFile(this.pathReport,ranking2 );
*/

	}

	public Result valuateUM(SimilarityFunction function, List<UserModel> userModels) throws PersistenceException {
		//visualizza i modelli utente recuperati 
		for(UserModel usermodel: userModels){
			System.out.println("Recuperato rappresentazione utente : "+usermodel);
		}

		ApplicatorFunction applicator = new ApplicatorFunction(dimBuffer);		
		//Applica la funzione di similarità ad un insieme di UM e costruisco il risultato
		applicator.setSimilarityFunction(function);
		Result result=applicator.apply(userModels); // limito ai primi 100 gli utenti da inserire nel result
		return result;
	}






}
