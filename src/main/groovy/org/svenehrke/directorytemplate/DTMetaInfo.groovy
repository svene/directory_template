package org.svenehrke.directorytemplate

class DTMetaInfo {
	public final static String META_INFO_FOLDERNAME = '.directory_template'

	String metaInfoFolderName
	String templateName


	String templateFolderInMetaFolder() {
		"${metaInfoFolderName}/${templateName}"
	}

	String inputParametersFilename() {
		"${metaInfoFolderName}/inputParameters.properties"
	}
}
