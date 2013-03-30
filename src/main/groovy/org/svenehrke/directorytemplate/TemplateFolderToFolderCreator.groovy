package org.svenehrke.directorytemplate

import groovy.util.logging.Log

import java.util.zip.ZipInputStream

/**
 * Capable to create a folder from a template folder
 */
@Log
class TemplateFolderToFolderCreator {

	String gdtHome
	String targetDir
	String componentName
	String templateName

	void createTargetFolder(Map<String, String> inFilenameBinding, final Map<String, String> inTextBinding) {

		DTMetaInfo mi = new DTMetaInfo(metaInfoFolderName: "$targetDir/${DTConstants.META_INFO_FOLDERNAME}", templateName: templateName)
		new DTMetaInfoFolder(metaInformation: mi).createMetaInfoFolder()


		String zipFileName = "${mi.metaInfoFolderName}/${templateName}.zip"

		// zip template directory (just to be able to reuse 'TemplateUnpacker''s filename binding capabilities):
		String templateSourceDirectoryName = "$gdtHome/$componentName/templates/$templateName"
		new AntBuilder().zip(basedir: "$templateSourceDirectoryName/..", destfile: zipFileName, includes: "${templateName}/**")
		def zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFileName)))

		// unzip again with bindings applied:
		new TemplateUnpacker(targetFolderName: mi.metaInfoFolderName, filenameBinding: inFilenameBinding).createFolderFromZipInputStream(zipInputStream)

		// Apply textBinding on extracted files:
		String templatedirectory = "${mi.templateFolderInMetaFolder()}/templatedirectory"
		DirectoryTemplateResolver.applyTextBindingToExpandedZip(templatedirectory, [], inTextBinding)

		// Move folders from temporary directory to current folder:
		new File(templatedirectory).eachFile { File f ->
			f.renameTo("${targetDir}/$f.name")
		}

		// cleanup:
		new File(mi.templateFolderInMetaFolder()).deleteDir()
		new File(zipFileName).delete()
	}
}
