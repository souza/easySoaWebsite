package org.easysoa.processor;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.stp.sca.Component;
import org.eclipse.stp.sca.ComponentReference;
import org.eclipse.stp.sca.ComponentService;
import org.eclipse.stp.sca.Implementation;
import org.eclipse.stp.sca.JavaImplementation;
import org.eclipse.stp.sca.PropertyValue;
import org.eclipse.stp.sca.SCAImplementation;
import org.eclipse.stp.sca.ScaFactory;
import org.eclipse.stp.sca.ScaPackage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.osoa.sca.annotations.Reference;
import org.ow2.frascati.metamodel.web.VelocityImplementation;
import org.ow2.frascati.metamodel.web.WebFactory;
import org.ow2.frascati.metamodel.web.WebPackage;

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
//		if (component.getImplementation() != null) {
//			componentArray.add(this.complexProcessor.getMenuItem(component.getImplementation(),
//					(String) (componentJSONObject.get("id"))));
//		}

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
		if (component.getImplementation() != null) {
			sb.append(this.complexProcessor.getPanel(component.getImplementation()));
		}
		else {
			System.out.println("component implementation is null");
			sb.append("<table>");
			sb.append("<tr>");
			sb.append("<td>");
			sb.append("<div class=\"java-implementation-image\"></div>");
			sb.append("Implementation : ");
			sb.append("</td>");
			sb.append("<td>");
	    	sb.append("<input type=\"text\" id=\"implementation\" name=\"implementation\" size=\"40\" value=\"\"/>");
	    	sb.append("</td>");
	    	sb.append("<td>");
	    	sb.append("<select name=\"implementation-type\" id=\"implementation-type\" size=\"1\" onChange=\"changeImplementation()\">");
	    	for(String label : this.implementationProcessor.allAvailableImplementationsLabel()){
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
		}
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
		sb.append("<input type=\"submit\" value=\"Save\"/input>");
		return sb.toString();
	}

	@Override
	public EObject saveElement(EObject eObject, Map<String, Object> params) {
		Component component = (Component)eObject;
		component.setName((String)params.get("name"));
		org.eclipse.stp.sca.Implementation osoaImplementation = transform((Implementation)this.complexProcessor.saveElement(component.getImplementation(),params));
		EReference eReference = null;
		if (osoaImplementation instanceof org.eclipse.stp.sca.SCAImplementation) {
			eReference = org.eclipse.stp.sca.ScaPackage.Literals.DOCUMENT_ROOT__IMPLEMENTATION_COMPOSITE;
		}
		if (osoaImplementation instanceof org.eclipse.stp.sca.JavaImplementation) {
			eReference = org.eclipse.stp.sca.ScaPackage.Literals.DOCUMENT_ROOT__IMPLEMENTATION_JAVA;
		}
		if (osoaImplementation instanceof VelocityImplementation) {
			eReference = WebPackage.Literals.DOCUMENT_ROOT__IMPLEMENTATION_VELOCITY;
		}
		// TODO: manage other implementations, as BPEL, Spring,
		// etc.

		((FeatureMap.Internal) component
				.getImplementationGroup()).clear();
		((FeatureMap.Internal) component
				.getImplementationGroup())
				.add(org.eclipse.stp.sca.ScaPackage.Literals.COMPONENT__IMPLEMENTATION_GROUP,
						org.eclipse.emf.ecore.util.FeatureMapUtil
								.createEntry(eReference,
										osoaImplementation));
		return component;
	}

	@Override
	public EObject getNewEObject(EObject eObject) {
		return ScaFactory.eINSTANCE.createComponent();
	}
	
	private org.eclipse.stp.sca.Implementation transform(
			Implementation oasisImplementation) {
		// If <implementation.composite>.
		if (oasisImplementation instanceof SCAImplementation) {
			SCAImplementation oasisSCAImplementation = (SCAImplementation) oasisImplementation;
			// Create an OSOA SCAImplementation.
			org.eclipse.stp.sca.SCAImplementation osoaSCAImplementation = ScaFactory.eINSTANCE
					.createSCAImplementation();
			// Copy the composite name.
			osoaSCAImplementation.setName(oasisSCAImplementation.getName());

			// TODO: Copy or transform other properties.

			return osoaSCAImplementation;
		}

		// If <implementation.java>.
		if (oasisImplementation instanceof JavaImplementation) {
			JavaImplementation oasisJavaImplementation = (JavaImplementation) oasisImplementation;
			// Create an OSOA JavaImplementation.
			org.eclipse.stp.sca.JavaImplementation osoaJavaImplementation = ScaFactory.eINSTANCE
					.createJavaImplementation();
			// Copy the Java class name.
			osoaJavaImplementation
					.setClass(oasisJavaImplementation.getClass_());

			// TODO: Copy or transform other properties.

			return osoaJavaImplementation;
		}
		if (oasisImplementation instanceof VelocityImplementation) {
			VelocityImplementation oasisVelocityImplementation = (VelocityImplementation) oasisImplementation;
			// Create an OSOA JavaImplementation.
			VelocityImplementation osoaJavaImplementation = WebFactory.eINSTANCE
					.createVelocityImplementation();
			// Copy the Java class name.
			osoaJavaImplementation.setDefault(oasisVelocityImplementation
					.getDefault());
			osoaJavaImplementation.setLocation(oasisVelocityImplementation
					.getLocation());

			// TODO: Copy or transform other properties.

			return oasisVelocityImplementation;
		}

		// TODO: BPEL implementation.

		// TODO: Spring implementation.

		// TODO: other implementations.

		// Else do nothing.
		return null;
	}

}
