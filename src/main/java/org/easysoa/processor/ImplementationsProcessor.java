package org.easysoa.processor;

import java.util.ArrayList;
import java.util.List;

import org.easysoa.api.ServiceManager;
import org.easysoa.api.Utils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.stp.sca.Component;
import org.eclipse.stp.sca.Composite;
import org.eclipse.stp.sca.Implementation;
import org.eclipse.stp.sca.JavaImplementation;
import org.eclipse.stp.sca.SCAImplementation;
import org.eclipse.stp.sca.ScaFactory;
import org.eclipse.stp.sca.domainmodel.frascati.FrascatiFactory;
import org.eclipse.stp.sca.domainmodel.frascati.FrascatiPackage;
import org.eclipse.stp.sca.domainmodel.frascati.ScriptImplementation;
import org.osoa.sca.annotations.Reference;
import org.osoa.sca.annotations.Scope;
import org.ow2.frascati.metamodel.web.VelocityImplementation;
import org.ow2.frascati.metamodel.web.WebFactory;
import org.ow2.frascati.metamodel.web.WebPackage;

@Scope("COMPOSITE")
public class ImplementationsProcessor implements ImplementationsProcessorItf {

	@Reference
	private List<ComplexProcessorItf> processors;
	@Reference
	protected Utils utils;
	@Reference
	protected ServiceManager serviceManager;
	private String url;
	private String editorMode;

	public List<String> allAvailableImplementationsLabel() {
		List<String> labels = new ArrayList<String>();
		for (ComplexProcessorItf processor : this.processors) {
			labels.add(processor.getLabel(null));
		}
		return labels;
	}

	@Override
	public String getImplementationView(Composite composite, String modelId,
			String id) {
		System.out.println("getImplementationView id : "+id);
		for (ComplexProcessorItf processor : this.processors) {
			System.out.println("label : "+processor.getLabel(null));
			if (processor.getLabel(null).equals(id)) {
				this.modifyImplementation(composite, modelId,
						processor.getNewEObject(null));
				return processor.getPanel(null);
			}
		}
		return null;
	}

	private void modifyImplementation(Composite composite, String id,
			EObject eObject) {
		System.out.println("modify implementation");
		String[] ids = id.split(" ");
		// composite
		if (ids[0].equals("component")) {
			EList<Component> components = composite.getComponent();
			for (Component component : components) {
				if (component.getName().equals(ids[1])) {
					// component name
					if (ids.length == 2) {
						System.out.println("2 modify implementation");
						org.eclipse.stp.sca.Implementation osoaImplementation = transform((Implementation)eObject);
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
						if (osoaImplementation instanceof ScriptImplementation) {
							eReference = FrascatiPackage.Literals.DOCUMENT_ROOT__IMPLEMENTATION_SCRIPT;
						}
						// TODO: manage other implementations, as BPEL, Spring,
						// etc.

						((FeatureMap.Internal) component
								.getImplementationGroup()).clear();
						if(eReference != null){
						((FeatureMap.Internal) component
								.getImplementationGroup())
								.add(org.eclipse.stp.sca.ScaPackage.Literals.COMPONENT__IMPLEMENTATION_GROUP,
										org.eclipse.emf.ecore.util.FeatureMapUtil
												.createEntry(eReference,
														osoaImplementation));
						}
						System.out.println(component.getImplementation()
								.getClass());
					}
				}
			}
		}
		utils.saveComposite(composite, serviceManager.getCurrentApplication()
				.getCompositeLocation());
		//serviceManager.reloadComposite();
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

			return osoaJavaImplementation;
		}
		if (oasisImplementation instanceof ScriptImplementation) {
			ScriptImplementation oasisScriptImplementation = (ScriptImplementation) oasisImplementation;
			// Create an OSOA JavaImplementation.
			ScriptImplementation osoaScriptImplementation = FrascatiFactory.eINSTANCE.createScriptImplementation();
			// Copy the Java class name.
			osoaScriptImplementation.setScript(oasisScriptImplementation
					.getScript());

			// TODO: Copy or transform other properties.

			return oasisScriptImplementation;
		}

		// TODO: BPEL implementation.

		// TODO: Spring implementation.

		// TODO: other implementations.

		// Else do nothing.
		return null;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public String getEditorMode() {
		return editorMode;
	}
	
	@Override
	public void setEditorMode(String editorMode) {
		this.editorMode = editorMode;
	}
	
	@Override
	public EObject createImplementation(String label){
		for (ComplexProcessorItf processor : this.processors) {
			if (processor.getLabel(null).equals(label)) {
				return processor.getNewEObject(null);
			}
		}
		return null;
	}

}
