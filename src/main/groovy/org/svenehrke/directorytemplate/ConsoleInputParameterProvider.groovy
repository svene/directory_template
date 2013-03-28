package org.svenehrke.directorytemplate

class ConsoleInputParameterProvider {

	def askForInputParameters(Collection aInputParameters) {
		println aInputParameters
		aInputParameters.each() { ip ->
			askForInputParameter ip
		}
	}

	def askForInputParameter(def aInputParameter) {
		aInputParameter.value = prompt(aInputParameter.prompt, aInputParameter.value)
	}

	/** original routine from gradle-template plugin */
	static String prompt(String message, String defaultValue = null) {
		if (defaultValue) {
			return System.console().readLine("${INPUT_PROMPT} ${message} [${defaultValue}] ") ?: defaultValue
		}
		System.console().readLine("${INPUT_PROMPT} ${message} ") ?: defaultValue
	}

	private static final String LINE_SEPARATOR = System.getProperty('line.separator')
	private static final String INPUT_PROMPT = "${LINE_SEPARATOR}??>"
}
