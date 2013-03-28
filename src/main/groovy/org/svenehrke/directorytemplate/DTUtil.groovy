package org.svenehrke.directorytemplate

class DTUtil {

	static String dotsToSlashes(String v) {
		v.replaceAll('\\.', '/')
	}

	static askForBindings(Map aBinding) {
		aBinding.each() { key, value ->
			aBinding[key] = prompt(key, aBinding[key])
		}
	}

	static askForInputParameter(DTInputParameter aInputParameter) {
		aInputParameter.value = prompt(aInputParameter.prompt, aInputParameter.value)
	}
	static askForInputParameters(Collection<DTInputParameter> aInputParameters) {
		aInputParameters.each() { ip ->
			askForInputParameter ip
		}
	}

	static Map inputParametersAsMap(List<DTInputParameter> aInputParameters) {
		aInputParameters.inject([:]) {map, param -> map << [(param.name): param]}
	}
	// original routine from gradle-template plugin:
	static String prompt(String message, String defaultValue = null) {
	   if (defaultValue) {
		  return System.console().readLine("${INPUT_PROMPT} ${message} [${defaultValue}] ") ?: defaultValue
	   }
	   System.console().readLine("${INPUT_PROMPT} ${message} ") ?: defaultValue
	}

	static final String LINE_SEPARATOR = System.getProperty('line.separator')
	static final String INPUT_PROMPT = "${LINE_SEPARATOR}??>"

}
