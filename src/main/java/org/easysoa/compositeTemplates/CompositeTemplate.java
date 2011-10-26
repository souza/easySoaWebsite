package org.easysoa.compositeTemplates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.stp.sca.Composite;
import org.osoa.sca.annotations.Init;
import org.osoa.sca.annotations.Reference;

public class CompositeTemplate implements CompositeTemplateItf {

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
    public String getId() {
        return null;
    }

	@Override
	public Composite createComposite(String templateName) {
		return this.templateMap.get(templateName).createComposite(templateName);
	}
}
