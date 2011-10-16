package org.svenehrke.directorytemplate

abstract class BaseDTDefinition {
	Map<String, DTInputParameter> inputParameters

	void createTargetFolder() {
		// Collect input parameters:
		askForInputParameters()

		// Iterate over zip-entries and create real folder layout with resolved variables from them:
		DirectoryTemplateResolver.createFolderFromZipResource(getClass().classLoader, templateName, filenameBinding)

		// Apply textBinding on extracted files:
		def rootDir = DirectoryTemplateResolver.applyBindings('@ROOT_FOLDER@', filenameBinding)
		DirectoryTemplateResolver.applyTextBindingToExpandedZip(rootDir, exclusions, textBinding)
	}

	void askForInputParameters() {
		Map<String, DTInputParameter> result = newInputParameters()
		DTUtil.askForInputParameters(result*.value)
		addDerivedInputParameters(result)
		inputParameters = result
	}

	abstract Map<String, DTInputParameter> newInputParameters()
	void addDerivedInputParameters(Map<String, DTInputParameter> aInputParameters) {

	}

	abstract String getTemplateName()
	abstract Map<String, String> getFilenameBinding()
	abstract Map<String, String> getTextBinding()

	// find file extensions in folder (http://lookherefirst.wordpress.com/2008/10/01/how-to-list-all-file-extensions-within-a-directory/)):
	//   find build -type f| sed -e "s/.*\./\./"|sort|uniq -c|grep -e '\..*'|sort -rn
	List<String> getExclusions() { [] }

}
