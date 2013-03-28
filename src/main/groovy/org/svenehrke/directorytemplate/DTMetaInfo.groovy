package org.svenehrke.directorytemplate

class DTMetaInfo {
	String metaInfoFolderName
	String templateName


	String templateFolderInMetaFolder() {
		"${metaInfoFolderName}/${templateName}"
	}

	String inputParametersFilename() {
		"${metaInfoFolderName}/inputParameters.properties"
	}
}
