package org.rm3umf.framework.main;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.rm3umf.domain.User;
import org.rm3umf.framework.buildmodel.BuildModel;
import org.rm3umf.framework.buildmodel.BuildModelException;
import org.rm3umf.framework.buildmodel.extractor.ExtractorException;
import org.rm3umf.persistenza.PersistenceException;


public class Main {
	
	public static void main(String[] args) throws  IOException, PersistenceException, BuildModelException, ExtractorException, NoSuchAlgorithmException{
		
		Logger root = Logger.getRootLogger();
		BasicConfigurator.configure();
		root.setLevel(Level.INFO);
		BuildModel bs=new BuildModel();
 		bs.start();
		
	}
}
