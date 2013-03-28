package org.svenehrke.dt.java
import org.svenehrke.directorytemplate.DTInputParameter
import org.svenehrke.directorytemplate.DTUtil

class DirectoryTemplateDTBuilder extends StandardDTBuilder {

	@Override
	Collection<DTInputParameter> newInputParameters() {
		Map<String, DTInputParameter> result = super.newInputParameters()
		result['group'] = new DTInputParameter(name: 'group', value: 'org.mygroup', prompt: 'Maven Group: ')
		result['templatename'] = new DTInputParameter(name: 'templatename', value: 'MyTemplate', prompt: 'Template name: ')
		result
	}

	@Override
	Collection<DTInputParameter> newDerivedInputParameters(Collection<DTInputParameter> aInputParameters) {
		Collection<DTInputParameter> result = super.newInputParameters()

		def pTemplatename = aInputParameters.find {param -> param.name == 'templatename'}
		def pGroup = aInputParameters.find {param -> param.name == 'group'}
		String lcTemplatename = pTemplatename.value.toLowerCase()
		result <<
		[
			'lctemplatename': new DTInputParameter(name: 'lctemplatename', value: lcTemplatename),
			'packagename': new DTInputParameter(name: 'packagename', value: "${pGroup.value.toLowerCase()}.$lcTemplatename"),
		]
		result
	}

	@Override
	Map<String, String> newFilenameBinding(Map<String, DTInputParameter> aInputParameters) {
		Map result = super.newFilenameBinding(aInputParameters)
		result <<
		[
			'@packagename@':DTUtil.dotsToSlashes(aInputParameters.packagename.value)
			,'@templatename@':aInputParameters.templatename.value
			,'@lctemplatename@':aInputParameters.lctemplatename.value
		]
		result
	}

	@Override
	Map<String, String> newTextBinding(Map<String, DTInputParameter> aInputParameters) {
		Map result = super.newTextBinding(aInputParameters)
		result <<
		[
			'group':aInputParameters.group.value
			,'packagename':aInputParameters.packagename.value
			,'templatename':aInputParameters.templatename.value
			,'lctemplatename':aInputParameters.lctemplatename.value
		]
		result
	}
}
