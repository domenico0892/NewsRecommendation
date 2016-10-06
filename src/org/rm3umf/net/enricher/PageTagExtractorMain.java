package org.rm3umf.net.enricher;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Created by ai-lab on 27/07/16.
 */
public class PageTagExtractorMain {

	public static void main(String[] args) {
		Logger root = Logger.getRootLogger();
		BasicConfigurator.configure();
		root.setLevel(Level.INFO);
		PageTagExtractor p = new PageTagExtractor();
		Method[] methods = PageTagExtractor.class.getMethods();
		Scanner s = new Scanner(System.in);
		System.out.print("Nome operazione > ");
		String n = s.nextLine();
		for (Method m : methods) {
			if (m.getName().equals(n))
				try {
					m.invoke(p);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
}