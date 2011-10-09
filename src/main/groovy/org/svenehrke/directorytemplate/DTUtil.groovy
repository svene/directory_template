package org.svenehrke.directorytemplate

class DTUtil {

	public static String dotsToSlashes(String v) {
		v.replaceAll('\\.', '/')
	}

	public static askForBindings(Map aBinding) {
		aBinding.each() { key, value ->
			aBinding[key] = prompt(key, aBinding[key])
		}
	}

	public static askForInputParameters(List<DTInputParameter> aInputParameters) {
		aInputParameters.each() { ip ->
			ip.value = prompt(ip.prompt, ip.value)
		}
	}

	public static Map inputParametersAsMap(List<DTInputParameter> aInputParameters) {
		def result = [:]
		aInputParameters.each() { ip ->
			result[ip.key] = ip.value
		}
		result
	}
	// original routine from gradle-template plugin:
	static String prompt(String message, String defaultValue = null) {
	   if (defaultValue) {
		  return System.console().readLine("${inputPrompt} ${message} [${defaultValue}] ") ?: defaultValue
	   }
	   return System.console().readLine("${inputPrompt} ${message} ") ?: defaultValue
	}

	static final String lineSep = System.getProperty("line.separator")
	static final String inputPrompt = "${lineSep}??>"

}
