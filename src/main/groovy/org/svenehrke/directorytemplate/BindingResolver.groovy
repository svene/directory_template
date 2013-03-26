package org.svenehrke.directorytemplate

import groovy.util.logging.Log

@Log
class BindingResolver {
	private final Map binding

	BindingResolver(Map inBinding) {
		this.binding = inBinding
	}

	String apply(String inValue) {
		String result = inValue
		binding?.each {key, value ->
			if (inValue.contains(key)) {
				result = result.replaceAll(key, value)
				log.info (":\n** key=$key, value=$value: ${inValue} --> ${result}")
			}
		}
		result
	}
}
