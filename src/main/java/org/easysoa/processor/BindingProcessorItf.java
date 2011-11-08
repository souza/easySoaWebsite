package org.easysoa.processor;

import java.util.List;

import org.eclipse.stp.sca.Composite;

public interface BindingProcessorItf {

	List<String> allAvailableBindingsLabel();

	String getBindingView(Composite composite, String modelId, String id);
}
