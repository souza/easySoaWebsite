package org.easysoa.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.stp.sca.ScaPackage;
import org.json.simple.JSONObject;
import org.osoa.sca.annotations.Init;
import org.osoa.sca.annotations.Reference;
import org.osoa.sca.annotations.Scope;

/**
 *
 * @author Michel Dirix
 */
@Scope("Composite")
public class ComplexProcessor implements ComplexProcessorItf {

    @Reference
    protected List<ComplexProcessorItf> processors;
    protected Map<String, ComplexProcessorItf> processorMap;

    @Init
    public final void initializeProcessorsByID() {
        this.processorMap = new HashMap<String, ComplexProcessorItf>();
        for (ComplexProcessorItf complexProcessor : this.processors) {
            this.processorMap.put(complexProcessor.getId(), complexProcessor);
        }
    }

    protected ComplexProcessorItf getProcessorById(EObject eObject) {
        return this.processorMap.get(eObject.eClass().getEPackage().getNsURI() + "#" + eObject.eClass().getName());
    }
    
    public String getLabel(EObject eObject) {
        return getProcessorById(eObject).getLabel(eObject);       
    }
    
    
    public JSONObject getMenuItem(EObject eObject,String parentId) {
        return getProcessorById(eObject).getMenuItem(eObject,parentId);
    }
    
    public String getPanel(EObject eObject) {
        return getProcessorById(eObject).getPanel(eObject);
    }

    @Override
    public String getId() {
        return null;
    }

	@Override
	public String getActionMenu(EObject eObject) {
		return getProcessorById(eObject).getActionMenu(eObject);
	}

	@Override
	public EObject saveElement(EObject eObject, Map<String, Object> params) {
		return getProcessorById(eObject).saveElement(eObject, params);
	}

	@Override
	public EObject getNewEObject(EObject eObject) {
		return getProcessorById(eObject).getNewEObject(eObject);
	}

}

