package org.svenehrke.directorytemplate

import groovy.util.logging.Slf4j
/**
 * Capable to create a folder from a template folder
 */
@Slf4j
class TemplateFolderToFolderCreator {

	String gdtHome
	String targetDir
	String componentName
	String templateName
	def fileExclusionFilter

	void createTargetFolder(Map<String, String> inFilenameBinding, final Map<String, String> inTextBinding) {

		DTMetaInfo mi = new DTMetaInfo(metaInfoFolderName: "$targetDir/${DTMetaInfo.META_INFO_FOLDERNAME}", templateName: templateName)
		new DTMetaInfoFolder(metaInformation: mi).createMetaInfoFolder()


		String templateSourceDirectoryName = "$gdtHome/$componentName/templates/$templateName"

		// Unpack template with bindings applied:
		new TemplateUnpacker(targetFolderName: mi.templateFolderInMetaFolder(), filenameBinding: inFilenameBinding).createFolderFrom(templateSourceDirectoryName)

		// Apply textBinding on extracted files:
		String templatedirectory = "${mi.templateFolderInMetaFolder()}/templatedirectory"
		DirectoryTemplateResolver.applyTextBindingToFolder(templatedirectory, fileExclusionFilter, inTextBinding)

		// Move folders from temporary directory to current folder:
		new File(templatedirectory).eachFile { File f ->
			f.renameTo("${targetDir}/$f.name")
		}

		// cleanup:
		new File(mi.templateFolderInMetaFolder()).deleteDir()
	}
}
