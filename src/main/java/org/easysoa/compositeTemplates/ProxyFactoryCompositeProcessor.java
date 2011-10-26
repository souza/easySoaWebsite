package org.easysoa.compositeTemplates;

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

}
