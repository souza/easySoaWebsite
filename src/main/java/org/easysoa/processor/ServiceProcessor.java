package org.easysoa.processor;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.stp.sca.Binding;
import org.eclipse.stp.sca.Interface;
import org.eclipse.stp.sca.ScaFactory;
import org.eclipse.stp.sca.ScaPackage;
import org.eclipse.stp.sca.Service;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.osoa.sca.annotations.Reference;

public class ServiceProcessor implements ComplexProcessorItf{

	@Reference
	protected ComplexProcessorItf complexProcessor;
	@Reference
	protected InterfaceProcessorItf interfaceProcessor;
	
	@Override
	public String getId() {
		return ScaPackage.eINSTANCE.getService().getEPackage().getNsURI() + "#" + ScaPackage.eINSTANCE.getService().getName();
	}

	@Override
	public String getLabel(EObject eObject) {
		return "Service";
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getMenuItem(EObject eObject, String parentId) {
		Service service = (Service)eObject;
		JSONObject serviceJSONObject = new JSONObject();
        serviceJSONObject.put("id", "service+"+service.getName());
        serviceJSONObject.put("text", service.getName());
        serviceJSONObject.put("im0", "Service.gif");
        serviceJSONObject.put("im1", "Service.gif");
        serviceJSONObject.put("im2", "Service.gif");
//        JSONArray interfaceArray = new JSONArray();
//        if (service.getInterface() != null) {
//            interfaceArray.add(this.complexProcessor.getMenuItem(service.getInterface(), (String)serviceJSONObject.get("id")));
//        }
//        serviceJSONObject.put("item", interfaceArray);
        JSONArray bindingArray = new JSONArray();
        for (Binding binding : service.getBinding()) {
            bindingArray.add(this.complexProcessor.getMenuItem(binding, (String)serviceJSONObject.get("id")));
        }
        serviceJSONObject.put("item", bindingArray);
        return serviceJSONObject;
	}

	@Override
	public String getPanel(EObject eObject) {
		Service service  = (Service)eObject;
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
	    			sb.append("<tr>");
	    			sb.append("<td>");
	    			sb.append("<div class=\"promote-image\"></div>");
	    			sb.append("Promote : ");
	    			sb.append("</td>");
	    			sb.append("<td colspan=\"2\">");
	    			if(service.getPromote()!=null)sb.append("<input type=\"text\" id=\"promote\" name=\"promote\" size=\"40\" value=\""+service.getPromote()+"\"/><br/>");
	    			else sb.append("<input type=\"text\" id=\"promote\" name=\"promote\" size=\"40\" value=\"\"/><br/>");
	    			sb.append("</td>");
	    			sb.append("</tr>");
	    			sb.append("</table>");
    	sb.append("</div>");
    	return sb.toString();
	}

	@Override
	public String getActionMenu(EObject eObject) {
		StringBuffer sb = new StringBuffer();
		sb.append("<a onclick=\"action('binding')\">Add Binding</a>");
		sb.append("<a onclick=\"action('deleteService')\">Delete</a>");
		sb.append("<input type=\"submit\" value=\"Save\"/input>");
		return sb.toString();
	}

	@Override
	public EObject saveElement(EObject eObject, Map<String, Object> params) {
		Service service = (Service)eObject;
		service.setName((String)params.get("name"));
		service.setPromote((String)params.get("promote"));
		service.setInterface((Interface)this.complexProcessor.saveElement(service.getInterface(), params));
		return service;
	}

	@Override
	public EObject getNewEObject(EObject eObject) {
		return ScaFactory.eINSTANCE.createService();
	}
}
