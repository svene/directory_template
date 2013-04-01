package org.svenehrke.directorytemplate
import groovy.io.FileType
import groovy.util.logging.Slf4j
import org.apache.commons.io.FileUtils

@Slf4j
class TemplateUnpacker {
	String targetFolderName
	Map<String, String> filenameBinding

	void createFolderFrom(String inTemplateSourceDirectoryName) {

		def bindingResolver = new BindingResolver(filenameBinding)

		// Create new folders with new names:
		new File(inTemplateSourceDirectoryName).eachDirRecurse {dir ->
			String path = dir.absolutePath - (inTemplateSourceDirectoryName + '/')

			def newName = bindingResolver.apply(path)
			String fn = "$targetFolderName/$newName"
			new File(fn).mkdirs()
		}

		// Create files with new names:
		new File(inTemplateSourceDirectoryName).eachFileRecurse FileType.FILES, {file ->
			String path = file.absolutePath - (inTemplateSourceDirectoryName + '/')

			def newName = bindingResolver.apply(path)
			String fn = "$targetFolderName/$newName"

			FileUtils.copyFile(file, new File(fn))
		}
	}
}
