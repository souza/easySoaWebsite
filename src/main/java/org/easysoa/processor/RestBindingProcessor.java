package org.easysoa.processor;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.stp.sca.domainmodel.frascati.FrascatiFactory;
import org.eclipse.stp.sca.domainmodel.frascati.RestBinding;
import org.json.simple.JSONObject;
import org.osoa.sca.annotations.Reference;

/**
 *
 * @author Michel Dirix
 */
public class RestBindingProcessor implements ComplexProcessorItf {

	@Reference
	protected BindingProcessorItf bindingProcessor;
	
    @Override
    public String getId() {
        return "http://frascati.ow2.org/xmlns/sca/1.1#RestBinding";
    }

    @Override
    public String getLabel(EObject eObject) {
        return "Rest";
    }

    @SuppressWarnings("unchecked")
	@Override
    public JSONObject getMenuItem(EObject eObject, String parentId) {
    	RestBinding binding = (RestBinding) eObject;
        JSONObject bindingObject = new JSONObject();
        bindingObject.put("id", "+binding+"+binding.getName());
        bindingObject.put("text", binding.getName());
        bindingObject.put("im0", "RestBinding.gif");
        bindingObject.put("im1", "RestBinding.gif");
        bindingObject.put("im2", "RestBinding.gif");
        return bindingObject;
    }

    public String getPanel(EObject eObject) {
    	RestBinding binding = null;
    	if(eObject != null)	binding = (RestBinding)eObject;
    	else binding = (RestBinding)this.getNewEObject(null);
        StringBuffer sb = new StringBuffer();
    	sb.append("<div class=\"component_frame_line\">");
    		sb.append("<table>");
    		sb.append("<tr>");
    		sb.append("<td>");
    			sb.append("<div class=\"http-binding-image\"></div>");
    			sb.append("Name : ");
    			sb.append("</td>");
    			sb.append("<td colspan=\"2\">");
    			if(binding.getUri()!=null)sb.append("<input type=\"text\" id=\"name\" name=\"name\" value=\""+binding.getName()+"\"/><br/>");
    			else sb.append("<input type=\"text\" id=\"name\" name=\"name\" value=\"\"/><br/>");
    			sb.append("</td>");
    	    	sb.append("</tr>");
    		sb.append("<tr>");
    		sb.append("<td>");
    			sb.append("<div class=\"http-binding-image\"></div>");
    			sb.append("Uri : ");
    			sb.append("</td>");
    			sb.append("<td>");
    			if(binding.getUri()!=null)sb.append("<input type=\"text\" id=\"uri\" name=\"uri\" value=\""+binding.getUri()+"\"/><br/>");
    			else sb.append("<input type=\"text\" id=\"uri\" name=\"uri\" value=\"\"/><br/>");
    			sb.append("</td>");
    			sb.append("<td>");
    			sb.append("<select name=\"binding-type\" id=\"binding-type\" size=\"1\" onChange=\"changeBinding()\">");
    	    	for(String label : this.bindingProcessor.allAvailableBindingsLabel()){
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
    	    	sb.append("</table>");
    	sb.append("</div>");
    	return sb.toString();
    }

	@Override
	public String getActionMenu(EObject eObject) {
		StringBuffer sb = new StringBuffer();
		sb.append("<a onclick=\"action('deleteBinding')\">Delete</a>");
		sb.append("<input type=\"submit\" value=\"Save\"/input>");
		return sb.toString();
	}

	@Override
	public EObject saveElement(EObject eObject, Map<String, Object> params) {
		System.out.println("saveelement rest");
		RestBinding binding = (RestBinding)eObject;
		binding.setUri((String)params.get("uri"));
		binding.setName((String)params.get("name"));
		return binding;
	}

	@Override
	public EObject getNewEObject(EObject eObject) {
		System.out.println("rest element created");
		return FrascatiFactory.eINSTANCE.createRestBinding();
	}

}
