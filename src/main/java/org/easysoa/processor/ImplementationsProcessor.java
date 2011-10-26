package org.easysoa.processor;

import java.util.ArrayList;
import java.util.List;

import org.osoa.sca.annotations.Reference;

public class ImplementationsProcessor implements ImplementationsProcessorItf {
	
	@Reference
	private List<ComplexProcessorItf> processors;

	public List<String> allAvailableImplementationsLabel(){
		List<String> labels = new ArrayList<String>();
		for(ComplexProcessorItf processor : this.processors){
			labels.add(processor.getLabel(null));
		}
		return labels;
	}

}
