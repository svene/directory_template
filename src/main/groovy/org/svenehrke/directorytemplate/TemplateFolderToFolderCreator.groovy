package org.svenehrke.directorytemplate

import java.util.zip.ZipInputStream

/**
 * Capable to create a folder from a template folder
 */
class TemplateFolderToFolderCreator {

	String workingDir
	String templateDirectoryName
	String templateName
	String unpackFolderName

	void createFolderFromTemplateFolder(Map<String, String> inFilenameBinding, final Map<String, String> inTextBinding) {
		String zipFileName = "${workingDir}/${templateName}.zip"

		// zip template directory (just to be able to reuse 'TemplateUnpacker''s filename binding capabilities):
		new AntBuilder().zip(basedir: "$templateDirectoryName/..", destfile: zipFileName, includes: "${templateName}/**")
		def zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFileName)))

		// unzip again with bindings applied:
		new TemplateUnpacker(unpackFolderName, inFilenameBinding).createFolderFromZipInputStream(zipInputStream)

		// Apply textBinding on extracted files:
		DirectoryTemplateResolver.applyTextBindingToExpandedZip(unpackFolderName, [], inTextBinding)

		// Move folders from temporary directory to current folder:
		new File("${unpackFolderName}/${templateName}").eachFile { File f ->
			f.renameTo("${workingDir}/$f.name")
		}
		new File(unpackFolderName).deleteDir()
	}
}
