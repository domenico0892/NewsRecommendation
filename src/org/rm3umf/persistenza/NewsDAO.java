package org.rm3umf.persistenza;

import java.util.List;

import org.rm3umf.domain.Message;
import org.rm3umf.domain.Period;
import org.rm3umf.domain.News;
import org.rm3umf.domain.User;

public interface NewsDAO {
	
	public News doRetrieveNewsById(String id) throws PersistenceException;

	public List<News> doRetrieveNewsInrilenvate(int soglia) throws PersistenceException ;

	public void deleteAll() throws PersistenceException ;

	public void delete(News url) throws PersistenceException ;

	public List<News> doRetrieveAll() throws PersistenceException ;
	
	public News doRetrieveNewsByUrl(String url) throws PersistenceException;
	
	public void deleteByNewsid(String id) throws PersistenceException;
	
	public List<News> doRetrieveNewsByUser(long user) throws PersistenceException;

	public List<News> doRetrieveNewsCompleteByUser(Long user,  List<Period> listaPeriodiTest) throws PersistenceException;

	public List<News> doRetrieveNewsTrainingByUser(long iduser, List<Period> listaPeriodiTest) throws PersistenceException;
}
