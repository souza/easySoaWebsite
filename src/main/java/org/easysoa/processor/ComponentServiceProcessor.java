package org.easysoa.processor;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.stp.sca.Binding;
import org.eclipse.stp.sca.ComponentService;
import org.eclipse.stp.sca.Interface;
import org.eclipse.stp.sca.ScaFactory;
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
        serviceObject.put("id", "+service+"+service.getName());
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
    		sb.append("<table>");
    		sb.append("<tr>");
    		sb.append("<td>");
	    			sb.append("<div class=\"service-image\"></div>");
	    			sb.append("Name : ");
	    			sb.append("</td>");
	    			sb.append("<td colspan=\"2\">");
	    			if(service.getName()!=null)sb.append("<input type=\"text\" id=\"name\" name=\"name\" value=\""+service.getName()+"\"/><br/>");
	    			else sb.append("<input type=\"text\" id=\"name\" name=\"name\" value=\"\"/><br/>");
	    			sb.append("</td>");
	    			sb.append("</tr>");
	    			sb.append("</table>");
	    			if(service.getInterface()!=null){
	    				sb.append(this.complexProcessor.getPanel(service.getInterface()));
	    			}
	    			else{
	    				sb.append("<table>");
	    				sb.append("<tr>");
	    				sb.append("<td>");
	    				sb.append("<div class=\"java-interface-image\"></div>");
	    				sb.append("Interface : ");
	    				sb.append("</td>");
	    				sb.append("<td>");
	    		    	sb.append("<input type=\"text\" id=\"interface\" name=\"interface\" size=\"40\" value=\"\"/>");
	    		    	sb.append("</td>");
	    		    	sb.append("<td>");
	    		    	sb.append("<select name=\"interface-type\" id=\"interface-type\" size=\"1\" onChange=\"changeInterface()\">");
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
    	sb.append("</div>");
    	return sb.toString();
	}

	@Override
	public String getActionMenu(EObject eObject) {
		StringBuffer sb = new StringBuffer();
		sb.append("<a onclick=\"action('addBinding')\">Add Binding</a>");
		sb.append("<a onclick=\"action('deleteComponentService')\">Delete</a>");
		sb.append("<input type=\"submit\" value=\"Save\"/input>");
		return sb.toString();
	}

	@Override
	public EObject saveElement(EObject eObject, Map<String, Object> params) {
		ComponentService componentService = (ComponentService)eObject;
		componentService.setName((String)params.get("name"));
		componentService.setInterface((Interface)this.complexProcessor.saveElement(componentService.getInterface(), params));
		return componentService;
	}

	@Override
	public EObject getNewEObject(EObject eObject) {
		return ScaFactory.eINSTANCE.createComponentService();
	}

}
