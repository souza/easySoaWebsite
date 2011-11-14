package org.easysoa.compositeTemplates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osoa.sca.annotations.Init;
import org.osoa.sca.annotations.Reference;

public class CompositeTemplate implements CompositeTemplateProcessorItf {

	@Reference
    protected List<CompositeTemplateItf> templates;
    protected Map<String, CompositeTemplateItf> templateMap;

    @Init
    public final void initializeProcessorsByID() {
        this.templateMap = new HashMap<String, CompositeTemplateItf>();
        for (CompositeTemplateItf compositeTemplate : this.templates) {
            this.templateMap.put(compositeTemplate.getId(), compositeTemplate);
        }
    }

    protected CompositeTemplateItf getProcessorById(String templateName) {
        return this.templateMap.get(templateName);
    }

	@Override
	public String getTemplate(String templateName, Map<String, Object> params) {
		String template = this.getProcessorById(templateName).getTemplate(params);
		System.out.println(template);
		return template;
	}

	@Override
	public List<String> allAvailableTemplatesLabel() {
		List<String> labels = new ArrayList<String>();
		for(CompositeTemplateItf template : this.templates){
			labels.add(template.getLabel());
		}
		return labels;
	}

	@Override
	public String getForm(String templateName) {
		return this.getProcessorById(templateName).getForm();
	}
	
	@Override
	public void doActionAfterCreation(String templateName, Map<String, Object> params){
		this.getProcessorById(templateName).doActionAfterCreation(params);
	}


}
