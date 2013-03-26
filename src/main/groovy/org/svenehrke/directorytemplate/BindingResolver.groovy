package org.svenehrke.directorytemplate

import groovy.util.logging.Log

@Log
class BindingResolver {
	private final Map binding

	BindingResolver(Map inBinding) {
		this.binding = inBinding
	}

	/** inValue with substrings appearing in binding's keys got replaced with their associated values.
	 * See Test for examples
	 */
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
