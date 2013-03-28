package org.svenehrke.dt.java

import org.svenehrke.directorytemplate.DTInputParameter

class SimpleSwingDTBuilder extends StandardJavaDTBuilder {

	@Override
	Collection<DTInputParameter> newInputParameters() {
		Collection<DTInputParameter> result = super.newInputParameters()
		result['packagename'] = new DTInputParameter(name: 'packagename', value: 'org.svenehrke.simpleswing', prompt: 'Packagename')
		result
	}

}
