package org.svenehrke.directorytemplate
import groovy.util.logging.Log

@Log
/**
* Base class for directory template builders
*/
abstract class BaseDirectoryTemplateBuilder {
	Map<String, DTInputParameter> inputParameters

	Map<String, DTInputParameter> askForInputParameters(DTMetaInfo inMetaInformation) {
		// Get all defined input parameters for this template:
		Collection<DTInputParameter> inputParameters = newInputParameters()

		new DTInputParameterStorage().loadParameters(inMetaInformation, inputParameters)

		// Now ask user for each input value:
		new ConsoleInputParameterProvider().askForInputParameters(inputParameters)

		new DTInputParameterStorage().storeParameters(inMetaInformation, inputParameters)

		inputParameters << newDerivedInputParameters(inputParameters)
		inputParameters
	}


	/** Store possibly modified parameters back to property file */

	Collection<DTInputParameter> newInputParameters() {
		[]
	}

	Collection<DTInputParameter> newDerivedInputParameters(Collection<DTInputParameter> aInputParameters) {
		[]
	}

	abstract Map<String, String> newFilenameBinding(Map<String, DTInputParameter> aInputParameters)
	abstract Map<String, String> newTextBinding(Map<String, DTInputParameter> aInputParameters)

	// find file extensions in folder (http://lookherefirst.wordpress.com/2008/10/01/how-to-list-all-file-extensions-within-a-directory/)):
	//   find build -type f| sed -e "s/.*\./\./"|sort|uniq -c|grep -e '\..*'|sort -rn
	List<String> getExclusions() { [] }

}
