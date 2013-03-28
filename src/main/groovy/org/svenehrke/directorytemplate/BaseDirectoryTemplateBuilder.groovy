package org.svenehrke.directorytemplate
import groovy.util.logging.Log

@Log
/**
* Base class for directory template builders
*/
abstract class BaseDirectoryTemplateBuilder {
	Map<String, DTInputParameter> inputParameters

	Map<String, DTInputParameter> askForInputParameters(DTMetaInformation inMetaInformation) {
		// Get all defined input parameters for this template:
		Map<String, DTInputParameter> inputParameterMap = newInputParameters()

		applyStoredPropertiesToInputParameters(inMetaInformation, inputParameterMap.values())

		// Now ask user for each input value:
		DTUtil.askForInputParameters(inputParameterMap.values())

		applyParametersToProperties(inMetaInformation, inputParameterMap.values())

		inputParameterMap.putAll(newDerivedInputParameters(inputParameterMap.values()))
		inputParameterMap
	}

	def applyStoredPropertiesToInputParameters(DTMetaInformation inMetaInformation, Collection<DTInputParameter> inInputParameters) {
		DTMetaFolder metaFolder = new DTMetaFolder(metaInformation: inMetaInformation)

		// Load properties from previous runs:
		Properties props = metaFolder.newPropertiesFromFile()

		// Apply property values from previous runs to values of defined input parameters:
		new DTInputParameters().applyPropertiesToInputParameters(inInputParameters, props)
	}

	/** Store possibly modified parameters back to property file */
	def applyParametersToProperties(DTMetaInformation inMetaInformation, Collection<DTInputParameter> inInputParameters) {
		DTMetaFolder metaFolder = new DTMetaFolder(metaInformation: inMetaInformation)
		Properties props = metaFolder.newPropertiesFromFile()

		new DTInputParameters().applyParametersToProperties(inInputParameters, props)
		metaFolder.storeProperties(props)

	}

	Collection<DTInputParameter> newInputParameters() {
		[]
	}

	Collection<DTInputParameter> newDerivedInputParameters(Collection<DTInputParameter> aInputParameters) {
		[:]
	}

	abstract Map<String, String> newFilenameBinding(Map<String, DTInputParameter> aInputParameters)
	abstract Map<String, String> newTextBinding(Map<String, DTInputParameter> aInputParameters)

	// find file extensions in folder (http://lookherefirst.wordpress.com/2008/10/01/how-to-list-all-file-extensions-within-a-directory/)):
	//   find build -type f| sed -e "s/.*\./\./"|sort|uniq -c|grep -e '\..*'|sort -rn
	List<String> getExclusions() { [] }

}
