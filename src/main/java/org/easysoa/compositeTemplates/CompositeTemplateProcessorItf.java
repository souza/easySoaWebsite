package org.easysoa.compositeTemplates;

import java.util.List;
import java.util.Map;

public interface CompositeTemplateProcessorItf {

	String getTemplate(String templateName, Map<String, Object> params);
	
	List<String> allAvailableTemplatesLabel();
	
	String getForm(String templateName);
	
}
