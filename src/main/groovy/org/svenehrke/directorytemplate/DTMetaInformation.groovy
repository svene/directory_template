package org.svenehrke.directorytemplate

class DTMetaInformation {
	String metaInfoFolderName
	String templateName


	String templateFolderInMetaFolder() {
		"${metaInfoFolderName}/${templateName}"
	}

	String inputParametersFilename() {
		"${metaInfoFolderName}/${templateName}"
	}
}
