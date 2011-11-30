package org.easysoa.processor;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.stp.sca.Component;
import org.eclipse.stp.sca.ComponentReference;
import org.eclipse.stp.sca.ComponentService;
import org.eclipse.stp.sca.PropertyValue;
import org.eclipse.stp.sca.ScaFactory;
import org.eclipse.stp.sca.ScaPackage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.osoa.sca.annotations.Reference;

public class ComponentProcessor implements ComplexProcessorItf {

	@Reference
	protected ComplexProcessorItf complexProcessor;
	@Reference
	protected ImplementationsProcessorItf implementationProcessor;
	

	@Override
	public String getId() {
		return ScaPackage.eINSTANCE.getComponent().getEPackage().getNsURI() + "#"
				+ ScaPackage.eINSTANCE.getComponent().getName();
	}

	@Override
	public String getLabel(EObject eObject) {
		return "Component";
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getMenuItem(EObject eObject, String parentId) {
		Component component = (Component) eObject;
		JSONObject componentJSONObject = new JSONObject();
		componentJSONObject.put("id", "component+" + component.getName());
		componentJSONObject.put("text", component.getName());
		componentJSONObject.put("im0", "Component.gif");
		componentJSONObject.put("im1", "Component.gif");
		componentJSONObject.put("im2", "Component.gif");
		JSONArray componentArray = new JSONArray();
		if(component.getImplementation() != null)
		componentArray.add(this.complexProcessor.getMenuItem(component.getImplementation(), (String)componentJSONObject.get("id")));
		for (ComponentService service : component.getService()) {
			componentArray.add(this.complexProcessor.getMenuItem(service,
					(String) componentJSONObject.get("id")));
		}

		for (PropertyValue property : component.getProperty()) {
			componentArray.add(this.complexProcessor.getMenuItem(property,
					(String) componentJSONObject.get("id")));
		}

		for (ComponentReference componentReference : component.getReference()) {
			componentArray.add(this.complexProcessor.getMenuItem(componentReference,
					(String) componentJSONObject.get("id")));
		}
		componentJSONObject.put("item", componentArray);
		return componentJSONObject;
	}

	@Override
	public String getPanel(EObject eObject) {
		Component component = (Component) eObject;
		StringBuffer sb = new StringBuffer();
		sb.append("<div class=\"component_frame_line\">");
		sb.append("<table>");
		sb.append("<tr>");
		sb.append("<td>");
		sb.append("<div class=\"component-image\"></div>");
		sb.append("Name : ");
		sb.append("</td>");
		sb.append("<td colspan=\"2\">");
		if(component.getName()!=null)sb.append("<input type=\"text\" id=\"name\" name=\"name\" value=\""+ component.getName() + "\"/><br/>");
		else sb.append("<input type=\"text\" id=\"name\" name=\"name\" value=\"\"/><br/>");
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("</table>");
		sb.append("</div>");
		return sb.toString();
	}

	@Override
	public String getActionMenu(EObject eObject) {
		StringBuffer sb = new StringBuffer();
		sb.append("<a onclick=\"action('addComponentService')\">Add Service</a>");
		sb.append("<a onclick=\"action('addComponentReference')\">Add Reference</a>");
		sb.append("<a onclick=\"action('addComponentProperty')\">Add Property</a>");
		sb.append("<a onclick=\"action('deleteComponent')\">Delete</a>");
		sb.append("<a onclick=\"addImplementation()\">Add implementation</a>");
		sb.append("<input type=\"submit\" value=\"Save\"/input>");
		return sb.toString();
	}

	@Override
	public EObject saveElement(EObject eObject, Map<String, Object> params) {
		Component component = (Component)eObject;
		component.setName((String)params.get("name"));
		return component;
	}

	@Override
	public EObject getNewEObject(EObject eObject) {
		return ScaFactory.eINSTANCE.createComponent();
	}
	
	

}
