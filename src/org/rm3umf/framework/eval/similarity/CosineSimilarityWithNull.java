package org.rm3umf.framework.eval.similarity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.rm3umf.domain.Signal;
import org.rm3umf.domain.UserModel;
import org.rm3umf.framework.eval.SimilarityFunction;
import org.rm3umf.framework.eval.UserModelWithScore;
import org.rm3umf.math.VectorUtil;
import org.rm3umf.math.WaveletUtil;


public class CosineSimilarityWithNull extends SimilarityFunction{

	private static Logger logger = Logger.getLogger(D1Distance.class);
	private String namefunction;
	private int minResolution;
	private int maxResolution;



	public CosineSimilarityWithNull() {
		this.namefunction = "CosineSimilarityWithNull";

	}



	public  double getSimilarity(UserModel u1 , UserModel u2) {

		logger.debug("Calcolo similarità tra:"+u1+"-"+u2);

		//logger.info("calcola similarit� tra utenti "+u1.getUser().getIduser()+"-"+u2.getUser().getIduser());
		if(u1.getSignals() == null) {
			
			return VectorUtil.getInstance().cosineSimilarity(u1.getTruthSignal().getSignal(), u2.getTruthSignal().getSignal());

		} else {
			//lista segnali 1
			List<Signal> list1=u1.getSignals();

			//lista segnali 2
			List<Signal> list2=u2.getSignals();

			int conceptCommon=0;

			// è una matrice che ha in posizione 0 il segnale del primo utente e in posizione 1 il segnale del 
			// secondo utente
			Map<String,double[][]> concept2singnal =  new HashMap<String,double[][]>();

			//Scorro la prima lista
			for(Signal signal:list1){
				String enr_id = signal.getEnrichment().getName();
				double[] array=signal.getSignal();
				//	double[] array2=new double[array.length];
				double[][] signals ={array,null};
				concept2singnal.put(enr_id, signals );
			}

			//Segnali della seconda lista
			for(Signal signal:list2){
				String enr_id=signal.getEnrichment().getName();
				double[] arraySignal=signal.getSignal();
				double[][] signals = concept2singnal.get(enr_id);
				if(signals==null){		
					double[] array2=new double[arraySignal.length];
					signals = new double[2][];
					signals[0]=null;
					concept2singnal.put(enr_id, signals);
				}else{
					conceptCommon++;
				}
				signals[1]=arraySignal;
			}

			int conceptTot = concept2singnal.keySet().size();	

			//ci sarà il totale
			double tot = 0.0;	


			for( String enr_id : concept2singnal.keySet() ){

				double[][] signals = concept2singnal.get(enr_id);

				//se il sengale in comune
				if(signals[0]!=null && signals[1]!=null){
					tot += VectorUtil.getInstance().cosineSimilarity(signals[0], signals[1]);
				}
				//se una delle due è null -> cosine similarity è 0 quindi non faccio niente
			}	
			int denominatore = concept2singnal.keySet().size();
			if(denominatore != 0) {
				tot=tot/denominatore;
			}
			return  tot;
		}
	}


	public static void main(String[] args){
		CosineSimilarityWithNull cs=new CosineSimilarityWithNull();
		double[] array={0.2,0.1,0.1,0.1};
		double[] array1={0.0,0.1,0.1,0.1};

		//System.out.print(tcs.getPeakiness(array));
		System.out.print(VectorUtil.getInstance().cosineSimilarity(array1,array));

	}

	@Override
	public String getNameFunction() {
		return this.namefunction;
	}

	@Override
	public int compare(UserModelWithScore o1, UserModelWithScore o2) {
		return Double.compare(o2.getScore(), o1.getScore());
	}

}
