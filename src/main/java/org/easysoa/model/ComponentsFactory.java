package org.easysoa.model;

import org.eclipse.stp.sca.Component;
import org.eclipse.stp.sca.ComponentReference;
import org.eclipse.stp.sca.ComponentService;
import org.eclipse.stp.sca.JavaImplementation;
import org.eclipse.stp.sca.JavaInterface;
import org.eclipse.stp.sca.PropertyValue;
import org.eclipse.stp.sca.Reference;
import org.eclipse.stp.sca.SCABinding;
import org.eclipse.stp.sca.ScaFactory;
import org.eclipse.stp.sca.Service;
import org.eclipse.stp.sca.WebServiceBinding;
import org.eclipse.stp.sca.domainmodel.frascati.FrascatiFactory;
import org.eclipse.stp.sca.domainmodel.frascati.RestBinding;
import org.ow2.frascati.metamodel.web.HttpBinding;
import org.ow2.frascati.metamodel.web.VelocityImplementation;
import org.ow2.frascati.metamodel.web.WebFactory;

public class ComponentsFactory implements ComponentsFactoryItf {

	@Override
	public Component createComponent() {
		return ScaFactory.eINSTANCE.createComponent();
	}

	@Override
	public ComponentService createComponentService() {
		return ScaFactory.eINSTANCE.createComponentService();
	}

	@Override
	public ComponentReference createComponentReference() {
		return ScaFactory.eINSTANCE.createComponentReference();
	}

	@Override
	public PropertyValue createPropertyValue() {
		return ScaFactory.eINSTANCE.createPropertyValue();
	}

	@Override
	public Service createService() {
		return ScaFactory.eINSTANCE.createService();
	}

	@Override
	public Reference createReference() {
		return ScaFactory.eINSTANCE.createReference();
	}

	@Override
	public HttpBinding createHttpBinding() {
		return WebFactory.eINSTANCE.createHttpBinding();
	}

	@Override
	public JavaImplementation createJavaImplementation() {
		return ScaFactory.eINSTANCE.createJavaImplementation();
	}

	@Override
	public VelocityImplementation createVelocityImplementation() {
		return WebFactory.eINSTANCE.createVelocityImplementation();
	}

	@Override
	public JavaInterface createJavaInterface() {
		return ScaFactory.eINSTANCE.createJavaInterface();
	}

	@Override
	public RestBinding createRestBinding() {
		return FrascatiFactory.eINSTANCE.createRestBinding();
	}

	@Override
	public SCABinding createScaBinding() {
		return ScaFactory.eINSTANCE.createSCABinding();
	}

	@Override
	public WebServiceBinding createWebServiceBinding() {
		return ScaFactory.eINSTANCE.createWebServiceBinding();
	}

}
