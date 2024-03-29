/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easysoa.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import javax.xml.namespace.QName;

import org.easysoa.api.RESTCall;
import org.easysoa.api.ServiceManager;
import org.easysoa.api.Users;
import org.easysoa.api.Utils;
import org.easysoa.compositeTemplates.CompositeTemplateProcessorItf;
import org.easysoa.model.User;
import org.easysoa.processor.BindingProcessorItf;
import org.easysoa.processor.ComplexProcessorItf;
import org.easysoa.processor.ImplementationsProcessorItf;
import org.easysoa.processor.InterfaceProcessorItf;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.stp.sca.Binding;
import org.eclipse.stp.sca.Component;
import org.eclipse.stp.sca.ComponentReference;
import org.eclipse.stp.sca.ComponentService;
import org.eclipse.stp.sca.Composite;
import org.eclipse.stp.sca.Implementation;
import org.eclipse.stp.sca.Interface;
import org.eclipse.stp.sca.JavaImplementation;
import org.eclipse.stp.sca.PropertyValue;
import org.eclipse.stp.sca.SCABinding;
import org.eclipse.stp.sca.SCAImplementation;
import org.eclipse.stp.sca.ScaFactory;
import org.eclipse.stp.sca.Service;
import org.eclipse.stp.sca.domainmodel.frascati.FrascatiFactory;
import org.eclipse.stp.sca.domainmodel.frascati.FrascatiPackage;
import org.eclipse.stp.sca.domainmodel.frascati.ScriptImplementation;
import org.osoa.sca.annotations.Reference;
import org.osoa.sca.annotations.Scope;
import org.ow2.frascati.metamodel.web.VelocityImplementation;
import org.ow2.frascati.metamodel.web.WebFactory;
import org.ow2.frascati.metamodel.web.WebPackage;

/**
 * 
 * @author Michel Dirix
 */
@Scope("COMPOSITE")
public class RESTCallImpl implements RESTCall {

	@Reference
	protected ServiceManager serviceManager;
	@Reference
	protected Utils utils;
	@Reference
	protected Users users;
	@Reference
	protected ComplexProcessorItf processor;
	@Reference
	protected CompositeTemplateProcessorItf compositeTemplates;
	@Reference
	protected ImplementationsProcessorItf implementations;
	@Reference
	protected InterfaceProcessorItf interfaces;
	@Reference
	protected BindingProcessorItf bindings;

	@Override
	public String getCompositeTree() {
		Composite composite = serviceManager.getComposite();
		return this.processor.getMenuItem(composite, "").toJSONString();
	}

	private synchronized EObject getComponent(String id) {
		String[] ids = id.split(" ");
		if (ids[ids.length - 1].contains("_")) {
			ids[ids.length - 1] = ids[ids.length - 1].substring(0,
					ids[ids.length - 1].indexOf("_"));
		}
		Composite composite = serviceManager.getComposite();
		// composite
		if (ids.length == 1) {
			return composite;
		} else if (ids[0].equals("component")) {
			EList<Component> components = composite.getComponent();
			for (Component component : components) {
				if (component.getName().equals(ids[1])) {
					// component name
					if (ids.length == 2) {
						return component;
					}
					// component name implementation
					else if (ids.length == 3) {
						return component.getImplementation();
					} else {
						// component name property name
						if (ids[2].equals("property")) {
							for (PropertyValue property : component
									.getProperty()) {
								if (property.getName().equals(ids[3])) {
									return property;
								}
							}
						} else if (ids[2].equals("reference")) {
							for (ComponentReference componentReference : component
									.getReference()) {
								if (componentReference.getName().equals(ids[3])) {
									// component name reference name
									if (ids.length == 4) {
										return componentReference;
									}
									// component name reference name binding
									// name
									// name
									else if (ids.length == 6
											&& ids[4].equals("binding")) {
										for (Binding binding : componentReference
												.getBinding()) {
											if (ids[5]
													.equals(binding.getName())) {
												return binding;
											}
										}
									}
								}
							}
						} else if (ids[2].equals("service")) {
							for (ComponentService componentService : component
									.getService()) {
								if (componentService.getName().equals(ids[3])) {
									// component name service name
									if (ids.length == 4) {
										return componentService;
									}
									// component name service name interface
									else if (ids.length == 6
											&& ids[4].equals("interface")) {
										return componentService.getInterface();
									}
									// component name service name binding uri
									else if (ids.length == 6
											&& ids[4].equals("binding")) {
										for (Binding binding : componentService
												.getBinding()) {
											if (ids[5]
													.equals(binding.getName())) {
												System.out.println("URI : "
														+ binding.getUri());
												System.out.println("NAME : "
														+ binding.getName());
												return binding;
											}
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
					if (ids.length == 2) {
						return service;
					}
					// service name binding uri
					else if (ids.length == 4 && ids[2].equals("binding")) {
						for (Binding binding : service.getBinding()) {
							if (ids[3].equals(binding.getName())) {
								return binding;
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
					if (ids.length == 2) {
						return reference;
					}
					// reference name binding uri
					else if (ids.length == 4 && ids[2].equals("binding")) {
						for (Binding binding : reference.getBinding()) {
							if (ids[3].equals(binding.getName())) {
								return binding;
							}
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public synchronized String getComponentContent(String id) {
		return this.processor.getPanel(this.getComponent(id));
	}

	@Override
	public synchronized String getComponentMenu(String id) {
		return this.processor.getActionMenu(this.getComponent(id));
	}

	@Override
	public synchronized void addElement(String id, String action) {
		String[] ids = id.split(" ");
		Composite composite = serviceManager.getComposite();
		// composite
		if (ids.length == 1) {
			if (action.equals("addComponent")) {
				Component component = ScaFactory.eINSTANCE.createComponent();
				component.setName("name");
				composite.getComponent().add(component);
			} else if (action.equals("addService")) {
				Service service = ScaFactory.eINSTANCE.createService();
				service.setName("name");
				composite.getService().add(service);
			} else if (action.equals("addReference")) {
				org.eclipse.stp.sca.Reference reference = ScaFactory.eINSTANCE
						.createReference();
				reference.setName("name");
				composite.getReference().add(reference);
			}
		} else if (ids[0].equals("component")) {
			Iterator<Component> components = composite.getComponent()
					.iterator();
			while (components.hasNext()) {
				Component component = components.next();
				if (component.getName().equals(ids[1])) {
					// component name
					if (ids.length == 2) {
						if (action.equals("addComponentService")) {
							ComponentService componentService = ScaFactory.eINSTANCE
									.createComponentService();
							componentService.setName("name");
							component.getService().add(componentService);
						} else if (action.equals("addComponentReference")) {
							ComponentReference componentReference = ScaFactory.eINSTANCE
									.createComponentReference();
							componentReference.setName("name");
							component.getReference().add(componentReference);
						} else if (action.equals("addComponentProperty")) {
							PropertyValue componentProperty = ScaFactory.eINSTANCE
									.createPropertyValue();
							componentProperty.setName("name");
							component.getProperty().add(componentProperty);
						} else if (action.equals("deleteComponent")) {
							components.remove();
						}
					}
					// component name implementation
					else if (ids.length == 3) {
						if (action.equals("deleteImplementation")) {
							((FeatureMap.Internal) component
									.getImplementationGroup()).clear();
						}
					} else {
						// component name property name
						if (ids[2].equals("property")) {
							Iterator<PropertyValue> iteratorPropertyValue = component
									.getProperty().iterator();
							while (iteratorPropertyValue.hasNext()) {
								PropertyValue property = iteratorPropertyValue
										.next();
								if (property.getName().equals(ids[3])) {
									if (action
											.equals("deleteComponentProperty")) {
										iteratorPropertyValue.remove();
									}
								}
							}
						} else if (ids[2].equals("reference")) {
							Iterator<ComponentReference> iteratorComponentReference = component
									.getReference().iterator();
							while (iteratorComponentReference.hasNext()) {
								ComponentReference componentReference = iteratorComponentReference
										.next();
								if (componentReference.getName().equals(ids[3])) {
									// component name reference name
									if (ids.length == 4) {
										if (action.equals("addBinding")) {
											SCABinding binding = ScaFactory.eINSTANCE
													.createSCABinding();
											binding.setName("name");
											binding.setUri("uri");
											componentReference.getBinding()
													.add(binding);
										} else if (action
												.equals("deleteComponentReference")) {
											iteratorComponentReference.remove();
										}
									}
									// component name reference name binding uri
									// name
									else if (ids.length == 6
											&& ids[4].equals("binding")) {
										Iterator<Binding> iteratorBinding = componentReference
												.getBinding().iterator();
										while (iteratorBinding.hasNext()) {
											Binding binding = iteratorBinding
													.next();
											if (ids[5]
													.equals(binding.getName())) {
												if (action
														.equals("deleteBinding")) {
													iteratorBinding.remove();
												}
											}
										}
									}
								}
							}
						} else if (ids[2].equals("service")) {
							Iterator<ComponentService> iteratorComponentService = component
									.getService().iterator();
							while (iteratorComponentService.hasNext()) {
								ComponentService componentService = iteratorComponentService
										.next();
								if (componentService.getName().equals(ids[3])) {
									// component name service name
									if (ids.length == 4) {
										if (action.equals("addBinding")) {
											SCABinding binding = ScaFactory.eINSTANCE
													.createSCABinding();
											binding.setUri("uri");
											binding.setName("name");
											componentService.getBinding().add(
													binding);
										} else if (action
												.equals("deleteComponentService")) {
											iteratorComponentService.remove();
										}
									}
									// component name service name interface
									else if (ids.length == 6
											&& ids[4].equals("interface")) {

									}
									// component name service name binding uri
									else if (ids.length == 6
											&& ids[4].equals("binding")) {
										Iterator<Binding> iteratorBinding = componentService
												.getBinding().iterator();
										while (iteratorBinding.hasNext()) {
											Binding binding = iteratorBinding
													.next();
											if (ids[5]
													.equals(binding.getName())) {
												if (action
														.equals("deleteBinding")) {
													iteratorBinding.remove();
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
		} else if (ids[0].equals("service")) {
			Iterator<Service> iteratorService = composite.getService()
					.iterator();
			while (iteratorService.hasNext()) {
				Service service = iteratorService.next();
				if (service.getName().equals(ids[1])) {
					// service name
					if (ids.length == 2) {
						if (action.equals("addBinding")) {
							SCABinding binding = ScaFactory.eINSTANCE
									.createSCABinding();
							binding.setUri("uri");
							binding.setName("name");
							service.getBinding().add(binding);
						}
						if (action.equals("deleteService")) {
							iteratorService.remove();
						}
					}
					// service name binding uri
					else if (ids.length == 4 && ids[2].equals("binding")) {
						Iterator<Binding> iteratorBinding = service
								.getBinding().iterator();
						while (iteratorBinding.hasNext()) {
							Binding binding = iteratorBinding.next();
							if (ids[3].equals(binding.getName())) {
								if (action.equals("deleteBinding")) {
									iteratorBinding.remove();
								}
							}
						}
					}
				}
			}
		} else if (ids[0].equals("reference")) {
			Iterator<org.eclipse.stp.sca.Reference> iteratorReference = composite
					.getReference().iterator();
			while (iteratorReference.hasNext()) {
				org.eclipse.stp.sca.Reference reference = iteratorReference
						.next();
				if (reference.getName().equals(ids[1])) {
					// reference name
					if (ids.length == 2) {
						if (action.equals("addBinding")) {
							SCABinding binding = ScaFactory.eINSTANCE
									.createSCABinding();
							binding.setUri("uri");
							binding.setName("name");
							reference.getBinding().add(binding);
						}
						if (action.equals("deleteReference")) {
							iteratorReference.remove();
						}
					}
					// reference name binding uri
					else if (ids.length == 4 && ids[2].equals("binding")) {
						Iterator<Binding> iteratorBinding = reference
								.getBinding().iterator();
						while (iteratorBinding.hasNext()) {
							Binding binding = iteratorBinding.next();
							if (ids[3].equals(binding.getName())) {
								if (action.equals("deleteBinding")) {
									iteratorBinding.remove();
								}
							}
						}
					}
				}
			}
		}
		utils.saveComposite(composite, serviceManager.getCurrentApplication()
				.getCompositeLocation());
		serviceManager.setComposite(composite);
		serviceManager.reloadComposite();
	}

	@Override
	public void saveElement(String params) {
		try {
			params = URLDecoder.decode(params, "UTF-8");
			String[] paramsArray = params.split("&");
			Map<String, Object> map = new HashMap<String, Object>();
			for (String param : paramsArray) {
				String[] paramArray = param.split("=");
				// for empty values in form
				if (paramArray.length == 1)
					map.put(paramArray[0], "");
				else
					map.put(paramArray[0], paramArray[1]);
			}
			EObject eobject = this.getComponent((String) map.get("id"));

			eobject = this.processor.saveElement(eobject, map);
			this.modifyModel((String) map.get("id"), eobject);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void modifyModel(String id, EObject eobject) {
		String[] ids = id.split(" ");
		Composite composite = serviceManager.getComposite();
		// composite
		if (ids.length == 1) {
			composite = (Composite) eobject;
		} else if (ids[0].equals("component")) {
			EList<Component> components = composite.getComponent();
			for (Component component : components) {
				if (component.getName().equals(ids[1])) {
					// component name
					if (ids.length == 2) {
						component = (Component) eobject;

					}
					// component name implementation
					else if (ids.length == 3) {
						org.eclipse.stp.sca.Implementation osoaImplementation = transform(component
								.getImplementation());
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
						if (eReference != null) {
							((FeatureMap.Internal) component
									.getImplementationGroup())
									.add(org.eclipse.stp.sca.ScaPackage.Literals.COMPONENT__IMPLEMENTATION_GROUP,
											org.eclipse.emf.ecore.util.FeatureMapUtil
													.createEntry(eReference,
															osoaImplementation));
						}
					} else {
						// component name property name
						if (ids[2].equals("property")) {
							for (PropertyValue property : component
									.getProperty()) {
								if (property.getName().equals(ids[3])) {
									property = (PropertyValue) eobject;
								}
							}
						} else if (ids[2].equals("reference")) {
							for (ComponentReference componentReference : component
									.getReference()) {
								if (componentReference.getName().equals(ids[3])) {
									// component name reference name
									if (ids.length == 4) {
										componentReference = (ComponentReference) eobject;
									}
									// component name reference name binding uri
									// name
									else if (ids.length == 6
											&& ids[4].equals("binding")) {
										Iterator<Binding> iterator = componentReference
												.getBinding().iterator();
										while (iterator.hasNext()) {
											Binding binding = iterator.next();
											if (binding.getName()
													.equals(ids[5])) {
												binding = (Binding) eobject;
											}

										}
									}
								}
							}
						} else if (ids[2].equals("service")) {
							for (ComponentService componentService : component
									.getService()) {
								if (componentService.getName().equals(ids[3])) {
									// component name service name
									if (ids.length == 4) {
										componentService = (ComponentService) eobject;
									}
									// component name service name interface
									else if (ids.length == 6
											&& ids[4].equals("interface")) {
										Interface interf = componentService
												.getInterface();
										interf = (Interface) eobject;
									}
									// component name service name binding uri
									else if (ids.length == 6
											&& ids[4].equals("binding")) {
										Iterator<Binding> iterator = componentService
												.getBinding().iterator();
										while (iterator.hasNext()) {
											Binding binding = iterator.next();
											if (binding.getName()
													.equals(ids[5])) {
												binding = (Binding) eobject;
											}
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
					if (ids.length == 2) {
						service = (Service) eobject;
					}
					// service name binding uri
					else if (ids.length == 4 && ids[2].equals("binding")) {
						Iterator<Binding> iterator = service.getBinding()
								.iterator();
						while (iterator.hasNext()) {
							Binding binding = iterator.next();
							if (binding.getName().equals(ids[3])) {
								iterator.remove();
								service.getBinding().add(binding);
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
					if (ids.length == 2) {
						reference = (org.eclipse.stp.sca.Reference) eobject;
					}
					// reference name binding uri
					else if (ids.length == 4 && ids[2].equals("binding")) {
						Iterator<Binding> iterator = reference.getBinding()
								.iterator();
						while (iterator.hasNext()) {
							Binding binding = iterator.next();
							if (binding.getName().equals(ids[3])) {
								iterator.remove();
								reference.getBinding().add(binding);
								break;
							}

						}
					}
				}
			}
		}
		utils.saveComposite(composite, serviceManager.getCurrentApplication()
				.getCompositeLocation());
		serviceManager.setComposite(composite);
		serviceManager.reloadComposite();
	}

	@Override
	public String getTemplateForm(String templateName) {
		return this.compositeTemplates.getForm(templateName);
	}

	@Override
	public void createApplication(String params) {
		try {
			params = URLDecoder.decode(params, "UTF-8");
			String[] paramsArray = params.split("&");
			Map<String, Object> map = new HashMap<String, Object>();
			for (String param : paramsArray) {
				String[] paramArray = param.split("=");
				map.put(paramArray[0], paramArray[1]);
			}
			User user = users.searchUser((String) map.get("user"));
			serviceManager.createService(user, (String) map.get("name"),
					(String) map.get("description"),
					(String) map.get("package"), (String) map.get("template"),
					map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized String getImplementationContent(String modelId,
			String id) {
		System.out.println("getImplementationContent");
		return this.implementations.getImplementationView(
				serviceManager.getComposite(), modelId, id);
	}

	@Override
	public synchronized String getInterfaceContent(String modelId, String id) {
		return this.interfaces.getInterfaceView(serviceManager.getComposite(),
				modelId, id);
	}

	@Override
	public String getBindingContent(String modelId, String id) {
		return this.bindings.getBindingView(serviceManager.getComposite(),
				modelId, id);
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
		if (oasisImplementation instanceof ScriptImplementation) {
			ScriptImplementation oasisScriptImplementation = (ScriptImplementation) oasisImplementation;
			// Create an OSOA JavaImplementation.
			ScriptImplementation osoaScriptImplementation = FrascatiFactory.eINSTANCE
					.createScriptImplementation();
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
	public String getImplementationFileContent() {
		try {
			String url = this.implementations.getUrl();
			System.out.println("URL : " + url);
			if (url == null)
				return "";
			File impl = new File(url);

			Scanner scanner = new Scanner(impl);
			StringBuffer sb = new StringBuffer();
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				sb.append(line + System.getProperty("line.separator"));
			}

			scanner.close();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String getEditorMode() {
		return this.implementations.getEditorMode();
	}

	@Override
	public void saveImplementation(String content) {
		System.out.println("save implem");
		System.out.println(content);
		String url = this.implementations.getUrl();
		if (url != null) {
			File file = new File(url);
			try {
				FileWriter fw = new FileWriter(file, false);
				BufferedWriter output = new BufferedWriter(fw);

				output.write(content);
				output.flush();
				output.close();
				fw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String getExistingImplementations() {
		System.out.println("getExistingImplementations");
		String implementations = "";
		Composite composite = serviceManager.getComposite();
		for (Component component : composite.getComponent()) {
			Implementation implementation = component.getImplementation();
			if (implementation != null) {
				if (implementation instanceof SCAImplementation) {
					SCAImplementation oasisSCAImplementation = (SCAImplementation) implementation;
					if (!implementations.contains(oasisSCAImplementation
							.getName().getLocalPart()))
						implementations += (oasisSCAImplementation.getName()
								.getLocalPart());
				}
				if (implementation instanceof JavaImplementation) {
					JavaImplementation oasisJavaImplementation = (JavaImplementation) implementation;
					if (!implementations.contains(oasisJavaImplementation
							.getClass_()))
						implementations += (oasisJavaImplementation.getClass_());
				}
				if (implementation instanceof VelocityImplementation) {
					VelocityImplementation oasisVelocityImplementation = (VelocityImplementation) implementation;
					if (!implementations.contains(oasisVelocityImplementation
							.getLocation()
							+ "/"
							+ oasisVelocityImplementation.getDefault()))
						implementations += (oasisVelocityImplementation
								.getLocation() + "/" + oasisVelocityImplementation
								.getDefault());
				}
				if (implementation instanceof ScriptImplementation) {
					ScriptImplementation oasisScriptImplementation = (ScriptImplementation) implementation;
					if (!implementations.contains(oasisScriptImplementation
							.getScript()))
						implementations += (oasisScriptImplementation
								.getScript());
				}
				implementations += ",";
			}
		}
		if (implementations.length() > 0
				&& implementations.lastIndexOf(",") != -1) {
			implementations = implementations.substring(0,
					implementations.length() - 1);
		}
		System.out.println(implementations);
		return implementations;
	}

	@Override
	public void uploadImplementation(String file, String implementationType, String id) {
		try {
			System.out.println("uploadImplementation# file : "+file+" implementationType : "+implementationType+" id : "+id);
//			id = URLDecoder.decode(id, "UTF-8");
//			System.out.println("createUsedImplementation # id : " + id
//					+ " className : " + className);
//			Composite composite = serviceManager.getComposite();
//			Implementation implementation = null;
//			for (Component component : composite.getComponent()) {
//				System.out.println("componentName : " + component.getName());
//				Implementation componentImplementation = component
//						.getImplementation();
//				if (componentImplementation != null) {
//					System.out.println("implem not null");
//					if (componentImplementation instanceof SCAImplementation) {
//						SCAImplementation oasisSCAImplementation = (SCAImplementation) componentImplementation;
//						if (className.equals(oasisSCAImplementation.getName()
//								.getLocalPart())) {
//							implementation = oasisSCAImplementation;
//						}
//					}
//					if (componentImplementation instanceof JavaImplementation) {
//						System.out.println("java instance");
//						JavaImplementation oasisJavaImplementation = (JavaImplementation) componentImplementation;
//						System.out.println("className : " + className);
//						System.out.println("implem : "
//								+ oasisJavaImplementation.getClass_());
//						if (className.equals(oasisJavaImplementation
//								.getClass_()))
//							implementation = oasisJavaImplementation;
//						System.out.println("found");
//						if (implementation != null) {
//							System.out.println("found and not null");
//						} else {
//							System.out.println("found and null");
//						}
//					}
//					if (componentImplementation instanceof VelocityImplementation) {
//						VelocityImplementation oasisVelocityImplementation = (VelocityImplementation) componentImplementation;
//						if (className.equals(oasisVelocityImplementation
//								.getLocation()
//								+ "/"
//								+ oasisVelocityImplementation.getDefault()))
//							implementation = oasisVelocityImplementation;
//					}
//					if (componentImplementation instanceof ScriptImplementation) {
//						ScriptImplementation oasisScriptImplementation = (ScriptImplementation) componentImplementation;
//						if (className.equals(oasisScriptImplementation
//								.getScript()))
//							implementation = oasisScriptImplementation;
//					}
//				}
//			}
//			if (implementation != null) {
//				System.out.println("implementation not null");
//				Component component = (Component) this.getComponent(id);
//				org.eclipse.stp.sca.Implementation osoaImplementation = transform(implementation);
//				EReference eReference = null;
//				if (osoaImplementation instanceof org.eclipse.stp.sca.SCAImplementation) {
//					eReference = org.eclipse.stp.sca.ScaPackage.Literals.DOCUMENT_ROOT__IMPLEMENTATION_COMPOSITE;
//				}
//				if (osoaImplementation instanceof org.eclipse.stp.sca.JavaImplementation) {
//					eReference = org.eclipse.stp.sca.ScaPackage.Literals.DOCUMENT_ROOT__IMPLEMENTATION_JAVA;
//				}
//				if (osoaImplementation instanceof VelocityImplementation) {
//					eReference = WebPackage.Literals.DOCUMENT_ROOT__IMPLEMENTATION_VELOCITY;
//				}
//				if (osoaImplementation instanceof ScriptImplementation) {
//					eReference = FrascatiPackage.Literals.DOCUMENT_ROOT__IMPLEMENTATION_SCRIPT;
//				}
//				// TODO: manage other implementations, as BPEL, Spring,
//				// etc.
//
//				((FeatureMap.Internal) component.getImplementationGroup())
//						.clear();
//				if (eReference != null) {
//					((FeatureMap.Internal) component.getImplementationGroup())
//							.add(org.eclipse.stp.sca.ScaPackage.Literals.COMPONENT__IMPLEMENTATION_GROUP,
//									org.eclipse.emf.ecore.util.FeatureMapUtil
//											.createEntry(eReference,
//													osoaImplementation));
//					System.out.println("modify model");
//					this.modifyModel(id, component);
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createNewImplementation(String id, String className,
			String implemType, boolean createFile) {
		try {
			System.out.println("createNewImplementation# id : "+id+" className : "+className+" implemType : "+implemType);
			boolean hasAPackage = false;
			id = URLDecoder.decode(id, "UTF-8");
			String classNameOrigin = className;
			if(implemType.equals("Java")){
				className = this.serviceManager.getCurrentApplication().getPackageName()+".impl."+className.substring(0, className.lastIndexOf("."));
				hasAPackage = true;
			}
			else if(implemType.equals("Script"))
				className = "scripts/"+ className;
			Component component = (Component) this.getComponent(id);
			Implementation implementation = (Implementation) this.implementations
					.createImplementation(implemType);
			org.eclipse.stp.sca.Implementation osoaImplementation = transform(implementation);
			EReference eReference = null;
			if (osoaImplementation instanceof org.eclipse.stp.sca.SCAImplementation) {
				eReference = org.eclipse.stp.sca.ScaPackage.Literals.DOCUMENT_ROOT__IMPLEMENTATION_COMPOSITE;
				SCAImplementation scaImplementation = (SCAImplementation) implementation;
				scaImplementation.setName(new QName(className));
				implementation = scaImplementation;
			}
			if (osoaImplementation instanceof org.eclipse.stp.sca.JavaImplementation) {
				System.out.println("java implementation");
				eReference = org.eclipse.stp.sca.ScaPackage.Literals.DOCUMENT_ROOT__IMPLEMENTATION_JAVA;
				JavaImplementation javaImplementation = (JavaImplementation) implementation;
				javaImplementation.setClass(className);
				implementation = javaImplementation;
			}
			if (osoaImplementation instanceof VelocityImplementation) {
				eReference = WebPackage.Literals.DOCUMENT_ROOT__IMPLEMENTATION_VELOCITY;
				VelocityImplementation velocityImplementation = (VelocityImplementation) implementation;
				String[] classNameArgs = className.split("/");
				velocityImplementation.setLocation(classNameArgs[0]);
				velocityImplementation.setDefault(classNameArgs[1]);
				implementation = velocityImplementation;
			}
			if (osoaImplementation instanceof ScriptImplementation) {
				eReference = FrascatiPackage.Literals.DOCUMENT_ROOT__IMPLEMENTATION_SCRIPT;
				ScriptImplementation scriptImplementation = (ScriptImplementation) implementation;
				scriptImplementation.setScript(className);
				implementation = scriptImplementation;
			}
			// TODO: manage other implementations, as BPEL, Spring,
			// etc.
			if(createFile)
			this.serviceManager.createFile(implemType, classNameOrigin);
			if(hasAPackage)
			this.serviceManager.changePackage(implemType, classNameOrigin);
			
			((FeatureMap.Internal) component.getImplementationGroup()).clear();
			if (eReference != null) {
				((FeatureMap.Internal) component.getImplementationGroup())
						.add(org.eclipse.stp.sca.ScaPackage.Literals.COMPONENT__IMPLEMENTATION_GROUP,
								org.eclipse.emf.ecore.util.FeatureMapUtil
										.createEntry(eReference, implementation));
				System.out.println("modify model");
				this.modifyModel(id, component);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
