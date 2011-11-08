package org.easysoa.processor;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.stp.sca.SCABinding;
import org.eclipse.stp.sca.ScaFactory;
import org.eclipse.stp.sca.ScaPackage;
import org.json.simple.JSONObject;
import org.osoa.sca.annotations.Reference;

/**
 *
 * @author Michel Dirix
 */
public class ScaBindingProcessor implements ComplexProcessorItf{

	@Reference
	protected BindingProcessorItf bindingProcessor;
	
    @Override
    public String getId() {
        return ScaPackage.eINSTANCE.getSCABinding().getEPackage().getNsURI() + "#" + ScaPackage.eINSTANCE.getSCABinding().getName();
    }

    @Override
    public String getLabel(EObject eObject) {
        return "Sca";
    }

    @SuppressWarnings("unchecked")
	@Override
    public JSONObject getMenuItem(EObject eObject, String parentId) {
        SCABinding binding = (SCABinding) eObject;
        JSONObject bindingObject = new JSONObject();
        bindingObject.put("id", "+binding+"+binding.getName());
        bindingObject.put("text", binding.getName());
        bindingObject.put("im0", "ScaBinding.gif");
        bindingObject.put("im1", "ScaBinding.gif");
        bindingObject.put("im2", "ScaBinding.gif");
        return bindingObject;
    }

    public String getPanel(EObject eObject) {
    	SCABinding binding = null;
    	if(eObject != null)	binding = (SCABinding)eObject;
    	else binding = (SCABinding)this.getNewEObject(null);
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
		SCABinding binding = (SCABinding)eObject;
		binding.setUri((String)params.get("uri"));
		binding.setName((String)params.get("name"));
		return binding;
	}

	@Override
	public EObject getNewEObject(EObject eObject) {
		return ScaFactory.eINSTANCE.createSCABinding();
	}
}
