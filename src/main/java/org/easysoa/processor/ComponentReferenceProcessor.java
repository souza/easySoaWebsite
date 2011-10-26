package org.easysoa.processor;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.stp.sca.Binding;
import org.eclipse.stp.sca.ComponentReference;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.osoa.sca.annotations.Reference;

public class ComponentReferenceProcessor implements ComplexProcessorItf {

	@Reference
	protected ComplexProcessorItf complexProcessor;
	@Reference
	protected InterfaceProcessorItf interfaceProcessor;
	
	@Override
	public String getId() {
		return "http://www.osoa.org/xmlns/sca/1.0#ComponentReference";
	}

	@Override
	public String getLabel(EObject eObject) {
		return "Component Reference";
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getMenuItem(EObject eObject, String parentId) {
		ComponentReference componentReference = (ComponentReference)eObject;
		JSONObject referenceObject = new JSONObject();
        referenceObject.put("id", parentId+"+reference+"+componentReference.getName());
        referenceObject.put("text", componentReference.getName());
        referenceObject.put("im0", "ComponentReference.gif");
        referenceObject.put("im1", "ComponentReference.gif");
        referenceObject.put("im2", "ComponentReference.gif");
        JSONArray referenceArray = new JSONArray();
        for (Binding binding : componentReference.getBinding()) {
        	referenceArray.add(this.complexProcessor.getMenuItem(binding, (String)referenceObject.get("id")));
        }

        referenceObject.put("item", referenceArray);
        return referenceObject;
	}

	@Override
	public String getPanel(EObject eObject) {
		ComponentReference reference = (ComponentReference)eObject;
		StringBuffer sb = new StringBuffer();
    	sb.append("<div class=\"component_frame_line\">");
    		sb.append("<form>");
    		sb.append("<table>");
    		sb.append("<tr>");
    			sb.append("<td>");
	    			sb.append("<div class=\"reference-image\"></div>");
	    			sb.append("Name : ");
	    		sb.append("</td>");
	    		sb.append("<td colspan=\"2\">");
	    			sb.append("<input type=\"text\" id=\"name\" name=\"name\" value=\""+reference.getName()+"\"/><br/>");
	    		sb.append("</td>");
	    	sb.append("</tr>");
	    			if(reference.getInterface()!=null){
	    				sb.append(this.complexProcessor.getPanel(reference.getInterface()));
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
	    			sb.append("<tr>");
	    			sb.append("<td>");
	    			sb.append("Target : ");
	    			sb.append("</td>");
	    			sb.append("<td colspan=\"2\">");
	    			sb.append("<input type=\"text\" id=\"target\" name=\"target\" size=\"40\" value=\""+reference.getTarget()+"\"/><br/>");
	    			sb.append("</td>");
	    			sb.append("</tr>");
	    			sb.append("</table>");
    		sb.append("</form>");
    	sb.append("</div>");
    	return sb.toString();
	}

	@Override
	public String getActionMenu(EObject eObject) {
		StringBuffer sb = new StringBuffer();
		sb.append("<a onClick=\"action('addBinding')\">Add Binding</a>");
		sb.append("<a onclick=\"action('deleteComponentReference')\">Delete</a>");
		return sb.toString();
	}

}