package org.rm3umf.framework.buildmodel.extractor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.rm3umf.domain.Enrichment;
import org.rm3umf.domain.Message;
import org.rm3umf.domain.News;
import org.rm3umf.domain.NewsEntity;
import org.rm3umf.domain.Period;
import org.rm3umf.domain.PseudoFragment;
import org.rm3umf.domain.SignalComponent;
import org.rm3umf.domain.TweetEntity;
import org.rm3umf.domain.TweetTopic;
import org.rm3umf.domain.User;
import org.rm3umf.persistenza.AAFacadePersistence;
import org.rm3umf.persistenza.PersistenceException;

public class TweetTopicSignalComponent implements StrategyExtraction {

	private static Logger logger = Logger.getLogger(TweetTopicSignalComponent.class);
	private String type = "TweetsTopic";


	@Override
	public List<SignalComponent> extract(PseudoFragment pseudo) throws PersistenceException, ExtractorException {
		List<SignalComponent> result = new LinkedList<SignalComponent>();
		Map<String,SignalComponent> map2tf=new HashMap<String, SignalComponent>();
		SignalComponent sc = null;
		int max = 0;
		for(Message mex: pseudo.getMessages()) {
			max++;
			List<TweetTopic> listaen = AAFacadePersistence.getInstance().tweettopicRetrieveByTweetId(mex.getIdMessage(), this.type);
			for(TweetTopic ne: listaen) {
				String id_en= ne.getName();
				sc=map2tf.get(id_en);
				int occurence;
				if(sc==null){
					sc=new SignalComponent();
					sc.setEnrichment(ne); //setto la news
					sc.setPeriod(pseudo.getPeriod());   //setto il periodo
					sc.setOccorence(0);//
					sc.setType(this.type);
					//setto l'utente
					sc.setUser(pseudo.getUser());
					result.add(sc);
					map2tf.put(id_en,sc);
				}
//				incrementa occurence
				occurence=sc.getOccorence()+ne.getNumberInstance();
				sc.setOccorence(occurence);
			}
		}
		return result;
	}
	
	public String getCriterion() {
		return this.type;
	}

	@Override
	public void exploreResource(boolean exploreResource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Enrichment> filter(int tHRESHOLD) throws PersistenceException {
		return AAFacadePersistence.getInstance().tweettopicfilter(tHRESHOLD);
	}

	@Override
	public void delete(Enrichment enr) throws PersistenceException {
		AAFacadePersistence.getInstance().tweettopicDelete(enr);
	}

	@Override
	public List<Enrichment> retrieveAllEnrichment() throws PersistenceException {
		return AAFacadePersistence.getInstance().tweettopicRetrieveAll();
	}

	@Override
	public Map<String, Enrichment> retrieveEnrichmentUserAndPeriod(User user, int periodid) throws PersistenceException {
		return AAFacadePersistence.getInstance().tweettopicRetrieve4UserAndPeriod(user, periodid);
	}

	@Override
	public List<Message> doRetrieveByUserIdAndDate(User user, Period period)
			throws PersistenceException {
		return AAFacadePersistence.getInstance().tweettopicDAOdoRetrieveByUserIdAndDate(user, period);
	}

}
