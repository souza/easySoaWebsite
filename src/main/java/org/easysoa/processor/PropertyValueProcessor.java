package org.easysoa.processor;

import java.util.Map;

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.stp.sca.PropertyValue;
import org.eclipse.stp.sca.ScaFactory;
import org.eclipse.stp.sca.ScaPackage;
import org.json.simple.JSONObject;

/**
 *
 * @author Michel Dirix
 */
public class PropertyValueProcessor implements ComplexProcessorItf{

    @Override
    public String getId() {
    	return ScaPackage.eINSTANCE.getPropertyValue().getEPackage().getNsURI() + "#" + ScaPackage.eINSTANCE.getPropertyValue().getName();
    }

    @Override
    public String getLabel(EObject eObject) {
        return "Property";
    }

    @SuppressWarnings("unchecked")
	@Override
    public JSONObject getMenuItem(EObject eObject, String parentId) {
    	PropertyValue property = (PropertyValue)eObject;
    	 JSONObject propertyObject = new JSONObject();
         propertyObject.put("id", "+property+"+property.getName());
         propertyObject.put("text", property.getName());
         propertyObject.put("im0", "Property.gif");
         propertyObject.put("im1", "Property.gif");
         propertyObject.put("im2", "Property.gif");
         return propertyObject;
    }

    @Override
    public String getPanel(EObject eObject) {
        PropertyValue property = (PropertyValue)eObject;
        StringBuffer sb = new StringBuffer();
    	sb.append("<div class=\"component_frame_line\">");
    		sb.append("<table>");
    		sb.append("<tr>");
    		sb.append("<td>");
    			sb.append("<div class=\"property-image\"></div>");
    			sb.append("Name : ");
    			sb.append("</td>");
    			sb.append("<td>");
    			if(property.getName()!=null)sb.append("<input type=\"text\" id=\"name\" name=\"name\" value=\""+property.getName()+"\"/><br/>");
    			else sb.append("<input type=\"text\" id=\"name\" name=\"name\" value=\"\"/><br/>");
    			sb.append("</tr>");
    			sb.append("<tr>");
    			sb.append("<td>");
    			sb.append("Type : ");
    			sb.append("</td>");
    			sb.append("<td>");
    			if(property.getType()!=null)sb.append("<input type=\"text\" id=\"type\" name=\"type\" value=\""+property.getType()+"\"/><br/>");
    			else sb.append("<input type=\"text\" id=\"type\" name=\"type\" value=\"\"/><br/>");
    			sb.append("</td>");
    			sb.append("</tr>");
    			sb.append("<tr>");
    			sb.append("<td>");
    			sb.append("Value : ");
    			sb.append("</td>");
    			sb.append("<td>");
    			if(property.getValue()!=null)sb.append("<input type=\"text\" id=\"value\" name=\"value\" value=\""+property.getValue()+"\"/><br/>");
    			else sb.append("<input type=\"text\" id=\"value\" name=\"value\" value=\"\"/><br/>");
    			sb.append("</td>");
    			sb.append("</tr>");
    			sb.append("</table>");
    	sb.append("</div>");
    	return sb.toString();
    }

	@Override
	public String getActionMenu(EObject eObject) {
		StringBuffer sb = new StringBuffer();
		sb.append("<a onclick=\"action('deleteComponentProperty')\">Delete</a>");
		sb.append("<input type=\"submit\" value=\"Save\"/input>");
		return sb.toString();
	}

	@Override
	public EObject saveElement(EObject eObject, Map<String, Object> params) {
		PropertyValue property = (PropertyValue)eObject;
		property.setName((String)params.get("name"));
		if(params.get("type")!=null && !((String)params.get("type")).equals(""))
		property.setType(new QName((String)params.get("type")));
		if(params.get("value")!=null && !((String)params.get("value")).equals(""))
		property.setValue((String)params.get("value"));
		return property;
	}

	@Override
	public EObject getNewEObject(EObject eObject) {
		return ScaFactory.eINSTANCE.createPropertyValue();
	}
	
}
