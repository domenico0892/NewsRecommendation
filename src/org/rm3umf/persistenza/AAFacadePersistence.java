package org.rm3umf.persistenza;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.rm3umf.domain.*;
import org.rm3umf.framework.buildmodel.extractor.StrategyExtraction;
import org.rm3umf.persistenza.postgreSQL.*;


public class AAFacadePersistence {

	private static AAFacadePersistence instance;
	private MessageDAO messageDAO;
	private UserDAO userDAO;
	private PeriodDAO periodDAO;
	private NewsDAO newsDAO;
	private SignalComponentDAO signalComponentDAO;
	private SignalDAO signalDAO;
	private NewsEntityDAO newsentityDAO;
	private NewsTopicDAO newstopicDAO;
	private TweetsEntityDAO tweetsentityDAO;
	private TweetsTopicDAO tweetstopicDAO;
	private PseudoDocumentDAO pseudoDocumentDAO;
	private MessageEnrichmentDAO message_enrichmentDAO;
	private NewsUserDao news_userDAO;
	private TruthSignalDAO truthsignalDAO;
	private TweetsEntityDAO tweetstypeentityDAO;
	private TopicDAO topicDao;
	private TypeDAO typeDao;
	private EntityDAO entityDao;
	private NewsTypeEntityDAO newstypeentityDAO;

	public AAFacadePersistence(){
		this.messageDAO=new MessageDAOpostgreSQL();
		this.userDAO=new UserDAOpostgreSQL();
		this.periodDAO=new PeriodDAOpostgreSQL();
		this.newsDAO=new NewsDAOpostgreSQL() ;
		this.signalComponentDAO=new SignalComponentDAOpostgreSQL();
		this.signalDAO=new SignalDAOpostgreSQL();
		this.pseudoDocumentDAO=new PseudoDocumentDAOpostgreSQL();
		this.newsentityDAO = new NewsEntityDAOpostgreSQL();
		this.newstopicDAO = new NewsTopicDAOpostgreSQL();
		this.tweetsentityDAO = new TweetsEntityDAOpostgreSQL();
		this.tweetstopicDAO = new TweetsTopicDAOpostgreSQL();
		this.message_enrichmentDAO = new MessageEnrichmentDAOpostgreSQL();
		this.news_userDAO = new NewsUserDAOpostgreSQL();
		this.truthsignalDAO = new TruthSignalDAOpostgreSQL();
		this.tweetstypeentityDAO = new TweetsTypeEntityDAOpostgreSQL();
		this.topicDao = new TopicDAOpostgreSQL();
		this.typeDao = new TypeDAOpostgreSQL();
		this.entityDao = new EntityDAOpostgreSQL();
		this.newstypeentityDAO = new NewsTypeEntityDAOpostgrSQL();
	}

	public synchronized static AAFacadePersistence getInstance(){
		if(instance==null)
			instance=new AAFacadePersistence();
		return instance;	
	}



	/**
	 * Prepara il db alla fase di costruzione dei segnali
	 * @throws PersistenceException
	 */
	public void prepareDBBuilderSignal(List<StrategyExtraction> listaEnrichment)throws PersistenceException{		
		AAFacadePersistence.getInstance().periodDelete();
		for(StrategyExtraction s : listaEnrichment){
		AAFacadePersistence.getInstance().signalComponentDeleteAll(s.getCriterion());
		AAFacadePersistence.getInstance().signalDelete(s.getCriterion());
		AAFacadePersistence.getInstance().truthsignaldeleteAll(s.getCriterion());
		}
	}

	public void prepareDBBuilderSignal4User(User u)throws PersistenceException{		
		AAFacadePersistence.getInstance().signalComponentDelete4User(u);
		AAFacadePersistence.getInstance().signalDelete4User(u);
	}

	/**
	 * Prepara il db alla fase di importing 
	 */
	public void prepareDBImporting()throws PersistenceException{
		//		userDeleteFollowed();
		//		userDeleteFollower();
		//		signalComponentDeleteAll();
		//		signalDelete();
		//		resourceDelete();
		//newsDeleteAll();
		//		periodDelete();
		//		messageDelete();
		//		userDelete();
	}

	/**
	 * USER
	 */
	public void userSave(User user) throws PersistenceException{
		userDAO.save(user);
	}

	public List<User> userRetriveAll() throws PersistenceException{
		return userDAO.doRetrieveAll();
	}

	public void userSaveFollowed(User user, Set<Long> listFollowed) throws PersistenceException{
		for(Long l:listFollowed){
			userDAO.saveFollowed(user.getIduser(),l);
		}
	}

	public void userSaveFollower(User user, Set<Long> listFollower) throws PersistenceException{
		for(Long l:listFollower){
			userDAO.saveFollower(user.getIduser(),l);
		}
	}

	public Set<Long> userGetFollowed(long userid) throws PersistenceException{
		return userDAO.retriveFollowedById(userid);
	}

	public Set<Long> userGetFollower(long userid) throws PersistenceException{
		return userDAO.retriveFollowerById(userid);
	}


	public void userDeleteFollowed() throws PersistenceException{
		userDAO.deleteFollowed();
	}

	public void userDeleteFollower() throws PersistenceException{
		userDAO.deleteFollower();
	}

	public void userDelete() throws PersistenceException{
		userDAO.deleteFollower();
		userDAO.deleteFollowed();
		userDAO.delete();
	}
	
	public List<UserModel> twittomender_tests1() throws PersistenceException{
		return this.userDAO.twittomender_tests1();
	}
	
	public List<UserModel> twittomender_tests7() throws PersistenceException{
		return this.userDAO.twittomender_tests7();
	}

	/**
	 *MESSAGE 
	 */

	public void messageSave(Message message) throws PersistenceException{
		this.messageDAO.save(message);
	}

	public List<Message> messageRetriveByUser(User u) throws PersistenceException{
		return messageDAO.retriveByUser(u);
	}

	public void messageDelete() throws PersistenceException{
		messageDAO.delete();
	}
	
	public String messageretriveJsonTextFromId(String id) throws PersistenceException {
		return messageDAO.retriveJsonTextFromId(id);
	}

	/**
	 * PERIOD
	 */
	public void periodDelete() throws PersistenceException{
		this.periodDAO.delete();		
	}

	public String periodGetMaxDate() throws PersistenceException{ 
		return	periodDAO.getMaxDate();
	}

	public String periodGetMinDate() throws PersistenceException{ 
		return	periodDAO.getMinDate();
	}

	public void periodSave(Period period) throws PersistenceException{
		this.periodDAO.save(period);
	}

	public List<Period> periodRetriveAll() throws PersistenceException{
		return this.periodDAO.doRetriveAll();
	}

	public String periodGetMaxDate4User(User u) throws PersistenceException{ 
		return this.periodDAO.getMaxDate4User(u);
	}

	public String periodGetMinDate4User(User u) throws PersistenceException{ 
		return this.periodDAO.getMinDate4User(u);
	}

	/**
	 * News
	 */
	/**
	 * Salva le news
	 */

	/**
	 * Cancella tutta la relazione news
	 */
	public void newsDeleteAll() throws PersistenceException{
		newsDAO.deleteAll();
	}

	/**
	 * Recupera le news a partire dall'url
	 */
	public News doRetrieveNewsByUrl(String url) throws PersistenceException {
		return newsDAO.doRetrieveNewsByUrl(url);
	}
	
	
	public List<News> newsRetriveByUser(Long user) throws PersistenceException {
		return newsDAO.doRetrieveNewsByUser(user);
	}
	
	
	public List<News> newsCompleteRetriveByUser(Long user,  List<Period> listaPeriodiTest) throws PersistenceException {
		return newsDAO.doRetrieveNewsCompleteByUser(user, listaPeriodiTest);
	}
	
	public List<News> newsTrainingRetriveByUser(long iduser,List<Period> listaPeriodiTest) throws PersistenceException {
		return newsDAO.doRetrieveNewsTrainingByUser(iduser, listaPeriodiTest);
	}
	

	/**
	 * Recupera tutti i news presenti nel DB
	 */
	public List<News> newsRetrieveAll() throws PersistenceException{
		return newsDAO.doRetrieveAll();
	}

	/**
	 *Cancella il news passato come paramentro 
	 */
	public void newsDelete(News news) throws PersistenceException{
		newsDAO.delete(news);
	}

	/**
	 *Recupera news che sono stati referenzaiti meno della soglai threshold 
	 */
	public List<News> newsRetriveInrilevante(int threshold) throws PersistenceException{
		return newsDAO.doRetrieveNewsInrilenvate(threshold);
	}

	/**
	 * PSEUDO-DOCUMENT
	 */
	public PseudoFragment pseudoDocumentGet(User user,Period period) throws PersistenceException {
		return pseudoDocumentDAO.doRetrieve(user, period);
	}

	public List<PseudoFragment> pseudoDocumentGetByPeriod(Period period) throws PersistenceException{
		return pseudoDocumentDAO.doRetriveByPeriod( period);
	}

	public List<PseudoFragment> pseudoDocumentGetByPeriodAndUser(Period period, User u) throws PersistenceException {
		return pseudoDocumentDAO.doRetriveByPeriodUser( period, u);
	}


	/**
	 * SIGNAL COMPONENT
	 */

	/**
	 *Salva il signalComponent passato come parametro  
	 * @throws NoSuchAlgorithmException 
	 */
	public void signalComponentSave(SignalComponent sigComp) throws PersistenceException, NoSuchAlgorithmException{
		//salvo e aggiorno il signalComponent (se � gia salvato si incremtea di uno il contatore nella relazione)
		//newsDAO.save(sigComp.getNews()); //salvo il news se non presente
		signalComponentDAO.save(sigComp);
	}

	/**
	 * Recupera tutti i SignalComponent per User
	 */
	public List<SignalComponent> signaComponentRetriveByUserAndPeriod(User user, int start, int end) throws PersistenceException{
		return signalComponentDAO.doRetrieveByUseridAndPeriod(user.getIduser(), start, end);
	}
	
	public List<SignalComponent> signaComponentRetriveByUser(User user, String s, int periodo) throws PersistenceException{
		return signalComponentDAO.doRetrieveByUserid(user.getIduser(), s, periodo);
	}

	/**
	 * Recupera tutti i SignalComponent per Period
	 */
	public List<SignalComponent> signalComponentRetriveByPeriod(Period period) throws PersistenceException{
		return signalComponentDAO.doRetrieveByPeriodid(period.getIdPeriodo());
	}

	/**
	 * Recupera tutti i SignalComponent per News
	 */
	public List<SignalComponent> signalComponentRetriveByNews(Enrichment enrichment) throws PersistenceException{
		return signalComponentDAO.doRetrieveByEnrichmentid(enrichment.getName());
	}

	/**
	 * Cancello il SignalComponent passato come parametro
	 */
	public void signalComponentDelete(SignalComponent signalComponent) throws PersistenceException{
		signalComponentDAO.delete(signalComponent);
	}

	public void signalComponentDeleteAll(String c) throws PersistenceException{
		signalComponentDAO.deleteAll(c);
	}
	/**
	 * Cancella SignalComponent per concepid
	 */
	public void signalComponentDeleteByEnrichment(Enrichment enrichment, String type) throws PersistenceException{
		signalComponentDAO.deleteByEnrichmentid(enrichment.getName(), type);
	}

	private void signalComponentDelete4User(User u) throws PersistenceException {
		this.signalComponentDAO.deleteAll4User(u);
	}
	
	public Map<String, Long> signalComponentUserIdTypeMap(long iduser, String criterion) throws PersistenceException {
		return this.signalComponentDAO.doRetrieveByUseridReturnMap(iduser, criterion);
	}

	/**
	 * SIGNAL
	 */
	public void signalSave(Signal signal) throws PersistenceException{
		this.signalDAO.save(signal);
	}

	public void signalDelete(String c) throws PersistenceException{
		this.signalDAO.deleteAll(c);
	}

	public List<Signal> signalRetriveByNewsNotUser(User user, News news) throws PersistenceException{
		return this.signalDAO.signalRetriveByNewsNotUser(user,	news);
	}
	
	public List<Signal> signalRetriveByUser(User user) throws PersistenceException{
		return this.signalDAO.signalRetriveByUser(user);
	}
	
	private void signalDelete4User(User u) throws PersistenceException {
		this.signalDAO.deleteAll4user(u);
	}


	/**
	 * USER MODEL
	 */

	public UserModel userModelRetriveByUser(User user) throws PersistenceException{
		List<Signal> listSignal=signalDAO.doRetrieveByUser(user);
		UserModel um=new UserModel(user, listSignal);
		return um;
	}

	/**
	 * Recupera tutti gli user model presenti nel sistema (cioe tutti gli um relativi ad utenti che hanno almeno un 
	 * segnale
	 */
	public List<UserModel> userModelRetriveAll() throws PersistenceException{
		List<Long> listUsers=signalDAO.retriveUserid();
		List<UserModel> listUserRappresentation=new LinkedList<UserModel>();
		//per ogni utente devo recuperare la rappresentazione utente composta dall'insieme di segnali
		for(int i=0 ; i<listUsers.size();i++ ){

			long userid = listUsers.get(i);
			//recupero l'utente
			User u=userDAO.doRetriveUserById(userid);
			System.out.print("recupero user rappresentation di:"+u.getIduser());
			//recupero tutti i segnali per l'utente u
			List<Signal> listSignal=signalDAO.doRetrieveByUser(u);
			//creo l'oggetto user rappresentation relativo all'utente u
			UserModel ur=new UserModel(u, listSignal);
			System.out.println("->"+ur);
			listUserRappresentation.add(ur);
		}
		return listUserRappresentation;
	}

	public List<UserModel> userModelRetriveAllWithCriterio(String criterio) throws PersistenceException{
		List<Long> listUsers=signalDAO.retriveUseridWithCriterio(criterio);
		List<UserModel> listUserRappresentation=new LinkedList<UserModel>();
		//per ogni utente devo recuperare la rappresentazione utente composta dall'insieme di segnali
		for(int i=0 ; i<listUsers.size();i++ ){

			long userid = listUsers.get(i);
			//recupero l'utente
			User u=userDAO.doRetriveUserById(userid);
			System.out.print("recupero user rappresentation di:"+u.getIduser());
			//recupero tutti i segnali per l'utente u
			List<Signal> listSignal=signalDAO.doRetrieveByUserAndCriterio(u, criterio);
			//creo l'oggetto user rappresentation relativo all'utente u
			UserModel ur=new UserModel(u, listSignal);
			System.out.println("->"+ur);
			listUserRappresentation.add(ur);
		}
		return listUserRappresentation;
	}

	/**
	 * Cancella lo UserModel passato come parametro (in pratica cancella i segnali)
	 */
	public void userModelDelete(UserModel um) throws PersistenceException{
		List<Signal> signals = um.getSignals();
		for(Signal s:signals)
			signalDAO.delete(s);
	}


	/**
	 * NEWSENTITY
	 * @throws PersistenceException 
	 */


	public List<NewsEntity> newsentityRetrieveByNewsId(String newsId, String type) throws PersistenceException {
		return this.newsentityDAO.getNewsEntityFromNewsId(newsId, type);
	}
	
	
	public List<Enrichment> newsEntityfilter(int tHRESHOLD) throws PersistenceException {
		return this.newsentityDAO.filter(tHRESHOLD);
	}

	public void newsEntityDelete(Enrichment enr) throws PersistenceException {
		this.newsentityDAO.delete(enr);
		
	}
	
	public List<Enrichment> newsEntityRetrieveAll() throws PersistenceException {
		return this.newsentityDAO.retrieveAll();
	}

	public  Map<String, Enrichment> newsEntityRetrieve4UserAndPeriod(User user, int periodid) throws PersistenceException {
		return this.newsentityDAO.retrieve4UserAndPeriod(user, periodid);
	}
	
	public List<Message> newsEntitydoRetrieveByUserIdAndDate(User user, Period period) throws PersistenceException {
		return this.newsentityDAO.doRetrieveByUserIdAndDate(user, period);
	}
	

	/**
	 * NEWSTOPIC
	 * @throws PersistenceException 
	 */
	
	public List<Message> newsTopicdoRetrieveByUserIdAndDate(User user, Period period) throws PersistenceException {
		return this.newstopicDAO.doRetrieveByUserIdAndDate(user, period);
	}

	public List<NewsTopic> newstopicRetrieveByNewsId(String newsId, String type) throws PersistenceException {
		return this.newstopicDAO.getNewsTopicFromNewsId(newsId, type);
	}
	
	public List<Enrichment> newsTopicfilter(int tHRESHOLD) throws PersistenceException {
		return this.newstopicDAO.filter(tHRESHOLD);
	}

	public void newsTopicDelete(Enrichment enr) throws PersistenceException {
		this.newstopicDAO.delete(enr);
		
	}
	
	public List<Enrichment> newsTopicRetrieveAll() throws PersistenceException {
		return this.newstopicDAO.retrieveAll();
	}

	public  Map<String, Enrichment> newsTopicRetrieve4UserAndPeriod(User user, int periodid) throws PersistenceException {
		return this.newstopicDAO.retrieve4UserAndPeriod(user, periodid);
	}

	/**
	 * TWEETENTITY
	 * @throws PersistenceException 
	 */
	
	
	public List<Message> tweetsentityDAOdoRetrieveByUserIdAndDate(User user, Period period) throws PersistenceException {
		return this.tweetsentityDAO.doRetrieveByUserIdAndDate(user, period);
	}

	public List<TweetEntity> tweetentityRetrieveByTweetId(String tweetId, String type) throws PersistenceException {
		return this.tweetsentityDAO.getTweetEntityFromTweetId(tweetId, type);
	}
	
	public List<Enrichment> tweetentityfilter(int tHRESHOLD) throws PersistenceException {
		return this.tweetsentityDAO.filter(tHRESHOLD);
	}

	public void tweetentityDelete(Enrichment enr) throws PersistenceException {
		this.tweetsentityDAO.delete(enr);
		
	}
	
	public List<Enrichment> tweetentityRetrieveAll() throws PersistenceException {
		return this.tweetsentityDAO.retrieveAll();
	}

	public  Map<String, Enrichment> tweetentityRetrieve4UserAndPeriod(User user, int periodid) throws PersistenceException {
		return this.tweetsentityDAO.retrieve4UserAndPeriod(user, periodid);
	}


	/**
	 * TWEETTOPIC
	 * @throws PersistenceException 
	 */


	public List<Message> tweettopicDAOdoRetrieveByUserIdAndDate(User user, Period period) throws PersistenceException {
		return this.tweetstopicDAO.doRetrieveByUserIdAndDate(user, period);
	}

	
	public List<TweetTopic> tweettopicRetrieveByTweetId(String tweetId, String type) throws PersistenceException {
		return this.tweetstopicDAO.getTweetTopicFromTweetId(tweetId, type);
	}
	
	public List<Enrichment> tweettopicfilter(int tHRESHOLD) throws PersistenceException {
		return this.tweetstopicDAO.filter(tHRESHOLD);
	}

	public void tweettopicDelete(Enrichment enr) throws PersistenceException {
		this.tweetstopicDAO.delete(enr);
		
	}
	
	public List<Enrichment> tweettopicRetrieveAll() throws PersistenceException {
		return this.tweetstopicDAO.retrieveAll();
	}

	public  Map<String, Enrichment> tweettopicRetrieve4UserAndPeriod(User user, int periodid) throws PersistenceException {
		return this.tweetstopicDAO.retrieve4UserAndPeriod(user, periodid);
	}

	/**
	 * MESSAGE ENRICHMENT
	 * @throws PersistenceException 
	 */

	public void messageEnrichmentSave(int id_enr, String id_mes, String type) throws PersistenceException {
		this.message_enrichmentDAO.saveMessEnr(id_enr, id_mes, type);
	}
	
	private void messageEnrichmentDelete() throws PersistenceException {
		this.message_enrichmentDAO.deleteAll();
		
	}
	
	/**
	 * NEWS MESSAGE
	 * @throws PersistenceException 
	 * @throws ParseException 
	 */
	
	public void newsUserSave(String id_news, long id_user, String id_tweet) throws PersistenceException, ParseException {
		this.news_userDAO.save(id_news, id_user, id_tweet);		
	}
	
	private void newsMessageDelete() throws PersistenceException {
		this.news_userDAO.deleteAll();
		
	}
	
	/**
	 * TRUTHSIGNAL
	 * @throws PersistenceException 
	 */
	
	public void truthsignalsave(TruthSignal signal) throws PersistenceException {
		this.truthsignalDAO.save(signal);
	}
	
	public void truthsignaldeleteAll(String c) throws PersistenceException {
		this.truthsignalDAO.deleteAll(c);
	}
	
	public void truthsignaldelete(TruthSignal signal) throws PersistenceException {
		this.truthsignalDAO.delete(signal);
	}
	
	public List<UserModel> truthsignaldoRetrieveByCriterio(String criterio) throws PersistenceException {
		return this.truthsignalDAO.doRetrieveByCriterio(criterio);
	}
	
	public TruthSignal truthsignaldoRetrieveByUserAndCriterio(User user, String criterio) throws PersistenceException {
		return this.truthsignalDAO.doRetrieveByUserAndCriterio(user,criterio);
	}
	
	public List<TruthSignal> truthsignaldoRetrieveByUser(User user) throws PersistenceException {
		return this.truthsignalDAO.doRetrieveByUser(user);
	}
	
	public void truthsignaldelete4user(User u) throws PersistenceException {
		this.truthsignalDAO.delete4user(u);
	}
	
	
	/**
	 * TWEETENTITY TYPE
	 * @throws PersistenceException 
	 */
	
	
	public List<Message> tweetstypeentityDAOdoRetrieveByUserIdAndDate(User user, Period period) throws PersistenceException {
		return this.tweetstypeentityDAO.doRetrieveByUserIdAndDate(user, period);
	}

	public List<TweetEntity> tweettypeentityRetrieveByTweetId(String tweetId, String type) throws PersistenceException {
		return this.tweetstypeentityDAO.getTweetEntityFromTweetId(tweetId, type);
	}
	
	public List<Enrichment> tweettypeentityfilter(int tHRESHOLD) throws PersistenceException {
		return this.tweetstypeentityDAO.filter(tHRESHOLD);
	}

	public void tweettypeentityDelete(Enrichment enr) throws PersistenceException {
		this.tweetstypeentityDAO.delete(enr);
		
	}
	
	public List<Enrichment> tweettypeentityRetrieveAll() throws PersistenceException {
		return this.tweetstypeentityDAO.retrieveAll();
	}

	public  Map<String, Enrichment> tweettypeentityRetrieve4UserAndPeriod(User user, int periodid) throws PersistenceException {
		return this.tweetstypeentityDAO.retrieve4UserAndPeriod(user, periodid);
	}

	
	/**
	 * NewsENTITY TYPE
	 * @throws PersistenceException 
	 */
	
	
	public List<Message> newstypeentityDAOdoRetrieveByUserIdAndDate(User user, Period period) throws PersistenceException {
		return this.newstypeentityDAO.doRetrieveByUserIdAndDate(user, period);
	}

	public List<NewsEntity> newstypeentityRetrieveByNewsId(String newsId, String type) throws PersistenceException {
		return this.newstypeentityDAO.getNewsEntityFromNewsId(newsId, type);
	}
	
	public List<Enrichment> newstypeentityfilter(int tHRESHOLD) throws PersistenceException {
		return this.newstypeentityDAO.filter(tHRESHOLD);
	}

	public void newstypeentityDelete(Enrichment enr) throws PersistenceException {
		this.newstypeentityDAO.delete(enr);
		
	}
	
	public List<Enrichment> newstypeentityRetrieveAll() throws PersistenceException {
		return this.newstypeentityDAO.retrieveAll();
	}

	public  Map<String, Enrichment> newstypeentityRetrieve4UserAndPeriod(User user, int periodid) throws PersistenceException {
		return this.newstypeentityDAO.retrieve4UserAndPeriod(user, periodid);
	}

	
	/**
	 * Topic
	 * @throws PersistenceException 
	 */
	
	public List<Enrichment> topicRetrieveByTweetId(String tweetId) throws PersistenceException {
		return this.topicDao.getTopicFromTweetId(tweetId);
	}

	public List<Enrichment> TopicRetrieveByTopic(String topic) throws PersistenceException{
		return this.topicDao.retrieveByTopic(topic);
	}
	
	public List<Enrichment> topicRetrieveAll() throws PersistenceException{
		return this.topicDao.retrieveAll();
	}
	
	public Map<String, Enrichment> topicRetrieve4UserAndPeriod(User user, int periodid) throws PersistenceException{
		return this.topicDao.retrieve4UserAndPeriod(user, periodid);
	}
	
	public List<Message> topicDAOdoRetrieveByUserIdAndDate(User user,Period period) throws PersistenceException {
		return this.topicDao.doRetrieveByUserIdAndDate(user, period);
	}
	
	/**
	 * Type
	 * @throws PersistenceException 
	 */
	
	public List<Enrichment> typeRetrieveByTweetId(String tweetId) throws PersistenceException {
		return this.typeDao.getTypeFromTweetId(tweetId);
	}

	public List<Enrichment> typeRetrieveByTopic(String topic) throws PersistenceException{
		return this.typeDao.retrieveByType(topic);
	}
	
	public List<Enrichment> typeRetrieveAll() throws PersistenceException{
		return this.typeDao.retrieveAll();
	}
	
	public Map<String, Enrichment> typeRetrieve4UserAndPeriod(User user, int periodid) throws PersistenceException{
		return this.typeDao.retrieve4UserAndPeriod(user, periodid);
	}
	
	public List<Message> typeDAOdoRetrieveByUserIdAndDate(User user,Period period) throws PersistenceException {
		return this.typeDao.doRetrieveByUserIdAndDate(user, period);
	}
	
	/**
	 * Entity
	 * @throws PersistenceException 
	 */
	
	public List<Enrichment> entityRetrieveByTweetId(String tweetId) throws PersistenceException {
		return this.entityDao.getEntityFromTweetId(tweetId);
	}

	public List<Enrichment> entityRetrieveByTopic(String topic) throws PersistenceException{
		return this.entityDao.retrieveByEntity(topic);
	}
	
	public List<Enrichment> entityRetrieveAll() throws PersistenceException{
		return this.entityDao.retrieveAll();
	}
	
	public Map<String, Enrichment> entityRetrieve4UserAndPeriod(User user, int periodid) throws PersistenceException{
		return this.entityDao.retrieve4UserAndPeriod(user, periodid);
	}
	
	public List<Message> entityDAOdoRetrieveByUserIdAndDate(User user,Period period) throws PersistenceException {
		return this.entityDao.doRetrieveByUserIdAndDate(user, period);
	}

}
