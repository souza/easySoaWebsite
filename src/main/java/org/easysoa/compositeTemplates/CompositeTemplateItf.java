package org.easysoa.compositeTemplates;

import java.util.List;

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
	
	List<String> allAvailableTemplatesLabel();
	
	String getLabel();
	
}
