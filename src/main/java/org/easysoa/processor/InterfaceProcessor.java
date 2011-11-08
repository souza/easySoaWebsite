package org.easysoa.processor;

import java.util.ArrayList;
import java.util.List;

import org.easysoa.api.ServiceManager;
import org.easysoa.api.Utils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.stp.sca.Component;
import org.eclipse.stp.sca.ComponentReference;
import org.eclipse.stp.sca.ComponentService;
import org.eclipse.stp.sca.Composite;
import org.eclipse.stp.sca.Interface;
import org.eclipse.stp.sca.Service;
import org.osoa.sca.annotations.Reference;

public class InterfaceProcessor implements InterfaceProcessorItf {

	@Reference
	private List<ComplexProcessorItf> processors;
	@Reference
	protected Utils utils;
	@Reference
	protected ServiceManager serviceManager;

	@Override
	public List<String> allAvailableInterfacesLabel() {
		List<String> labels = new ArrayList<String>();
		for (ComplexProcessorItf processor : this.processors) {
			labels.add(processor.getLabel(null));
		}
		return labels;
	}

	@Override
	public String getInterfaceView(Composite composite, String modelId,
			String id) {
		for (ComplexProcessorItf processor : this.processors) {
			if (processor.getLabel(null).equals(id)) {
				this.modifyInterface(composite, modelId,
						processor.getNewEObject(null));
				return processor.getPanel(null);
			}
		}
		return null;
	}

	private void modifyInterface(Composite composite, String id, EObject eObject) {
		String[] ids = id.split(" ");
		if (ids[0].equals("component")) {
			EList<Component> components = composite.getComponent();
			for (Component component : components) {
				if (component.getName().equals(ids[1])) {
					if (ids[2].equals("service")) {
						for (ComponentService componentService : component
								.getService()) {
							if (componentService.getName().equals(ids[3])) {
								componentService
										.setInterface((Interface) eObject);
							}
						}
					}
					else if (ids[2].equals("reference")) {
						for (ComponentReference componentReference : component
								.getReference()) {
							if (componentReference.getName().equals(ids[3])) {
								componentReference
										.setInterface((Interface) eObject);
							}
						}
					}
				}
			}
		} else if (ids[0].equals("service")) {
			for (Service service : composite.getService()) {
				if (service.getName().equals(ids[1])) {
					// service name
					if (ids.length == 2) {
						service.setInterface((Interface) eObject);
					}
				}
			}
		} else if (ids[0].equals("reference")) {
			for (org.eclipse.stp.sca.Reference reference : composite
					.getReference()) {
				if (reference.getName().equals(ids[1])) {
					// reference name
					if (ids.length == 2) {
						reference.setInterface((Interface)eObject);
					}
				}
			}
		}
		utils.saveComposite(composite, serviceManager.getCurrentApplication()
				.getCompositeLocation());
	}
}
