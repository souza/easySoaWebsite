package org.easysoa.compositeTemplates;

import java.util.List;

import org.eclipse.stp.sca.Composite;
import org.eclipse.stp.sca.ScaFactory;


public class EmptyCompositeProcessor implements CompositeTemplateItf {

	@Override
	public String getId() {
		return "Empty";
	}

	@Override
	public Composite createComposite(String templateName) {
		Composite composite = ScaFactory.eINSTANCE.createComposite();
		return composite;
	}

	@Override
	public List<String> allAvailableTemplatesLabel() {
		return null;
	}

	@Override
	public String getLabel() {
		return "Empty";
	}

}
