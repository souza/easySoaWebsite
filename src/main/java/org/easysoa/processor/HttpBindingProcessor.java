package org.easysoa.processor;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.stp.sca.Binding;
import org.json.simple.JSONObject;
import org.osoa.sca.annotations.Reference;

/**
 *
 * @author Michel Dirix
 */
public class HttpBindingProcessor implements ComplexProcessorItf {

	@Reference
	protected BindingProcessorItf bindingProcessor; 
	
    @Override
    public String getId() {
        return "http://tuscany.apache.org/xmlns/sca/1.0#HTTPBinding";
    }

    @Override
    public String getLabel(EObject eObject) {
        return "Http";
    }

    @SuppressWarnings("unchecked")
	@Override
    public JSONObject getMenuItem(EObject eObject, String parentId) {
        Binding binding = (Binding) eObject;
        JSONObject bindingObject = new JSONObject();
        bindingObject.put("id", parentId+"+binding+"+binding.getUri());
        bindingObject.put("text", binding.getUri());
        bindingObject.put("im0", "HTTPBinding.gif");
        bindingObject.put("im1", "HTTPBinding.gif");
        bindingObject.put("im2", "HTTPBinding.gif");
        return bindingObject;
    }

    @Override
    public String getPanel(EObject eObject) {
        Binding binding = (Binding)eObject;
        StringBuffer sb = new StringBuffer();
    	sb.append("<div class=\"component_frame_line\">");
    		sb.append("<form>");
    		sb.append("<table>");
    		sb.append("<tr>");
    		sb.append("<td>");
    			sb.append("<div class=\"http-binding-image\"></div>");
    			sb.append("Uri : ");
    			sb.append("</td>");
    			sb.append("<td>");
    			sb.append("<input type=\"text\" id=\"uri\" name=\"uri\" value=\""+binding.getUri()+"\"/><br/>");
    			sb.append("</td>");
    			sb.append("<td>");
    			sb.append("<select name=\"binding-type\" id=\"binding-type\" size=\"1\">");
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
    		sb.append("</form>");
    	sb.append("</div>");
    	return sb.toString();
    }

	@Override
	public String getActionMenu(EObject eObject) {
		StringBuffer sb = new StringBuffer();
		sb.append("<a onclick=\"action('deleteBinding')\">Delete</a>");
		return sb.toString();
	}
	
}
