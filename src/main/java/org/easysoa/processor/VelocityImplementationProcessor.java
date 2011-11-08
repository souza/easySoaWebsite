package org.easysoa.processor;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.json.simple.JSONObject;
import org.osoa.sca.annotations.Reference;
import org.ow2.frascati.metamodel.web.VelocityImplementation;
import org.ow2.frascati.metamodel.web.WebFactory;

public class VelocityImplementationProcessor implements ComplexProcessorItf {

	@Reference
	protected ImplementationsProcessorItf implementationsProcessor; 
	
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
    	sb.append("<td>");
    	sb.append("<select name=\"implementation-type\" id=\"implementation-type\" size=\"1\" onChange=\"changeImplementation()\">");
    	for(String label : this.implementationsProcessor.allAvailableImplementationsLabel()){
    		if(label.equals(this.getLabel(null))){
    			sb.append("<option selected=\"selected\">"+label+"</option>");
    		}
    		else{
    			sb.append("<option>"+label+"</option>");
    		}
    	}
    	sb.append("</select>");
    	sb.append("</td>");
    	sb.append("</tr>");
    	sb.append("<tr>");
    	sb.append("<td>");
    	sb.append("Location : ");
    	sb.append("</td>");
    	sb.append("<td>");
    	if(velocityImplementation.getLocation()!=null)sb.append("<input type=\"text\" id=\"location\" name=\"location\" size=\"40\" value=\""+velocityImplementation.getLocation()+"\"/>");
    	else sb.append("<input type=\"text\" id=\"location\" name=\"location\" size=\"40\" value=\"\"/>");
    	sb.append("</td>");
    	sb.append("</tr>");
    	sb.append("<tr>");
    	sb.append("<td>");
    	sb.append("Default : ");
    	sb.append("</td>");
    	sb.append("<td>");
    	if(velocityImplementation.getDefault()!=null)sb.append("<input type=\"text\" id=\"default\" name=\"default\" size=\"40\" value=\""+velocityImplementation.getDefault()+"\"/>");
    	else sb.append("<input type=\"text\" id=\"default\" name=\"default\" size=\"40\" value=\"\"/>");
    	sb.append("</td>");
    	sb.append("</tr>");
    	sb.append("</table>");
    	return sb.toString();
	}

	@Override
	public String getActionMenu(EObject eObject) {
		StringBuffer sb = new StringBuffer();
		sb.append("<a onclick=\"action('deleteImplementation')\">Delete</a>");
		return sb.toString();
	}

	@Override
	public EObject saveElement(EObject eObject, Map<String, Object> params) {
		VelocityImplementation implem = (VelocityImplementation)eObject;
		implem.setLocation((String)params.get("location"));
		implem.setDefault((String)params.get("default"));
		return implem;
	}

	@Override
	public EObject getNewEObject(EObject eObject) {
		System.out.println("getNewEObject Velocity");
		VelocityImplementation implementation = WebFactory.eINSTANCE.createVelocityImplementation();
		return implementation;
	}
}
