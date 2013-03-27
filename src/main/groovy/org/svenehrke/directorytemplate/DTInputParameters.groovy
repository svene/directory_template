package org.svenehrke.directorytemplate

class DTInputParameters {
	def applyParametersToProperties(Map<String, DTInputParameter> parameters, Properties props) {
		parameters.each {ip ->
			log.info "-->setting property $ip.key to $ip.value.value"
			props.setProperty(ip.key, ip.value.value)
		}
	}

	def applyPropertiesToInputParameters(Map<String, DTInputParameter> parameters, Properties props) {
		parameters.each {ip ->
			String v = props.get(ip.key)
			if (v) {
				ip.value.value = v // Put property value on value of DTInputParameter
			}
		}
	}
}
