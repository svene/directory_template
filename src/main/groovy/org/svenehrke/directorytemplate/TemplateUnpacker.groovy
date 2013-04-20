package org.svenehrke.directorytemplate
import groovy.io.FileType
import groovy.util.logging.Slf4j
import org.apache.commons.io.FileUtils

@Slf4j
class TemplateUnpacker {
	String targetFolderName
	Map<String, String> filenameBinding

	void createFolderFrom(String inTemplateSourceDirectoryName) {

		// Conversion to support windows backslash (since 'templateSourceDirectoryName' is used in '-' operation further down)
		def templateSourceDirectoryName = new File(inTemplateSourceDirectoryName).absolutePath

		def bindingResolver = new BindingResolver(filenameBinding)

		// Create new folders with new names:
		new File(templateSourceDirectoryName).eachDirRecurse {dir ->
			String path = dir.absolutePath - (templateSourceDirectoryName + File.separator)

			def newName = bindingResolver.apply(path)
			String fn = "$targetFolderName/$newName"
			new File(fn).mkdirs()
		}

		// Create files with new names:
		new File(templateSourceDirectoryName).eachFileRecurse FileType.FILES, {file ->
			String path = file.absolutePath - (templateSourceDirectoryName + File.separator)

			def newName = bindingResolver.apply(path)
			String fn = "$targetFolderName" + File.separator + "$newName"

			FileUtils.copyFile(file, new File(fn))
		}
	}
}
