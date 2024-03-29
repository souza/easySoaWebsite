package org.easysoa.processor;

import java.util.Map;

import org.easysoa.api.ServiceManager;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.stp.sca.Binding;
import org.eclipse.stp.sca.Interface;
import org.eclipse.stp.sca.ScaFactory;
import org.eclipse.stp.sca.ScaPackage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.osoa.sca.annotations.Reference;

public class ReferenceProcessor implements ComplexProcessorItf {

	@Reference
	protected ComplexProcessorItf complexProcessor;
	@Reference
	protected InterfaceProcessorItf interfaceProcessor;
	@Reference
	protected ServiceManager serviceManager;
	
	@Override
	public String getId() {
		return ScaPackage.eINSTANCE.getReference().getEPackage().getNsURI() + "#" + ScaPackage.eINSTANCE.getReference().getName();
	}

	@Override
	public String getLabel(EObject eObject) {
		return "Reference";
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getMenuItem(EObject eObject, String parentId) {
		org.eclipse.stp.sca.Reference reference  = (org.eclipse.stp.sca.Reference)eObject;
		JSONObject referenceObject = new JSONObject();
        referenceObject.put("id", "reference+"+reference.getName());
        referenceObject.put("text", reference.getName());
        referenceObject.put("im0", "Reference.gif");
        referenceObject.put("im1", "Reference.gif");
        referenceObject.put("im2", "Reference.gif");
//        JSONArray interfaceArray = new JSONArray();
//        if (reference.getInterface() != null) {
//            interfaceArray.add(this.complexProcessor.getMenuItem(reference.getInterface(), (String)referenceObject.get("id")));
//        }
//        referenceObject.put("item", interfaceArray);

        JSONArray bindingArray = new JSONArray();
        for (Binding binding : reference.getBinding()) {
            bindingArray.add(this.complexProcessor.getMenuItem(binding,(String)referenceObject.get("id")));
        }
        referenceObject.put("item", bindingArray);
        return referenceObject;
	}

	@Override
	public String getPanel(EObject eObject) {
		org.eclipse.stp.sca.Reference reference  = (org.eclipse.stp.sca.Reference)eObject;
		StringBuffer sb = new StringBuffer();
    	sb.append("<div class=\"component_frame_line\">");
    		sb.append("<table>");
    		sb.append("<tr>");
    		sb.append("<td>");
	    			sb.append("<div class=\"reference-image\"></div>");
	    			sb.append("Name : ");
	    			sb.append("</td>");
	    			sb.append("<td colspan=\"2\">");
	    			if(reference.getName()!=null)sb.append("<input type=\"text\" id=\"name\" name=\"name\" value=\""+reference.getName()+"\"/><br/>");
	    			else sb.append("<input type=\"text\" id=\"name\" name=\"name\" value=\"\"/><br/>");
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
	    			sb.append("Target : ");
	    			sb.append("</td>");
	    			sb.append("<td colspan=\"2\">");
	    				sb.append("<select name=\"target\" id=\"target\" size=\"1\">");
    		    	for(String target : this.serviceManager.getAllTarget()){
    		    		if(reference.getTarget()!=null && reference.getTarget().equals(target)){
    		    			sb.append("<option selected=\"selected\">"+target+"</option>");
    		    		}
    		    		else{
    		    			sb.append("<option>"+target+"</option>");
    		    		}
    		    	}
    		    	sb.append("</select>");
	    			sb.append("</td>");
	    			sb.append("</tr>");
	    			sb.append("<tr>");
	    			sb.append("<td>");
	    			sb.append("<div class=\"promote-image\"></div>");
	    			sb.append("Promote : ");
	    			sb.append("</td>");
	    			sb.append("<td colspan=\"2\">");
	    			if(reference.getPromote()!=null)sb.append("<input type=\"text\" id=\"promote\" name=\"promote\" size=\"40\" value=\""+reference.getPromote()+"\"/><br/>");
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
		sb.append("<a onclick=\"action('addBinding')\">Add Binding</a>");
		sb.append("<a onclick=\"action('deleteReference')\">Delete</a>");
		sb.append("<input type=\"submit\" value=\"Save\"/input>");
		return sb.toString();
	}

	@Override
	public EObject saveElement(EObject eObject, Map<String, Object> params) {
		org.eclipse.stp.sca.Reference reference = (org.eclipse.stp.sca.Reference)eObject;
		reference.setName((String)params.get("name"));
		System.out.println("promote : #"+(String)params.get("promote")+"#");
		if(params.get("promote")!=null && !((String)params.get("promote")).equals(""))
		reference.setPromote((String)params.get("promote"));
		if(params.get("target")!=null && !((String)params.get("target")).equals(""))
		reference.setTarget((String)params.get("target"));
		if(reference.getInterface()!=null)
		reference.setInterface((Interface)this.complexProcessor.saveElement(reference.getInterface(), params));
		return reference;
	}

	@Override
	public EObject getNewEObject(EObject eObject) {
		return ScaFactory.eINSTANCE.createReference();
	}
}
