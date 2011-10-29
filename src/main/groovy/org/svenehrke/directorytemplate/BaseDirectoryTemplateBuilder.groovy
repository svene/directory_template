package org.svenehrke.directorytemplate

import groovy.util.logging.Log

@Log
abstract class BaseDirectoryTemplateBuilder {
	Map<String, DTInputParameter> inputParameters
//	String rootDir

	void createTargetFolder() {
		createMetaInfoFile()

		// Collect input parameters:
		inputParameters = askForInputParameters([:])
		Map<String, String> filenameBinding = newFilenameBinding(inputParameters)
		String tmpRootDirName = 'sj'
		filenameBinding.put('@ROOT_FOLDER@', tmpRootDirName)

		// Iterate over zip-entries and create real folder layout with resolved variables from them:
		String rd = ".meta/directory_template"
		new File(rd).mkdirs()
		DirectoryTemplateResolver.createFolderFromZipResource(getClass().classLoader, getZipName(), filenameBinding, rd)

		// Apply textBinding on extracted files:
		DirectoryTemplateResolver.applyTextBindingToExpandedZip(rd, exclusions, newTextBinding(inputParameters))
		new File("$rd/sj").eachFile { File f ->
			f.renameTo("./$f.name")
		}
	}

	private void createMetaInfoFile() {
		String dtCfgDir = ".meta/directory_template"
		new File(dtCfgDir).mkdirs()
		File f = new File("$dtCfgDir/inputParameters.properties")
		if (!f.exists()) {
			f.createNewFile()
		}
	}
	
	private static String getMetaInfoFilename() {
		".meta/directory_template/inputParameters.properties"
	}
	
	private Map<String, DTInputParameter> askForInputParameters(Map<String, DTInputParameter> aInputParameters) {
		// Get all defined input parameters for this template:
		Map<String, DTInputParameter> result = newInputParameters()

		// Load properties from previous runs:
		Properties props = newPropertiesFromFile()

		// Apply property values from previous runs to values of defined input parameters:
		applyPropertiesToInputParameters(result, props)

		// Now ask user for each input value:
		DTUtil.askForInputParameters(result*.value)
		result << aInputParameters

		// Store possibly modified parameters back as property file:
		applyParametersToProperties(result, props)
		storeMetaInfoFile(props)
		

		result.putAll(newDerivedInputParameters(result))
		result
	}

	private static storeMetaInfoFile(Properties props) {
		File propFile = new File(getMetaInfoFilename())
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

	private static Properties newPropertiesFromFile() {
		File propFile = new File(getMetaInfoFilename())
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

	abstract String getZipName()
	abstract Map<String, String> newFilenameBinding(Map<String, DTInputParameter> aInputParameters)
	abstract Map<String, String> newTextBinding(Map<String, DTInputParameter> aInputParameters)

	// find file extensions in folder (http://lookherefirst.wordpress.com/2008/10/01/how-to-list-all-file-extensions-within-a-directory/)):
	//   find build -type f| sed -e "s/.*\./\./"|sort|uniq -c|grep -e '\..*'|sort -rn
	List<String> getExclusions() { [] }

}
