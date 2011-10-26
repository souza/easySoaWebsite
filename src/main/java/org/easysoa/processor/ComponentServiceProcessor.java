package org.easysoa.processor;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.stp.sca.Binding;
import org.eclipse.stp.sca.ComponentService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.osoa.sca.annotations.Reference;

public class ComponentServiceProcessor implements ComplexProcessorItf {

	@Reference
	protected ComplexProcessorItf complexProcessor;
	@Reference
	protected InterfaceProcessorItf interfaceProcessor;
	
	@Override
	public String getId() {
		return "http://www.osoa.org/xmlns/sca/1.0#ComponentService";
	}

	@Override
	public String getLabel(EObject eObject) {
		return "Component Service";
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getMenuItem(EObject eObject, String parentId) {
		ComponentService service =(ComponentService)eObject;
		JSONObject serviceObject = new JSONObject();
        serviceObject.put("id", parentId+"+service+"+service.getName());
        serviceObject.put("text", service.getName());
        serviceObject.put("im0", "ComponentService.gif");
        serviceObject.put("im1", "ComponentService.gif");
        serviceObject.put("im2", "ComponentService.gif");
        JSONArray serviceArray = new JSONArray();
//        if (service.getInterface() != null) {
//            serviceArray.add(this.complexProcessor.getMenuItem(service.getInterface(), (String)serviceObject.get("id")));
//        }
        for (Binding binding : service.getBinding()) {
            serviceArray.add(this.complexProcessor.getMenuItem(binding,(String)serviceObject.get("id")));
        }
        serviceObject.put("item", serviceArray);
        return serviceObject;
	}

	@Override
	public String getPanel(EObject eObject) {
		ComponentService service = (ComponentService)eObject;
		StringBuffer sb = new StringBuffer();
    	sb.append("<div class=\"component_frame_line\">");
    		sb.append("<form>");
    		sb.append("<table>");
    		sb.append("<tr>");
    		sb.append("<td>");
	    			sb.append("<div class=\"service-image\"></div>");
	    			sb.append("Name : ");
	    			sb.append("</td>");
	    			sb.append("<td colspan=\"2\">");
	    			sb.append("<input type=\"text\" id=\"name\" name=\"name\" value=\""+service.getName()+"\"/><br/>");
	    			sb.append("</td>");
	    			sb.append("</tr>");
	    			if(service.getInterface()!=null){
	    				sb.append(this.complexProcessor.getPanel(service.getInterface()));
	    			}
	    			else{
	    				sb.append("<tr>");
	    				sb.append("<td>");
	    				sb.append("<div class=\"java-interface-image\"></div>");
	    				sb.append("Interface : ");
	    				sb.append("</td>");
	    				sb.append("<td>");
	    		    	sb.append("<input type=\"text\" id=\"interface\" name=\"interface\" size=\"40\" value=\"\"/>");
	    		    	sb.append("</td>");
	    		    	sb.append("<td>");
	    		    	sb.append("<select name=\"interface-type\" id=\"interface-type\" size=\"1\">");
	    		    	for(String label : this.interfaceProcessor.allAvailableInterfacesLabel()){
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
	    			}
	    			sb.append("</table>");
    		sb.append("</form>");
    	sb.append("</div>");
    	return sb.toString();
	}

	@Override
	public String getActionMenu(EObject eObject) {
		StringBuffer sb = new StringBuffer();
		sb.append("<a onclick=\"action('addBinding')\">Add Binding</a>");
		sb.append("<a onclick=\"action('deleteComponentService')\">Delete</a>");
		return sb.toString();
	}

}