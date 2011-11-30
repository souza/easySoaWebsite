package org.easysoa.processor;

import java.util.Map;

import org.easysoa.api.ServiceManager;
import org.eclipse.emf.ecore.EObject;
import org.json.simple.JSONObject;
import org.osoa.sca.annotations.Reference;
import org.ow2.frascati.metamodel.web.VelocityImplementation;
import org.ow2.frascati.metamodel.web.WebFactory;

public class VelocityImplementationProcessor implements ComplexProcessorItf {

	@Reference
	protected ImplementationsProcessorItf implementationsProcessor; 
	@Reference 
	protected ServiceManager serviceManager;
	
	@Override
	public String getId() {
		return "http://frascati.ow2.org/xmlns/web/1.0#VelocityImplementation";
	}

	@Override
	public String getLabel(EObject eObject) {
		return "Velocity";
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getMenuItem(EObject eObject, String parentId) {
		VelocityImplementation velocityImplementation = (VelocityImplementation)eObject;
        JSONObject implemObject = new JSONObject();
        implemObject.put("id", "+implementation");
        implemObject.put("text", velocityImplementation.getLocation()+"/"+velocityImplementation.getDefault());
        implemObject.put("im0", "Implementation.gif");
        implemObject.put("im1", "Implementation.gif");
        implemObject.put("im2", "Implementation.gif");
        return implemObject;
	}

	@Override
	public String getPanel(EObject eObject) {
		System.out.println("VelocityImplementation panel");
		VelocityImplementation velocityImplementation = null;
		if(eObject!=null) velocityImplementation = (VelocityImplementation)eObject;
		else velocityImplementation = (VelocityImplementation)this.getNewEObject(null);
    	StringBuffer sb = new StringBuffer();
    	sb.append("<table id=\"implementation-panel\">");
    	sb.append("<tr>");
    	sb.append("<td>");
    	sb.append("<div class=\"velocity-implementation-image\"></div>");
    	sb.append("Implementation : ");
    	sb.append("</td>");
    	sb.append("<td name=\"implementation-type\" id=\"implementation-type\" size=\"1\">");
    	sb.append("Velocity");
    	sb.append("</td>");
    	sb.append("</tr>");
    	sb.append("<tr>");
    	sb.append("<td>");
    	sb.append("Location/Default : ");
    	sb.append("</td>");
    	sb.append("<td>");
    	sb.append("<input type=\"text\" id=\"implementation\" name=\"implementation\" size=\"40\" value=\""+velocityImplementation.getLocation()+"/"+velocityImplementation.getDefault()+"\"/>");
    	sb.append("</td>");
    	sb.append("</tr>");
    	sb.append("<tr>");
    	sb.append("<td colspan=\"2\">");
    	sb.append("<div id=\"editor\"");
    	sb.append("</div>");
    	sb.append("</td>");
    	sb.append("</tr>");
    	sb.append("</table>");
    	if(velocityImplementation.getLocation()!=null && velocityImplementation.getDefault()!=null){
    		String url = this.serviceManager.isFileInApplication(velocityImplementation.getLocation()+"."+velocityImplementation.getDefault());
    		this.implementationsProcessor.setUrl(url);
    		if(url!=null){
    			this.implementationsProcessor.setEditorMode("java");
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
		VelocityImplementation implem = (VelocityImplementation)eObject;
		String implementation = (String)params.get("implementation");
		String[] implemParams = implementation.split("/");
		implem.setLocation(implemParams[0]);
		implem.setDefault(implemParams[1]);
		return implem;
	}

	@Override
	public EObject getNewEObject(EObject eObject) {
		System.out.println("getNewEObject Velocity");
		VelocityImplementation implementation = WebFactory.eINSTANCE.createVelocityImplementation();
		return implementation;
	}
}
