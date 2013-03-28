package org.svenehrke.dt.java

import org.svenehrke.directorytemplate.DTInputParameter

class GradleDTBuilder extends StandardDTBuilder {

	@Override
	Collection<DTInputParameter> newInputParameters() {
		Collection<DTInputParameter> result = super.newInputParameters()
		result['packagename'] = new DTInputParameter(name: 'packagename', value: 'org.svenehrke.simplejava', prompt: 'Packagename')
		result
	}

	@Override
	Map<String, String> newTextBinding(Map<String, DTInputParameter> aInputParameters) {
		Map result = super.newTextBinding(aInputParameters)
		result <<
		[
			'packagename':inputParameters.packagename.value
		]
		result
	}

}
