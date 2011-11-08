package org.easysoa.processor;

import java.util.List;

import org.eclipse.stp.sca.Composite;
import org.osoa.sca.annotations.Service;

@Service
public interface ImplementationsProcessorItf {

	List<String> allAvailableImplementationsLabel();
	
	String getImplementationView(Composite composite, String modelId, String id);
	
}
