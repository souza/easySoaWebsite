package org.easysoa.processor;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.stp.sca.JavaInterface;
import org.eclipse.stp.sca.ScaFactory;
import org.eclipse.stp.sca.ScaPackage;
import org.json.simple.JSONObject;
import org.osoa.sca.annotations.Reference;

/**
 *
 * @author Michel Dirix
 */
public class JavaInterfaceProcessor implements ComplexProcessorItf {

	@Reference
	protected InterfaceProcessorItf interfaceProcessor; 
	
    @Override
    public String getId() {
        return ScaPackage.eINSTANCE.getJavaInterface().getEPackage().getNsURI() + "#" + ScaPackage.eINSTANCE.getJavaInterface().getName();
    }

    @Override
    public String getLabel(EObject eObject) {
        return "Java";
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public JSONObject getMenuItem(EObject eObject, String parentId) {
        JavaInterface javaInterface = (JavaInterface) eObject;
        JSONObject interfObject = new JSONObject();
        interfObject.put("id", "+interface+"+javaInterface.getInterface());
        interfObject.put("text", javaInterface.getInterface());
        interfObject.put("im0", "JavaInterface.gif");
        interfObject.put("im1", "JavaInterface.gif");
        interfObject.put("im2", "JavaInterface.gif");
        return interfObject;
    }

    @Override
    public String getPanel(EObject eObject) {
    	JavaInterface javaInterface = null;
    	if(eObject != null) javaInterface = (JavaInterface)eObject;
    	else javaInterface = (JavaInterface)this.getNewEObject(null);
    	StringBuffer sb = new StringBuffer();
    	sb.append("<table id=\"interface-panel\">");
    	sb.append("<tr>");
    	sb.append("<td>");
    	sb.append("<div class=\"java-interface-image\"></div>");
    	sb.append("Interface : ");
    	sb.append("</td>");
    	sb.append("<td>");
    	if(javaInterface.getInterface()!=null)sb.append("<input type=\"text\" id=\"interface\" name=\"interface\" size=\"40\" value=\""+javaInterface.getInterface()+"\"/>");
    	else sb.append("<input type=\"text\" id=\"interface\" name=\"interface\" size=\"40\" value=\"\"/>");
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
    	sb.append("</table>");
    	return sb.toString();
    }

	@Override
	public String getActionMenu(EObject eObject) {
		StringBuffer sb = new StringBuffer();
		sb.append("<a onclick=\"action('deleteInterface')\">Delete</a>");
		return sb.toString();
	}

	@Override
	public EObject saveElement(EObject eObject, Map<String, Object> params) {
		JavaInterface javaInterface = (JavaInterface)eObject;
		if(params.get("interface")!=null)
		javaInterface.setInterface((String)params.get("interface"));
		return javaInterface;
	}

	@Override
	public EObject getNewEObject(EObject eObject) {
		return ScaFactory.eINSTANCE.createJavaInterface();
	}
	

}
