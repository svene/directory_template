package org.svenehrke.directorytemplate
import groovy.util.logging.Log

@Log
/**
* Base class for directory template builders
*/
abstract class BaseDirectoryTemplateBuilder {
	Map<String, DTInputParameter> inputParameters

	abstract String templateName()
	
	def zipName(String inTemplateName) {
		"dt_${inTemplateName()}.zip"
	}
	String templateFolderName() {
		"${metaInfoFolderName()}/${templateName()}"
	}
	static String metaInfoFolderName() {
		'.directory_template'
	}
	static String inputParametersFilename() {
		"${metaInfoFolderName()}/inputParameters.properties"
	}


	Map<String, DTInputParameter> askForInputParameters(Map<String, DTInputParameter> aInputParameters, String inInputParametersFilename) {
		// Get all defined input parameters for this template:
		Map<String, DTInputParameter> result = newInputParameters()

		// Load properties from previous runs:
		Properties props = newPropertiesFromFile(inInputParametersFilename)

		// Apply property values from previous runs to values of defined input parameters:
		applyPropertiesToInputParameters(result, props)

		// Now ask user for each input value:
		DTUtil.askForInputParameters(result*.value)
		result << aInputParameters

		// Store possibly modified parameters back as property file:
		applyParametersToProperties(result, props)
		storeMetaInfoFile(props, inInputParametersFilename)
		

		result.putAll(newDerivedInputParameters(result))
		result
	}

	private static storeMetaInfoFile(Properties props, String inInputParametersFilename) {
		File propFile = new File(inInputParametersFilename)
		log.info 'Store properties to file'
		propFile.withWriter {
			props.store(it, '')
		}
	}

	private static applyParametersToProperties(Map<String, DTInputParameter> parameters, Properties props) {
		parameters.each {ip ->
			log.info "-->setting property $ip.key to $ip.value.value"
			props.setProperty(ip.key, ip.value.value)
		}
	}

	private static applyPropertiesToInputParameters(Map<String, DTInputParameter> parameters, Properties props) {
		parameters.each {ip ->
			String v = props.get(ip.key)
			if (v) {
				ip.value.value = v // Put property value on value of DTInputParameter
			}
		}
	}

	private static Properties newPropertiesFromFile(String inInputParametersFilename) {
		File propFile = new File(inInputParametersFilename)
		Properties props = new Properties()
		propFile.withReader {
			props.load(it)
		}
		props
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
