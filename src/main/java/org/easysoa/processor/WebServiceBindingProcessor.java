package org.easysoa.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.stp.sca.ScaFactory;
import org.eclipse.stp.sca.ScaPackage;
import org.eclipse.stp.sca.WebServiceBinding;
import org.json.simple.JSONObject;
import org.osoa.sca.annotations.Reference;

/**
 *
 * @author Michel Dirix
 */
public class WebServiceBindingProcessor implements ComplexProcessorItf{

	@Reference
	protected BindingProcessorItf bindingProcessor;
	
    @Override
    public String getId() {
        return ScaPackage.eINSTANCE.getWebServiceBinding().getEPackage().getNsURI() + "#" + ScaPackage.eINSTANCE.getWebServiceBinding().getName();
    }

    @Override
    public String getLabel(EObject eObject) {
        return "Web Service";
    }

    @SuppressWarnings("unchecked")
	@Override
    public JSONObject getMenuItem(EObject eObject, String parentId) {
    	WebServiceBinding binding = (WebServiceBinding) eObject;
    	binding.setUri(binding.getWsdlElement());
        JSONObject bindingObject = new JSONObject();
        bindingObject.put("id", "+binding+"+binding.getName());
        bindingObject.put("text", binding.getName());
        bindingObject.put("im0", "WebServiceBinding.gif");
        bindingObject.put("im1", "WebServiceBinding.gif");
        bindingObject.put("im2", "WebServiceBinding.gif");
        return bindingObject;
    }

    @Override
    public String getPanel(EObject eObject) {
    	WebServiceBinding binding = null;
    	if(eObject != null) binding = (WebServiceBinding)eObject;
    	else binding = (WebServiceBinding)this.getNewEObject(null);
        StringBuffer sb = new StringBuffer();
    	sb.append("<div class=\"component_frame_line\">");
    		sb.append("<table>");
    		sb.append("<tr>");
    		sb.append("<td>");
    			sb.append("<div class=\"web-service-binding-image\"></div>");
    			sb.append("Name : ");
    			sb.append("</td>");
    			sb.append("<td>");
    			if(binding.getName()!=null)sb.append("<input type=\"text\" id=\"name\" name=\"name\" value=\""+binding.getName()+"\"/><br/>");
    			else sb.append("<input type=\"text\" id=\"name\" name=\"name\" value=\"\"/><br/>");
    			sb.append("</td>");
    	    	sb.append("</tr>");
    		sb.append("<tr>");
    			sb.append("<td colspan=\"2\">");
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
    	    	sb.append("<tr>");
        		sb.append("<td>");
        			sb.append("<div class=\"web-service-binding-image\"></div>");
        			sb.append("Wsdl Element : ");
        			sb.append("</td>");
        			sb.append("<td>");
        			if(binding.getWsdlElement()!=null)sb.append("<input type=\"text\" id=\"wsdl-element\" name=\"wsdl-element\" value=\""+binding.getWsdlElement()+"\"/><br/>");
        			else sb.append("<input type=\"text\" id=\"wsdl-element\" name=\"wsdl-element\" value=\"\"/><br/>");
        			sb.append("</td>");
        	    	sb.append("</tr>");
        	    	sb.append("<tr>");
            		sb.append("<td>");
            			sb.append("<div class=\"web-service-binding-image\"></div>");
            			sb.append("Wsdl Location : ");
            			sb.append("</td>");
            			sb.append("<td>");
            			if(binding.getWsdlLocation()!=null && binding.getWsdlLocation().size()>0)sb.append("<input type=\"text\" id=\"wsdl-location\" name=\"wsdl-location\" value=\""+binding.getWsdlLocation().get(0)+"\"/><br/>");
            			else sb.append("<input type=\"text\" id=\"wsdl-location\" name=\"wsdl-location\" value=\"\"/><br/>");
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
		WebServiceBinding binding = (WebServiceBinding)eObject;
		binding.setName((String)params.get("name"));
		binding.setUri((String)params.get("wsdl-element"));
		binding.setWsdlElement((String)params.get("wsdl-element"));
		List<String> locations = new ArrayList<String>();
		locations.add((String)params.get("wsdlLocation"));
		binding.setWsdlLocation(locations);
		return binding;
	}

	@Override
	public EObject getNewEObject(EObject eObject) {
		return ScaFactory.eINSTANCE.createWebServiceBinding();
	}

}
