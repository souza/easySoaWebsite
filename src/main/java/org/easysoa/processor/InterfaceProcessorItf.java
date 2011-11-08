package org.easysoa.processor;

import java.util.List;

import org.eclipse.stp.sca.Composite;

public interface InterfaceProcessorItf {

	List<String> allAvailableInterfacesLabel();
	
	String getInterfaceView(Composite composite, String modelId, String id);
}
