package org.rm3umf.persistenza.mongodb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.rm3umf.persistenza.DataSource;
import org.rm3umf.persistenza.PersistenceException;
import org.rm3umf.persistenza.postgreSQL.DataSourcePostgreSQL;

import com.mongodb.MongoClient;

public class MongoConnection {

		   private static MongoConnection instance = null;
		   private MongoConnection() {}
		   
		   public static MongoConnection getInstance() {
		      if(instance == null) {
		         instance = new MongoConnection();
		      }
		      return instance;
		   }
		   
			public MongoClient getConnection() {
				return new MongoClient();
			}
}