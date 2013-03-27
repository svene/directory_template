package org.svenehrke.directorytemplate

class DTMetaFolder {
	def createMetaInfoFolder(DTMetaInformation mi) {
		new File(mi.templateFolderInMetaFolder()).mkdirs()
		File f = new File(mi.inputParametersFilename())
		if (!f.exists()) {
			f.createNewFile()
		}
	}
}
