package org.easysoa.processor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.easysoa.api.ServiceManager;
import org.easysoa.api.Utils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.stp.sca.Binding;
import org.eclipse.stp.sca.Component;
import org.eclipse.stp.sca.ComponentReference;
import org.eclipse.stp.sca.ComponentService;
import org.eclipse.stp.sca.Composite;
import org.eclipse.stp.sca.Service;
import org.osoa.sca.annotations.Reference;

public class BindingProcessor implements BindingProcessorItf {

	@Reference
	private List<ComplexProcessorItf> processors;
	@Reference
	protected Utils utils;
	@Reference
	protected ServiceManager serviceManager;

	@Override
	public List<String> allAvailableBindingsLabel() {
		List<String> labels = new ArrayList<String>();
		for (ComplexProcessorItf processor : this.processors) {
			labels.add(processor.getLabel(null));
		}
		return labels;
	}

	@Override
	public String getBindingView(Composite composite, String modelId, String id) {
		System.out.println("getBindingView ID : "+id);
		for (ComplexProcessorItf processor : this.processors) {
			if (processor.getLabel(null).equals(id)) {
				Binding binding = (Binding)processor.getNewEObject(null);
				String[] ids = modelId.split(" ");
				binding.setName(ids[ids.length-1]);
				System.out.println(binding.eClass());
				this.modifyBinding(composite, modelId, binding);
				return processor.getPanel(null);
			}
		}
		return null;
	}

	private void modifyBinding(Composite composite, String id, EObject eObject) {
		System.out.println("modify binding");
		System.out.println("eObject name : "+((Binding)eObject).getName());
		String[] ids = id.split(" ");
		if (ids[0].equals("component")) {
			EList<Component> components = composite.getComponent();
			for (Component component : components) {
				if (component.getName().equals(ids[1])) {
					if (ids[2].equals("service")) {
						for (ComponentService componentService : component
								.getService()) {
							if (componentService.getName().equals(ids[3])) {
								if (ids.length == 6 && ids[4].equals("binding")) {
									Iterator<Binding> iterator = componentService.getBinding().iterator();
									while (iterator.hasNext()) {
										Binding binding = iterator.next();
										System.out.println("binding name : "+binding.getName());
										if (binding.getName().equals(((Binding) eObject).getName())) {
											System.out.println("change binding");
											iterator.remove();
											componentService.getBinding().add((Binding) eObject);
											break;
										}
										
									}
								}
							}
						}
					} else if (ids[2].equals("reference")) {
						for (ComponentReference componentReference : component
								.getReference()) {
							if (componentReference.getName().equals(ids[3])) {
								if (ids.length == 6 && ids[4].equals("binding")) {
									Iterator<Binding> iterator = componentReference.getBinding().iterator();
									while (iterator.hasNext()) {
										Binding binding = iterator.next();
										System.out.println("binding name : "+binding.getName());
										if (binding.getName().equals(((Binding) eObject).getName())) {
											System.out.println("change binding");
											iterator.remove();
											componentReference.getBinding().add((Binding) eObject);
											break;
										}
										
									}
								}
							}
						}
					}
				}
			}
		} else if (ids[0].equals("service")) {
			for (Service service : composite.getService()) {
				if (service.getName().equals(ids[1])) {
					// service name
					if (ids.length == 4 && ids[2].equals("binding")) {
						Iterator<Binding> iterator = service.getBinding().iterator();
						while (iterator.hasNext()) {
							Binding binding = iterator.next();
							System.out.println("binding name : "+binding.getName());
							if (binding.getName().equals(((Binding) eObject).getName())) {
								System.out.println("change binding");
								iterator.remove();
								service.getBinding().add((Binding) eObject);
								break;
							}
							
						}
					}
				}
			}
		} else if (ids[0].equals("reference")) {
			for (org.eclipse.stp.sca.Reference reference : composite
					.getReference()) {
				if (reference.getName().equals(ids[1])) {
					// reference name
					if (ids.length == 4 && ids[2].equals("binding")) {
						Iterator<Binding> iterator = reference.getBinding().iterator();
						while (iterator.hasNext()) {
							Binding binding = iterator.next();
							System.out.println("binding name : "+binding.getName());
							if (binding.getName().equals(((Binding) eObject).getName())) {
								System.out.println("change binding");
								iterator.remove();
								reference.getBinding().add((Binding) eObject);
								break;
							}
							
						}
					}
				}
			}
		}
		utils.saveComposite(composite, serviceManager.getCurrentApplication()
				.getCompositeLocation());
	}

}
