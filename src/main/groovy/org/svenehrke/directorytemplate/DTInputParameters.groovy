package org.svenehrke.directorytemplate

import groovy.util.logging.Slf4j

@Slf4j
class DTInputParameters {

	def find(String inName, def inInputParameters) {
		inInputParameters.find {param -> param.name == inName}
	}

	def applyParametersToProperties(Collection<DTInputParameter> parameters, Properties props) {
		parameters.each {ip ->
			log.info "-->setting property $ip.key to $ip.value"
			props.setProperty(ip.name, ip.value)
		}
	}

	def applyPropertiesToInputParameters(Collection<DTInputParameter> parameters, Properties props) {
		parameters.each {ip ->
			String v = props.get(ip.name)
			if (v) {
				ip.value = v // Put property value on value of DTInputParameter
			}
		}
	}
}
