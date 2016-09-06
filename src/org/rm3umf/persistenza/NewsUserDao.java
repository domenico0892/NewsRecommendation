package org.rm3umf.persistenza;

import java.text.ParseException;
import java.util.Date;

public interface NewsUserDao {
	
	public void save(String id_news, long id_user,String id_tweet) throws PersistenceException, ParseException;

	public void deleteAll() throws PersistenceException;

}
