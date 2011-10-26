package org.easysoa.compositeTemplates;

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

}
