package org.svenehrke.dt.java

import org.svenehrke.directorytemplate.DTInputParameter

class SimpleJavaDTBuilder extends StandardJavaDTBuilder {

	@Override
	Collection<DTInputParameter> newInputParameters() {
		Map<String, DTInputParameter> result = super.newInputParameters()
		result['packagename'] = new DTInputParameter(name: 'packagename', value: 'org.svenehrke.simplejava', prompt: 'Packagename')
		result
	}

}
