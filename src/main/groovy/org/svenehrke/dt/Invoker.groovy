package org.svenehrke.dt
import org.svenehrke.directorytemplate.BaseDirectoryTemplateBuilder
import org.svenehrke.directorytemplate.DTConstants
import org.svenehrke.directorytemplate.DTMetaInformation
import org.svenehrke.directorytemplate.ZippedTemplateToFolderCreator
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
		String templateName = args[0]
		if (!templates.keySet().contains(templateName)) {
			printUsage(templates)
		}

		BaseDirectoryTemplateBuilder builder = templates[templateName]

		// Collect input parameters:
		def mi = new DTMetaInformation(metaInfoFolderName: DTConstants.META_INFO_FOLDERNAME, templateName: templateName)
		def inputParameters = builder.askForInputParameters(mi)

		// Create folder from template:
		new ZippedTemplateToFolderCreator(metaInformation: mi).createTargetFolder(
			builder.newFilenameBinding(inputParameters)
			,builder.newTextBinding(inputParameters)
			,builder.getExclusions()
		)

	}

	def printUsage(Map aTemplates) {
		println 'Usage: groovy jdt.groovy ' + aTemplates.keySet().join(' | ')
		System.exit(1)
	}

}
