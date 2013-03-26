package org.svenehrke.directorytemplate

import java.util.zip.ZipInputStream

/**
 * Capable to create a folder from a zipped template folder
 */
class ZippedTemplateToFolderCreator {

	/** Create target folder from template folder (template method) */
	void createTargetFolder(Map<String, String> inFilenameBinding, Map<String, String> inTextBinding,
		String inTemplateFolderName, String inInputParametersFilename, String inMetaInfoFolderName,
		String inZipFilename, List<String> inExclusions) {

		createMetaInfoFolder(inInputParametersFilename, inMetaInfoFolderName)

		// Iterate over zip-entries and create real folder layout with resolved variables from them:
		def zipInputStream = new ZipInputStream(getClass().classLoader.getResourceAsStream(inZipFilename))
		new TemplateUnpacker(inMetaInfoFolderName, inFilenameBinding).createFolderFromZipInputStream(zipInputStream)

		// Apply textBinding on extracted files:
		DirectoryTemplateResolver.applyTextBindingToExpandedZip(inTemplateFolderName, inExclusions, inTextBinding)

		// Move folders from temporary directory to current folder:
		new File(inTemplateFolderName).eachFile { File f ->
			f.renameTo("./$f.name")
		}
	}

	private static void createMetaInfoFolder(String inInputParametersFilename, String inMetaInfoFolderName) {
		new File(inMetaInfoFolderName).mkdirs()
		File f = new File("${inInputParametersFilename}")
		if (!f.exists()) {
			f.createNewFile()
		}
	}
}
