package org.rm3umf.net.enricher;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;

/**
 * Created by domenico on 06/08/16.
 */
public class BoilerpipeService {

    private ArticleExtractor articleExtractor;

    public BoilerpipeService () {
        this.articleExtractor = new ArticleExtractor();
    }
    
    public String getTextFromUrl (String url) {
    	try {
			return this.articleExtractor.getText(url);
		} catch (BoilerpipeProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }

    public String getCleanedText(String text) {
        try {
            return this.articleExtractor.getText(text);
        } catch (BoilerpipeProcessingException e) {
            System.err.println("Errore nel processamento dell'HTML");
        }
        return null;
    }
}
