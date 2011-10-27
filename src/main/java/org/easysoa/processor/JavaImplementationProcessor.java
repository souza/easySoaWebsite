package org.easysoa.processor;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.stp.sca.JavaImplementation;
import org.eclipse.stp.sca.ScaPackage;
import org.json.simple.JSONObject;
import org.osoa.sca.annotations.Reference;

/**
 *
 * @author Michel Dirix
 */
public class JavaImplementationProcessor implements ComplexProcessorItf {

	@Reference
	protected ImplementationsProcessorItf implementationsProcessor; 
	
	@Override
    public String getId() {
        return ScaPackage.eINSTANCE.getJavaImplementation().getEPackage().getNsURI() + "#" + ScaPackage.eINSTANCE.getJavaImplementation().getName();
    }
	
	@Override
    public String getLabel(EObject eObject) {
        return "Java";
    }
	
    @SuppressWarnings("unchecked")
	@Override
    public JSONObject getMenuItem(EObject eObject, String parentId) {
        JavaImplementation javaImplementation = (JavaImplementation)eObject;
        JSONObject implemObject = new JSONObject();
        implemObject.put("id", parentId+"+implementation");
        implemObject.put("text", javaImplementation.getClass_());
        implemObject.put("im0", "JavaImplementation.gif");
        implemObject.put("im1", "JavaImplementation.gif");
        implemObject.put("im2", "JavaImplementation.gif");
        return implemObject;
    }

    @Override
    public String getPanel(EObject eObject) {
    	JavaImplementation javaImplementation = (JavaImplementation)eObject;
    	StringBuffer sb = new StringBuffer();
    	sb.append("<tr>");
    	sb.append("<td>");
    	sb.append("<div class=\"java-implementation-image\"></div>");
    	sb.append("Implementation : ");
    	sb.append("</td>");
    	sb.append("<td>");
    	if(javaImplementation.getClass_()!=null)sb.append("<input type=\"text\" id=\"implementation\" name=\"implementation\" size=\"40\" value=\""+javaImplementation.getClass_()+"\"/>");
    	else sb.append("<input type=\"text\" id=\"implementation\" name=\"implementation\" size=\"40\" value=\"\"/>");
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
