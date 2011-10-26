package org.easysoa.processor;

import java.util.List;

import org.osoa.sca.annotations.Service;

@Service
public interface ImplementationsProcessorItf {

	List<String> allAvailableImplementationsLabel();
}
