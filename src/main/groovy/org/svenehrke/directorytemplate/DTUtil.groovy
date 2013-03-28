package org.svenehrke.directorytemplate

class DTUtil {

	static String dotsToSlashes(String v) {
		v.replaceAll('\\.', '/')
	}

	static Map inputParametersAsMap(List<DTInputParameter> aInputParameters) {
		aInputParameters.inject([:]) {map, param -> map << [(param.name): param]}
	}

}
