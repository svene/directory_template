package org.svenehrke.directorytemplate

import java.util.zip.ZipInputStream

/**
 * Capable to create a folder from a zipped template folder
 */
class ZippedTemplateToFolderCreator {

	DTMetaInformation metaInformation

	/** Create target folder from template folder (template method) */
	void createTargetFolder(Map<String, String> inFilenameBinding, Map<String, String> inTextBinding, List<String> inExclusions) {

		new DTMetaFolder().createMetaInfoFolder(metaInformation)

		// Iterate over zip-entries and create real folder layout with resolved variables from them:
		def zipInputStream = new ZipInputStream(getClass().classLoader.getResourceAsStream("dt_${templateName}.zip"))
		new TemplateUnpacker(metaInformation.templateFolderInMetaFolder(), inFilenameBinding).createFolderFromZipInputStream(zipInputStream)

		// Apply textBinding on extracted files:
		DirectoryTemplateResolver.applyTextBindingToExpandedZip(metaInformation.templateFolderInMetaFolder(), inExclusions, inTextBinding)

		// Move folders from temporary directory to current folder:
		new File(metaInformation.templateFolderInMetaFolder()).eachFile { File f ->
			f.renameTo("./$f.name")
		}
	}

}
