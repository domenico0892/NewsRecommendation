package org.rm3umf.framework.buildmodel;

import java.util.List;

import org.rm3umf.domain.News;
import org.rm3umf.domain.PseudoFragment;
import org.rm3umf.domain.SignalComponent;
import org.rm3umf.framework.buildmodel.extractor.ExtractorException;
import org.rm3umf.persistenza.PersistenceException;

public interface EnrichmentSignalComponent {

	public void createEnrichmentSignalComponent(List<SignalComponent> sigComp);
	
	public List<SignalComponent> extractFromNews(PseudoFragment pseudo) throws PersistenceException, ExtractorException;
}
