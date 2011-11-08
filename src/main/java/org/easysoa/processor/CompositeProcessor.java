package org.easysoa.processor;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.stp.sca.Component;
import org.eclipse.stp.sca.Composite;
import org.eclipse.stp.sca.ScaPackage;
import org.eclipse.stp.sca.Service;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.osoa.sca.annotations.Reference;

/**
 * 
 * @author Michel Dirix
 */
public class CompositeProcessor implements ComplexProcessorItf {

	@Reference
	protected ComplexProcessorItf complexProcessor;

	@Override
	public String getId() {
		return ScaPackage.eINSTANCE.getComposite().getEPackage().getNsURI() + "#"
				+ ScaPackage.eINSTANCE.getComposite().getName();
	}

	@Override
	public String getLabel(EObject eObject) {
		return "Composite";
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getMenuItem(EObject eObject, String parentId) {
		Composite composite = (Composite) eObject;
		JSONObject object = new JSONObject();
		object.put("id", 0);
		JSONArray compositeArray = new JSONArray();
		JSONObject compositeObject = new JSONObject();
		compositeObject.put("id", "composite+");
		compositeObject.put("text", composite.getName());
		compositeObject.put("im0", "Composite.gif");
		compositeObject.put("im1", "Composite.gif");
		compositeObject.put("im2", "Composite.gif");
		compositeArray.add(compositeObject);
		JSONArray array = new JSONArray();
		for (Component component : composite.getComponent()) {
			array.add(this.complexProcessor.getMenuItem(component, ""));
		}

		for (Service service : composite.getService()) {
			array.add(this.complexProcessor.getMenuItem(service, ""));
		}

		for (org.eclipse.stp.sca.Reference reference : composite.getReference()) {
			array.add(this.complexProcessor.getMenuItem(reference, ""));
		}
		compositeObject.put("item", array);
		object.put("item", compositeArray);
		return object;
	}

	@Override
	public String getPanel(EObject eObject) {
		Composite composite = (Composite) eObject;
		StringBuffer sb = new StringBuffer();
		sb.append("<div class=\"component_frame_line\">");
		sb.append("<table>");
		sb.append("<tr>");
		sb.append("<td>");
		sb.append("<div class=\"composite-image\"></div>");
		sb.append("Name : ");
		sb.append("</td>");
		sb.append("<td>");
		if(composite.getName()!=null)sb.append("<input type=\"text\" id=\"name\" name=\"name\" value=\""+ composite.getName()+ "\"/>");
		else sb.append("<input type=\"text\" id=\"name\" name=\"name\" value=\"\"/>");
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("</table>");
		sb.append("</div>");
		return sb.toString();
	}

	@Override
	public String getActionMenu(EObject eObject) {
		StringBuffer sb = new StringBuffer();
		sb.append("<a onclick=\"action('addComponent')\">Add component</a>");
		sb.append("<a onclick=\"action('addService')\">Add service</a>");
		sb.append("<a onclick=\"action('addReference')\">Add reference</a>");
		sb.append("<input type=\"submit\" value=\"Save\"/input>");
		return sb.toString();
	}

	@Override
	public EObject saveElement(EObject eObject, Map<String, Object> params) {
		Composite composite = (Composite)eObject;
		composite.setName((String)params.get("name"));
		return composite;
	}

	@Override
	public EObject getNewEObject(EObject eObject) {
		// TODO Auto-generated method stub
		return null;
	}

}
