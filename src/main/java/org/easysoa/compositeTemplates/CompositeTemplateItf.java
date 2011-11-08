package org.easysoa.compositeTemplates;

import java.util.Map;

import org.osoa.sca.annotations.Service;

/**
*
* @author Michel Dirix
*/
@Service
public interface CompositeTemplateItf {

	String getId();

	String getTemplate(Map<String, Object> params);
	
	String getLabel();
	
	String getForm();
	
	
}
