package org.easysoa.model;

import org.eclipse.stp.sca.Component;
import org.eclipse.stp.sca.ComponentReference;
import org.eclipse.stp.sca.ComponentService;
import org.eclipse.stp.sca.JavaImplementation;
import org.eclipse.stp.sca.JavaInterface;
import org.eclipse.stp.sca.PropertyValue;
import org.eclipse.stp.sca.Reference;
import org.eclipse.stp.sca.SCABinding;
import org.eclipse.stp.sca.WebServiceBinding;
import org.eclipse.stp.sca.domainmodel.frascati.RestBinding;
import org.osoa.sca.annotations.Service;
import org.ow2.frascati.metamodel.web.HttpBinding;
import org.ow2.frascati.metamodel.web.VelocityImplementation;

@Service
public interface ComponentsFactoryItf {

	Component createComponent();
	ComponentService createComponentService();
	ComponentReference createComponentReference();
	PropertyValue createPropertyValue();
	org.eclipse.stp.sca.Service createService();
	Reference createReference();
	HttpBinding createHttpBinding();
	JavaImplementation createJavaImplementation();
	VelocityImplementation createVelocityImplementation();
	JavaInterface createJavaInterface();
	RestBinding createRestBinding();
	SCABinding createScaBinding();
	WebServiceBinding createWebServiceBinding();
	
}
