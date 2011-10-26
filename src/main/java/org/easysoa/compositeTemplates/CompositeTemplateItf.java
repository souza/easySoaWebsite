package org.easysoa.compositeTemplates;

import org.eclipse.stp.sca.Composite;
import org.osoa.sca.annotations.Service;

/**
*
* @author Michel Dirix
*/
@Service
public interface CompositeTemplateItf {

	String getId();

	Composite createComposite(String templateName);
	
}
