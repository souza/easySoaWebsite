/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easysoa.impl;

import org.easysoa.api.RESTCall;
import org.easysoa.api.ServiceManager;
import org.easysoa.api.Utils;
import org.easysoa.processor.ComplexProcessorItf;
import org.eclipse.emf.common.util.EList;
import org.eclipse.stp.sca.Binding;
import org.eclipse.stp.sca.Component;
import org.eclipse.stp.sca.ComponentReference;
import org.eclipse.stp.sca.ComponentService;
import org.eclipse.stp.sca.Composite;
import org.eclipse.stp.sca.PropertyValue;
import org.eclipse.stp.sca.SCABinding;
import org.eclipse.stp.sca.ScaFactory;
import org.eclipse.stp.sca.Service;
import org.osoa.sca.annotations.Reference;
import org.osoa.sca.annotations.Scope;

/**
 * 
 * @author Michel Dirix
 */
@Scope("COMPOSITE")
public class RESTCallImpl implements RESTCall {

	@Reference
	private ServiceManager serviceManager;
	@Reference
	private Utils utils;
	@Reference
	private ComplexProcessorItf processor;

	@Override
	public String getCompositeTree() {
		Composite composite = serviceManager.getComposite();
		return this.processor.getMenuItem(composite, "").toJSONString();
	}

	@Override
	public synchronized String getComponentContent(String id) {
		System.out.println("ID : "+id);
		String[] ids = id.split(" ");
		Composite composite = serviceManager.getComposite();
		// composite
		if (ids.length == 1) {
			return this.processor.getPanel(composite);
		}
		else if (ids[0].equals("component")) {
			EList<Component> components = composite.getComponent();
			for (Component component : components) {
				if (component.getName().equals(ids[1])) {
					// component name
					if (ids.length == 2) {
						return this.processor.getPanel(component);
					}
					// component name implementation
					else if (ids.length == 3) {
						return this.processor.getPanel(component.getImplementation());
					}
					else {
						// component name property name
						if (ids[2].equals("property")) {
							for (PropertyValue property : component.getProperty()) {
								if (property.getName().equals(ids[3])) {
									return this.processor.getPanel(property);
								}
							}
						}
						else if (ids[2].equals("reference")) {
							for (ComponentReference componentReference : component.getReference()) {
								if (componentReference.getName().equals(ids[3])) {
									// component name reference name
									if (ids.length == 4) {
										return this.processor.getPanel(componentReference);
									}
									// component name reference name binding uri
									// name
									else if (ids.length == 6 && ids[4].equals("binding")) {
										for (Binding binding : componentReference.getBinding()) {
											if (ids[5].equals(binding.getUri())) {
												return this.processor.getPanel(binding);
											}
										}
									}
								}
							}
						}
						else if (ids[2].equals("service")) {
							for (ComponentService componentService : component.getService()) {
								if (componentService.getName().equals(ids[3])) {
									// component name service name
									if (ids.length == 4) {
										return this.processor.getPanel(componentService);
									}
									// component name service name interface
									else if (ids.length == 6 && ids[4].equals("interface")) {
										return this.processor.getPanel(componentService
												.getInterface());
									}
									// component name service name binding uri
									else if (ids.length == 6 && ids[4].equals("binding")) {
										for (Binding binding : componentService.getBinding()) {
											if (ids[5].equals(binding.getUri())) {
												return this.processor.getPanel(binding);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		else if (ids[0].equals("service")) {
			for (Service service : composite.getService()) {
				if (service.getName().equals(ids[1])) {
					// service name
					if (ids.length == 2) {
						return this.processor.getPanel(service);
					}
					// service name binding uri
					else if (ids.length == 4 && ids[2].equals("binding")) {
						for (Binding binding : service.getBinding()) {
							if (ids[3].equals(binding.getUri())) {
								return this.processor.getPanel(binding);
							}
						}
					}
				}
			}
		}
		else if (ids[0].equals("reference")) {
			for (org.eclipse.stp.sca.Reference reference : composite.getReference()) {
				if (reference.getName().equals(ids[1])) {
					// reference name
					if (ids.length == 2) {
						return this.processor.getPanel(reference);
					}
					// reference name binding uri
					else if (ids.length == 4 && ids[2].equals("binding")) {
						for (Binding binding : reference.getBinding()) {
							if (ids[3].equals(binding.getUri())) {
								return this.processor.getPanel(binding);
							}
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public synchronized String getComponentMenu(String id) {
		String[] ids = id.split(" ");
		Composite composite = serviceManager.getComposite();
		// composite
		if (ids.length == 1) {
			return this.processor.getActionMenu(composite);
		}
		else if (ids[0].equals("component")) {
			EList<Component> components = composite.getComponent();
			for (Component component : components) {
				if (component.getName().equals(ids[1])) {
					// component name
					if (ids.length == 2) {
						return this.processor.getActionMenu(component);
					}
					// component name implementation
					else if (ids.length == 3) {
						return this.processor.getActionMenu(component.getImplementation());
					}
					else {
						// component name property name
						if (ids[2].equals("property")) {
							for (PropertyValue property : component.getProperty()) {
								if (property.getName().equals(ids[3])) {
									return this.processor.getActionMenu(property);
								}
							}
						}
						else if (ids[2].equals("reference")) {
							for (ComponentReference componentReference : component.getReference()) {
								if (componentReference.getName().equals(ids[3])) {
									// component name reference name
									if (ids.length == 4) {
										return this.processor.getActionMenu(componentReference);
									}
									// component name reference name binding uri
									// name
									else if (ids.length == 6 && ids[4].equals("binding")) {
										for (Binding binding : componentReference.getBinding()) {
											if (ids[5].equals(binding.getUri())) {
												return this.processor.getActionMenu(binding);
											}
										}
									}
								}
							}
						}
						else if (ids[2].equals("service")) {
							for (ComponentService componentService : component.getService()) {
								if (componentService.getName().equals(ids[3])) {
									// component name service name
									if (ids.length == 4) {
										return this.processor.getActionMenu(componentService);
									}
									// component name service name interface
									else if (ids.length == 6 && ids[4].equals("interface")) {
										return this.processor.getActionMenu(componentService
												.getInterface());
									}
									// component name service name binding uri
									else if (ids.length == 6 && ids[4].equals("binding")) {
										for (Binding binding : componentService.getBinding()) {
											if (ids[5].equals(binding.getUri())) {
												return this.processor.getActionMenu(binding);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		else if (ids[0].equals("service")) {
			for (Service service : composite.getService()) {
				if (service.getName().equals(ids[1])) {
					// service name
					if (ids.length == 2) {
						return this.processor.getActionMenu(service);
					}
					// service name binding uri
					else if (ids.length == 4 && ids[2].equals("binding")) {
						for (Binding binding : service.getBinding()) {
							if (ids[3].equals(binding.getUri())) {
								return this.processor.getActionMenu(binding);
							}
						}
					}
				}
			}
		}
		else if (ids[0].equals("reference")) {
			for (org.eclipse.stp.sca.Reference reference : composite.getReference()) {
				if (reference.getName().equals(ids[1])) {
					// reference name
					if (ids.length == 2) {
						return this.processor.getActionMenu(reference);
					}
					// reference name binding uri
					else if (ids.length == 4 && ids[2].equals("binding")) {
						for (Binding binding : reference.getBinding()) {
							if (ids[3].equals(binding.getUri())) {
								return this.processor.getActionMenu(binding);
							}
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public synchronized void addElement(String id, String action) {
		String[] ids = id.split(" ");
		Composite composite = serviceManager.getComposite();
		// composite
		if (ids.length == 1) {
			if(action.equals("addComponent")){
				Component component = ScaFactory.eINSTANCE.createComponent();
				component.setName("name");
				composite.getComponent().add(component);
			}
			else if(action.equals("addService")){
				Service service = ScaFactory.eINSTANCE.createService();
				service.setName("name");
				composite.getService().add(service);
			}
			else if(action.equals("addReference")){
				org.eclipse.stp.sca.Reference reference = ScaFactory.eINSTANCE.createReference();
				reference.setName("name");
				composite.getReference().add(reference);
			}
		}
		else if (ids[0].equals("component")) {
			EList<Component> components = composite.getComponent();
			for (Component component : components) {
				if (component.getName().equals(ids[1])) {
					// component name
					if (ids.length == 2) {
						if (action.equals("addComponentService")) {
							ComponentService componentService = ScaFactory.eINSTANCE.createComponentService();
							componentService.setName("name");
							component.getService().add(componentService);
						}
						else if (action.equals("addComponentReference")) {
							ComponentReference componentReference = ScaFactory.eINSTANCE.createComponentReference();
							componentReference.setName("name");
							component.getReference().add(componentReference);
						}
						else if (action.equals("addComponentProperty")) {
							PropertyValue componentProperty = ScaFactory.eINSTANCE.createPropertyValue();
							componentProperty.setName("name");
							component.getProperty().add(componentProperty);
						}
						else if (action.equals("deleteComponent")) {
							composite.getComponent().remove(component);
						}
					}
					// component name implementation
					else if (ids.length == 3) {

					}
					else {
						// component name property name
						if (ids[2].equals("property")) {
							for (PropertyValue property : component.getProperty()) {
								if (property.getName().equals(ids[3])) {
									if(action.equals("deleteComponentProperty")){
										component.getProperty().remove(property);
									}
								}
							}
						}
						else if (ids[2].equals("reference")) {
							for (ComponentReference componentReference : component.getReference()) {
								if (componentReference.getName().equals(ids[3])) {
									// component name reference name
									if (ids.length == 4) {
										if (action.equals("addBinding")) {
											SCABinding binding = ScaFactory.eINSTANCE
													.createSCABinding();
											binding.setUri("uri");
											componentReference.getBinding().add(binding);
										}
										else if(action.equals("deleteComponentReference")){
											component.getReference().remove(componentReference);
										}
									}
									// component name reference name binding uri
									// name
									else if (ids.length == 6 && ids[4].equals("binding")) {
										for (Binding binding : componentReference.getBinding()) {
											if (ids[5].equals(binding.getUri())) {
												if (action.equals("deleteBinding")) {
													componentReference.getBinding().remove(binding);
												}
											}
										}
									}
								}
							}
						}
						else if (ids[2].equals("service")) {
							for (ComponentService componentService : component.getService()) {
								if (componentService.getName().equals(ids[3])) {
									// component name service name
									if (ids.length == 4) {
										if (action.equals("addBinding")) {
											SCABinding binding = ScaFactory.eINSTANCE
													.createSCABinding();
											binding.setUri("uri");
											componentService.getBinding().add(binding);
										}
										else if(action.equals("deleteComponentService")){
											component.getService().remove(componentService);
										}
									}
									// component name service name interface
									else if (ids.length == 6 && ids[4].equals("interface")) {

									}
									// component name service name binding uri
									else if (ids.length == 6 && ids[4].equals("binding")) {
										for (Binding binding : componentService.getBinding()) {
											if (ids[5].equals(binding.getUri())) {
												if (action.equals("deleteBinding")) {
													componentService.getBinding().remove(binding);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		else if (ids[0].equals("service")) {
			for (Service service : composite.getService()) {
				if (service.getName().equals(ids[1])) {
					// service name
					if (ids.length == 2) {
						if (action.equals("addBinding")) {
							SCABinding binding = ScaFactory.eINSTANCE.createSCABinding();
							binding.setUri("uri");
							service.getBinding().add(binding);
						}
						if(action.equals("deleteService")){
							composite.getService().remove(service);
						}
					}
					// service name binding uri
					else if (ids.length == 4 && ids[2].equals("binding")) {
						for (Binding binding : service.getBinding()) {
							if (ids[3].equals(binding.getUri())) {
								if (action.equals("deleteBinding")) {
									service.getBinding().remove(binding);
								}
							}
						}
					}
				}
			}
		}
		else if (ids[0].equals("reference")) {
			for (org.eclipse.stp.sca.Reference reference : composite.getReference()) {
				if (reference.getName().equals(ids[1])) {
					// reference name
					if (ids.length == 2) {
						if (action.equals("addBinding")) {
							SCABinding binding = ScaFactory.eINSTANCE.createSCABinding();
							binding.setUri("uri");
							reference.getBinding().add(binding);
						}
						if(action.equals("deleteReference")){
							composite.getReference().remove(reference);
						}
					}
					// reference name binding uri
					else if (ids.length == 4 && ids[2].equals("binding")) {
						for (Binding binding : reference.getBinding()) {
							if (ids[3].equals(binding.getUri())) {
								if (action.equals("deleteBinding")) {
									reference.getBinding().remove(binding);
								}
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
