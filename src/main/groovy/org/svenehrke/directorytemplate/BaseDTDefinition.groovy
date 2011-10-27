package org.svenehrke.directorytemplate

abstract class BaseDTDefinition {
	Map<String, DTInputParameter> inputParameters

	void createTargetFolder() {

		String rootDir = DTUtil.prompt('Rootdir', 'myproject')
		String dtCfgDir = "$rootDir/.meta/directory_template"
		new File(dtCfgDir).mkdirs()
		File f = new File("$dtCfgDir/inputParameters.groovy")
		f = new File("$dtCfgDir/inputParameters.properties")
		if (!f.exists()) {
			f.createNewFile()
		}

		// Collect input parameters:
		askForInputParameters(f)
		Map<String, String> filenameBinding = getFilenameBinding()
		rootDir = DirectoryTemplateResolver.applyBindings('@ROOT_FOLDER@', filenameBinding)
		println "rootdir: $rootDir"



		// Iterate over zip-entries and create real folder layout with resolved variables from them:
		DirectoryTemplateResolver.createFolderFromZipResource(getClass().classLoader, getZipName(), filenameBinding)

		// Apply textBinding on extracted files:
		DirectoryTemplateResolver.applyTextBindingToExpandedZip(rootDir, exclusions, textBinding)
	}

	void askForInputParameters(File propFile) {
		Map<String, DTInputParameter> result = newInputParameters()

		Properties props = new Properties()
		propFile.withReader {
			props.load(it)
		}

		result.each {ip ->
			String v = props.get(ip.key)
			if (v) {
				ip.value.value = v // Put property value on value of DTInputParameter
			}
		}

		DTUtil.askForInputParameters(result*.value)

		// Store properties to file:
		println 'Store properties to file:'
		result.each {ip ->
			println "-->setting property $ip.key to $ip.value.value"
			props.setProperty(ip.key, ip.value.value)
		}
		propFile.withWriter {
			props.store(it, '')
		}
		

		addDerivedInputParameters(result)
		inputParameters = result
	}

	abstract Map<String, DTInputParameter> newInputParameters()
	void addDerivedInputParameters(Map<String, DTInputParameter> aInputParameters) {

	}

	abstract String getZipName()
	abstract Map<String, String> getFilenameBinding()
	abstract Map<String, String> getTextBinding()

	// find file extensions in folder (http://lookherefirst.wordpress.com/2008/10/01/how-to-list-all-file-extensions-within-a-directory/)):
	//   find build -type f| sed -e "s/.*\./\./"|sort|uniq -c|grep -e '\..*'|sort -rn
	List<String> getExclusions() { [] }

}
