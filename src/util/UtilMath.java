package util;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.rm3umf.framework.eval.UserModelWithScore;

import twitter4j.internal.logging.Logger;

public class UtilMath {


	private static Logger logger = Logger.getLogger(UtilText.class);


	public static double log_base2 (double x) {

		double numeratore = Math.log(x);
		double denominatore = Math.log(2);

		return numeratore/denominatore;
	}

	public static <K extends Comparable,V extends Comparable> Map<K,V> sortMapByValues(Map<K,V> map){
		List<Map.Entry<K,V>> entries = new LinkedList<Map.Entry<K,V>>(map.entrySet());

		Collections.sort(entries, new Comparator<Map.Entry<K,V>>() {

			@Override
			public int compare(Entry<K, V> o1, Entry<K, V> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		Map<K,V> sortedMap = new LinkedHashMap<K,V>();

		for(Map.Entry<K,V> entry: entries){
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

	public static List<UserModelWithScore> sortListByScore(List<UserModelWithScore> list) {
		if (list.size() > 0) {
			Collections.sort(list, new Comparator<UserModelWithScore>() {
				@Override
				public int compare(UserModelWithScore o1, UserModelWithScore o2) {
					if(o1.getScore() < o2.getScore())
						return 1;
					else 
						return -1;
				}
			});
		}
		return list;
	}

	public static void main(String[] args){

		Map<String, Integer> olympic2012 = new HashMap<String, Integer>();

		olympic2012.put("England", 3);
		olympic2012.put("USA", 1);
		olympic2012.put("China", 2);
		olympic2012.put("Russia", 4);

		Map<String, Integer> sorted = sortMapByValues(olympic2012);

		for(String s: sorted.keySet()){
			System.out.println(s+": "+sorted.get(s)+"\n");
		}
	}
}
