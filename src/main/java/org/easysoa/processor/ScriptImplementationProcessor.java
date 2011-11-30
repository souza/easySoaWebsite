package org.easysoa.processor;

import java.util.Map;

import org.easysoa.api.ServiceManager;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.stp.sca.domainmodel.frascati.FrascatiFactory;
import org.eclipse.stp.sca.domainmodel.frascati.ScriptImplementation;
import org.json.simple.JSONObject;
import org.osoa.sca.annotations.Reference;

/**
 *
 * @author Michel Dirix
 */
public class ScriptImplementationProcessor implements ComplexProcessorItf {

	@Reference
	protected ImplementationsProcessorItf implementationsProcessor; 
	@Reference 
	protected ServiceManager serviceManager;
	
	@Override
    public String getId() {
		return "http://frascati.ow2.org/xmlns/sca/1.1#ScriptImplementation";
    }
	
	@Override
    public String getLabel(EObject eObject) {
        return "Script";
    }
	
    @SuppressWarnings("unchecked")
	@Override
    public JSONObject getMenuItem(EObject eObject, String parentId) {
        ScriptImplementation implementation = (ScriptImplementation)eObject;
        JSONObject implemObject = new JSONObject();
        implemObject.put("id", "+implementation");
        implemObject.put("text", implementation.getScript());
        implemObject.put("im0", "Implementation.gif");
        implemObject.put("im1", "Implementation.gif");
        implemObject.put("im2", "Implementation.gif");
        return implemObject;
    }

    @Override
    public String getPanel(EObject eObject) {
    	ScriptImplementation scriptImplementation = null;
    	if(eObject != null)	scriptImplementation = (ScriptImplementation)eObject;
    	else scriptImplementation = (ScriptImplementation)this.getNewEObject(null);
    	StringBuffer sb = new StringBuffer();
    	sb.append("<table id=\"implementation-panel\">");
    	sb.append("<tr>");
    	sb.append("<td>");
    	sb.append("<div class=\"java-implementation-image\"></div>");
    	sb.append("Implementation : ");
    	sb.append("</td>");
    	sb.append("<td name=\"implementation-type\" id=\"implementation-type\" size=\"1\">");
    	sb.append("Script");
    	sb.append("</td>");
    	sb.append("</tr>");
    	sb.append("<tr>");
    	sb.append("<td>");
    	sb.append("Source : ");
    	sb.append("</td>");
    	sb.append("<td>");
    	if(scriptImplementation.getScript()!=null)sb.append("<input type=\"text\" id=\"implementation\" name=\"implementation\" size=\"40\" value=\""+scriptImplementation.getScript()+"\"/>");
    	else sb.append("<input type=\"text\" id=\"implementation\" name=\"implementation\" size=\"40\" value=\"\"/>");
    	sb.append("</td>");
    	sb.append("</tr>");
    	sb.append("<tr>");
    	sb.append("<td colspan=\"2\">");
    	sb.append("<div id=\"editor\"");
    	sb.append("</div>");
    	sb.append("</td>");
    	sb.append("</tr>");
    	sb.append("</table>");
    	if(scriptImplementation.getScript()!=null){
    		String url = this.serviceManager.isFileInApplication(scriptImplementation.getScript());
    		this.implementationsProcessor.setUrl(url);
    		if(url!=null){
    			this.implementationsProcessor.setEditorMode("javascript");
    		}
    	}
    	else{
    		this.implementationsProcessor.setUrl(null);
    	}
    	return sb.toString();
    }

	@Override
	public String getActionMenu(EObject eObject) {
		StringBuffer sb = new StringBuffer();
		sb.append("<a onclick=\"action('deleteImplementation')\">Delete</a>");
		sb.append("<input type=\"submit\" value=\"Save\"/input>");
		return sb.toString();
	}

	@Override
	public EObject saveElement(EObject eObject, Map<String, Object> params) {
		ScriptImplementation implem = (ScriptImplementation)eObject;
		implem.setScript((String)params.get("implementation"));
		return implem;
	}

	@Override
	public EObject getNewEObject(EObject eObject) {
		System.out.println("getNewEObject script");
		return FrascatiFactory.eINSTANCE.createScriptImplementation();
	}
	
}
