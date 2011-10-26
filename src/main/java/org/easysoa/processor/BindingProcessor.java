package org.easysoa.processor;

import java.util.ArrayList;
import java.util.List;

import org.osoa.sca.annotations.Reference;

public class BindingProcessor implements BindingProcessorItf {

	@Reference
	private List<ComplexProcessorItf> processors;

	@Override
	public List<String> allAvailableBindingsLabel() {
		List<String> labels = new ArrayList<String>();
		for (ComplexProcessorItf processor : this.processors) {
			labels.add(processor.getLabel(null));
		}
		return labels;
	}

}
