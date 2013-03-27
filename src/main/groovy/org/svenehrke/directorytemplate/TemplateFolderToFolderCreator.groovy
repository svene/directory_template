package org.svenehrke.directorytemplate

import java.util.zip.ZipInputStream

/**
 * Capable to create a folder from a template folder
 */
class TemplateFolderToFolderCreator {

	String targetDir
	String templateSourceDirectoryName
	String templateName

	void createTargetFolder(Map<String, String> inFilenameBinding, final Map<String, String> inTextBinding) {

		DTMetaInformation mi = new DTMetaInformation(metaInfoFolderName: "$targetDir/${DTConstants.META_INFO_FOLDERNAME}", templateName: templateName)
		new DTMetaFolder(metaInformation: mi).createMetaInfoFolder()


		String zipFileName = "${mi.metaInfoFolderName}/${templateName}.zip"

		// zip template directory (just to be able to reuse 'TemplateUnpacker''s filename binding capabilities):
		new AntBuilder().zip(basedir: "$templateSourceDirectoryName/..", destfile: zipFileName, includes: "${templateName}/**")
		def zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFileName)))

		// unzip again with bindings applied:
		new TemplateUnpacker(targetFolderName: mi.metaInfoFolderName, filenameBinding: inFilenameBinding).createFolderFromZipInputStream(zipInputStream)

		// Apply textBinding on extracted files:
		DirectoryTemplateResolver.applyTextBindingToExpandedZip(mi.templateFolderInMetaFolder(), [], inTextBinding)

		// Move folders from temporary directory to current folder:
		new File(mi.templateFolderInMetaFolder()).eachFile { File f ->
			f.renameTo("${targetDir}/$f.name")
		}

		// cleanup:
		new File(mi.templateFolderInMetaFolder()).deleteDir()
		new File(zipFileName).delete()
	}
}
