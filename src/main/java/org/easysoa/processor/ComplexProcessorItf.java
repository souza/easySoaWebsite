package org.easysoa.processor;

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
    
}
