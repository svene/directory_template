package org.svenehrke.dt

import org.svenehrke.directorytemplate.BaseDirectoryTemplateBuilder
import org.svenehrke.dt.java.DirectoryTemplateDTBuilder
import org.svenehrke.dt.java.GradleDTBuilder
import org.svenehrke.dt.java.SimpleJavaDTBuilder
import org.svenehrke.dt.java.SimpleSwingDTBuilder

class Invoker {
	Map<String, BaseDirectoryTemplateBuilder> templates = [
		'simplejava' : new SimpleJavaDTBuilder()
		, 'simpleswing' : new SimpleSwingDTBuilder()
		, 'gradle' : new GradleDTBuilder()
		, 'directorytemplate' : new DirectoryTemplateDTBuilder()
	]

	def run(String[] args) {
		if (!args) {
			printUsage(templates)
		}
		String key = args[0]
		if (!templates.keySet().contains(key)) {
			printUsage(templates)
		}

		BaseDirectoryTemplateBuilder builder = templates[key]

		// Collect input parameters:
		def inputParameters = builder.askForInputParameters([:])
		Map<String, String> filenameBinding = builder.newFilenameBinding(inputParameters)
		Map<String, String> textBinding = builder.newTextBinding(inputParameters)
		String templateFolderName = builder.templateFolderName()
		builder.createTargetFolder(filenameBinding, textBinding, templateFolderName)

	}

	def printUsage(Map aTemplates) {
		println 'Usage: groovy jdt.groovy ' + aTemplates.keySet().join(' | ')
		System.exit(1)
	}

}
