package org.svenehrke.directorytemplate

class DevelopmentInputParameterProvider {
	def askForInputParameters(Collection aInputParameters) {
		println aInputParameters
		aInputParameters.each() { ip ->
			askForInputParameter ip
		}
	}

	def askForInputParameter(def aInputParameter) {
		if (aInputParameter.name == 'packagename') {
			aInputParameter.value = 'someotherpackage'
		}
	}
}
