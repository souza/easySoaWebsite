package org.easysoa.processor;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.stp.sca.ScaFactory;
import org.eclipse.stp.sca.ScaPackage;
import org.eclipse.stp.sca.WSDLPortType;
import org.json.simple.JSONObject;
import org.osoa.sca.annotations.Reference;

/**
 *
 * @author Michel Dirix
 */
public class WsdlInterfaceProcessor implements ComplexProcessorItf {

	@Reference
	protected InterfaceProcessorItf interfaceProcessor; 
	
    @Override
    public String getId() {
        return ScaPackage.eINSTANCE.getWSDLPortType().getEPackage().getNsURI() + "#" + ScaPackage.eINSTANCE.getWSDLPortType().getName();
    }

    @Override
    public String getLabel(EObject eObject) {
        return "WSDL";
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public JSONObject getMenuItem(EObject eObject, String parentId) {
        WSDLPortType wsdlInterface = (WSDLPortType) eObject;
        JSONObject interfObject = new JSONObject();
        interfObject.put("id", "+interface+"+wsdlInterface.getInterface());
        interfObject.put("text", wsdlInterface.getInterface());
        interfObject.put("im0", "JavaInterface.gif");
        interfObject.put("im1", "JavaInterface.gif");
        interfObject.put("im2", "JavaInterface.gif");
        return interfObject;
    }

    @Override
    public String getPanel(EObject eObject) {
    	WSDLPortType wsdlInterface = null;
    	if(eObject != null) wsdlInterface = (WSDLPortType)eObject;
    	else wsdlInterface = (WSDLPortType)this.getNewEObject(null);
    	StringBuffer sb = new StringBuffer();
    	sb.append("<table id=\"interface-panel\">");
    	sb.append("<tr>");
    	sb.append("<td>");
    	sb.append("<div class=\"java-interface-image\"></div>");
    	sb.append("Interface : ");
    	sb.append("</td>");
    	sb.append("<td>");
    	if(wsdlInterface.getInterface()!=null)sb.append("<input type=\"text\" id=\"interface\" name=\"interface\" size=\"40\" value=\""+wsdlInterface.getInterface()+"\"/>");
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
		WSDLPortType wsdlInterface = (WSDLPortType)eObject;
		wsdlInterface.setInterface((String)params.get("interface"));
		return wsdlInterface;
	}

	@Override
	public EObject getNewEObject(EObject eObject) {
		return ScaFactory.eINSTANCE.createWSDLPortType();
	}
	

}
