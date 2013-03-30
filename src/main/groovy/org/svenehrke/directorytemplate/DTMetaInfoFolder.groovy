package org.svenehrke.directorytemplate

import groovy.util.logging.Log

@Log
class DTMetaInfoFolder {

	DTMetaInfo metaInformation

	def createMetaInfoFolder() {
		new File(metaInformation.metaInfoFolderName).mkdirs()
		File f = new File(metaInformation.inputParametersFilename())
		if (!f.exists()) {
			f.createNewFile()
		}
	}

	def storeProperties(Properties props) {
		File propFile = new File(metaInformation.inputParametersFilename())
		log.info 'Store properties to file'
		propFile.withWriter {
			props.store(it, '')
		}
	}

	def Properties newPropertiesFromFile() {
		File propFile = new File(metaInformation.inputParametersFilename())
		Properties props = new Properties()
		propFile.withReader {
			props.load(it)
		}
		props
	}
}
