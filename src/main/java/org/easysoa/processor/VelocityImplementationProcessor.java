package org.easysoa.processor;

import org.eclipse.emf.ecore.EObject;
import org.json.simple.JSONObject;
import org.osoa.sca.annotations.Reference;
import org.ow2.frascati.metamodel.web.VelocityImplementation;

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
        implemObject.put("id", parentId+"+implementation");
        implemObject.put("text", velocityImplementation.getLocation()+"/"+velocityImplementation.getLocation());
        implemObject.put("im0", "Implementation.gif");
        implemObject.put("im1", "Implementation.gif");
        implemObject.put("im2", "Implementation.gif");
        return implemObject;
	}

	@Override
	public String getPanel(EObject eObject) {
		VelocityImplementation velocityImplementation = (VelocityImplementation)eObject;
    	StringBuffer sb = new StringBuffer();
    	sb.append("<tr>");
    	sb.append("<td>");
    	sb.append("<div class=\"velocity-implementation-image\"></div>");
    	sb.append("Implementation : ");
    	sb.append("</td>");
    	sb.append("<td>");
    	sb.append("<input type=\"text\" id=\"implementation\" name=\"implementation\" size=\"40\" value=\""+velocityImplementation.getLocation()+"/"+velocityImplementation.getLocation()+"\"/>");
    	sb.append("</td>");
    	sb.append("<td>");
    	sb.append("<select name=\"implementation-type\" id=\"implementation-type\" size=\"1\">");
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
    	return sb.toString();
	}

	@Override
	public String getActionMenu(EObject eObject) {
		StringBuffer sb = new StringBuffer();
		sb.append("<a onclick=\"action('deleteImplementation')\">Delete</a>");
		return sb.toString();
	}
}
