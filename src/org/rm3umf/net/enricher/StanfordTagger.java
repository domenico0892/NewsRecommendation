package org.rm3umf.net.enricher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;

public class StanfordTagger {
	public static void main (String[] args) {

		
		Map<String, String> m = getEntities("Donald Trump is the official Republican Party candidate for the White House.");
		for (String k : m.keySet()) {
			System.out.println(k + " : " + m.get(k));
		}
	}
	
	public static Map<String, String> getEntities (String text) {
		Document doc = new Document(text);
		List<Sentence> s = doc.sentences();
		HashMap<String, String> map = new HashMap<>();
		int i;
		for (Sentence c : s) {
			String entity = "";
			String ner = "";
			i = 0;
			while (i < c.lemmas().size()) {
				entity = "";
				ner = "";
				while (!c.nerTag(i).equals("O")) {
					if (entity.equals(""))
						entity = c.lemma(i);
					else
						entity = entity + " " + c.lemma(i); 
					ner = c.nerTag(i);
					i++;
				}
				i++;
				if (!entity.equals(""))
					map.put(entity, ner);
			}
		}
		return map;
	}
}	