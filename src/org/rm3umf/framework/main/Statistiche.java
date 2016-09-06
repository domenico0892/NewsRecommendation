package org.rm3umf.framework.main;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.rm3umf.domain.Message;
import org.rm3umf.domain.Signal;
import org.rm3umf.domain.User;
import org.rm3umf.persistenza.AAFacadePersistence;
import org.rm3umf.persistenza.PersistenceException;

public class Statistiche {

	public static void main(String[] args) throws PersistenceException, IOException {

		List<User> listautenti = AAFacadePersistence.getInstance().userRetriveAll();
		
		int totale = 0;
		int min = 10000;
		User umin = null;
		Map<Long, Integer> tweets4user = new HashMap<Long, Integer>();
		for(User u : listautenti) {
			List<Signal> listamex = AAFacadePersistence.getInstance().signalRetriveByUser(u);
//			int mex = listamex.size();
//			totale += mex;
//			if(mex < min) {
//				min = mex;
//				umin=u;
			
//			}
			tweets4user.put(u.getIduser(), listamex.size());
		}
		
		TreeMap<Long, Integer> sort_tweets4user = SortByValue(tweets4user);
		
		int[] valori = new int[sort_tweets4user.values().size()];
		int c = 0;
		for(Integer i : sort_tweets4user.values()) {
			valori[c] = i;
			c++;
		}
		
		BufferedWriter br = new BufferedWriter(new FileWriter("signal4user_ordASC1.csv"));
		StringBuilder sb = new StringBuilder();
		c = 0;
		for(Long i : sort_tweets4user.keySet()) {
			
			sb.append(i + "\t");    
			sb.append(valori[c] + "\t");
			sb.append(c + "\t");   
			sb.append("\n");  
			c++;
		}
		br.write(sb.toString());
		br.close();
		
		
//		if(umin != null)
//			System.out.println("Utente con il minor numero di tweets: "+umin.getIduser()+", " + min);
//		int media = totale/listautenti.size();
//		System.out.println("valore medio di tweets scritti: "+media);
	}
	
	
	public static TreeMap<Long, Integer> SortByValue (Map<Long, Integer> map) {
		ValueComparator vc =  new ValueComparator(map);
		TreeMap<Long,Integer> sortedMap = new TreeMap<Long,Integer>(vc);
		sortedMap.putAll(map);
		return sortedMap;
	}
	
	

}

class ValueComparator implements Comparator<Long> {
	 
    Map<Long, Integer> map;
 
    public ValueComparator(Map<Long, Integer> base) {
        this.map = base;
    }
 
    public int compare(Long a, Long b) {
        if (map.get(a) >= map.get(b)) {
            return 1;
        } else {
            return -1;
        } // returning 0 would merge keys 
    }
}


