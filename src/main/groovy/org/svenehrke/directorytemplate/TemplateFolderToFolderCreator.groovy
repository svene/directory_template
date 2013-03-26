package org.svenehrke.directorytemplate

import java.util.zip.ZipInputStream

/**
 * Capable to create a folder from a template folder
 */
class TemplateFolderToFolderCreator {

	void createFolderFromTemplateFolder(Map<String, String> inFilenameBinding, final Map<String, String> inTextBinding) {
		String dtName = 'simplejava'

		String workingDir = '/home/sven/tmp/gdt'
		String aTemplateDirectoryFolderName = '/home/sven/.gdt/dt_java/templatedirectory'
		String aSourceFolderName = '/home/sven/.gdt/dt_java/templatedirectory/simplejava'
		String zipFileName = "${workingDir}/${dtName}.zip"

		// zip template directory:

		new AntBuilder()
		new AntBuilder().zip(basedir: aTemplateDirectoryFolderName, destfile: zipFileName, includes: "${dtName}/**")
		def zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFileName)))

		// unzip again with bindings applied:
		String aTargetFolderName = "${workingDir}/ttt"
		new TemplateUnpacker(aTargetFolderName, inFilenameBinding).createFolderFromZipInputStream(zipInputStream)

		// Apply textBinding on extracted files:
		DirectoryTemplateResolver.applyTextBindingToExpandedZip(aTargetFolderName, [], inTextBinding)

		// Move folders from temporary directory to current folder:
		new File("$aTargetFolderName/$dtName").eachFile { File f ->
			f.renameTo("$workingDir/$f.name")
		}
	}
}
