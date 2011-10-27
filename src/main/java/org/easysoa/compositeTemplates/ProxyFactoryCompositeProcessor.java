package org.easysoa.compositeTemplates;

import java.util.List;

import org.eclipse.stp.sca.Composite;

public class ProxyFactoryCompositeProcessor implements CompositeTemplateItf {

	@Override
	public String getId() {
		return "Proxy";
	}

	@Override
	public Composite createComposite(String templateName) {
		return null;
	}

	@Override
	public List<String> allAvailableTemplatesLabel() {
		return null;
	}

	@Override
	public String getLabel() {
		return "Proxy";
	}

}
