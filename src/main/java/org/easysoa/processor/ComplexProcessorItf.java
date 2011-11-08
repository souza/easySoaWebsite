package org.easysoa.processor;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.json.simple.JSONObject;
import org.osoa.sca.annotations.Service;

/**
 *
 * @author Michel Dirix
 */
@Service
public interface ComplexProcessorItf{
    
    String getId();
    
    String getLabel(EObject eObject);
    
    JSONObject getMenuItem(EObject eObject, String parentId);
    
    String getPanel(EObject eObject);
    
    String getActionMenu(EObject eObject);
    
    EObject saveElement(EObject eObject, Map<String, Object> params);
    
    EObject getNewEObject(EObject eObject);
}
