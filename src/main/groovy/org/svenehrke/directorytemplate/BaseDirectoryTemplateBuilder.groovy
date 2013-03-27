package org.svenehrke.directorytemplate
import groovy.util.logging.Log

@Log
/**
* Base class for directory template builders
*/
abstract class BaseDirectoryTemplateBuilder {
	Map<String, DTInputParameter> inputParameters

	abstract String templateName()
	

	Map<String, DTInputParameter> askForInputParameters(Map<String, DTInputParameter> aInputParameters, DTMetaInformation inMetaInformation) {
		// Get all defined input parameters for this template:
		Map<String, DTInputParameter> result = newInputParameters()

		def metaFolder = new DTMetaFolder(metaInformation: inMetaInformation)

		// Load properties from previous runs:
		Properties props = metaFolder.newPropertiesFromFile()

		// Apply property values from previous runs to values of defined input parameters:
		new DTInputParameters().applyPropertiesToInputParameters(result, props)

		// Now ask user for each input value:
		DTUtil.askForInputParameters(result*.value)
		result << aInputParameters

		// Store possibly modified parameters back as property file:
		new DTInputParameters().applyParametersToProperties(result, props)
		metaFolder.storeProperties(props)

		result.putAll(newDerivedInputParameters(result))
		result
	}

	Map<String, DTInputParameter> newInputParameters() {
		[:]
	}
	Map<String, DTInputParameter> newDerivedInputParameters(Map<String, DTInputParameter> aInputParameters) {
		[:]
	}

	abstract Map<String, String> newFilenameBinding(Map<String, DTInputParameter> aInputParameters)
	abstract Map<String, String> newTextBinding(Map<String, DTInputParameter> aInputParameters)

	// find file extensions in folder (http://lookherefirst.wordpress.com/2008/10/01/how-to-list-all-file-extensions-within-a-directory/)):
	//   find build -type f| sed -e "s/.*\./\./"|sort|uniq -c|grep -e '\..*'|sort -rn
	List<String> getExclusions() { [] }

}
