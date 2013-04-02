package org.svenehrke.directorytemplate.tool

import org.svenehrke.directorytemplate.ConsoleInputParameterProvider
import org.svenehrke.directorytemplate.DTInputParameterStorage
import org.svenehrke.directorytemplate.DTMetaInfo
import org.svenehrke.directorytemplate.DevelopmentInputParameterProvider
import org.svenehrke.directorytemplate.TemplateFolderToFolderCreator

class ApplyCommand {

	String gdtHome
	String[] args

	boolean run() {
		if (args.size() != 1) {
			new Usage().show()
			return false
		}
		String templateName = args[0]
		Map<String, File> at = new GdtInfo(gdtHome: gdtHome).availableTemplates()
		File templateSourceDirectory = at[templateName]
		if (!templateSourceDirectory?.exists()) {
			println("template '${templateName}' not found.")
			return false
		}
		String componentName = templateSourceDirectory.parentFile.parentFile.getName()

		String targetDir = '.'

		// Collect input parameters:
		DTMetaInfo mi = new DTMetaInfo(metaInfoFolderName: "$targetDir/${DTMetaInfo.META_INFO_FOLDERNAME}" , templateName: templateName)


		def cfg = new ConfigSlurper().parse(new File("$gdtHome/${componentName}/templates/${templateName}/.config/dt_config.groovy").toURL()).config
		Collection inputParameters = cfg.parameters
		new DTInputParameterStorage().loadParameters(mi, inputParameters)

		// Now ask user for each input value:
		def inputParameterProvider = System.properties['development'] ? new DevelopmentInputParameterProvider() : new ConsoleInputParameterProvider()
		inputParameterProvider.askForInputParameters(inputParameters)
		new DTInputParameterStorage().storeParameters(mi, inputParameters)

		def transformer = cfg.transformer
		def inputParameters2 = transformer.call(inputParameters)

		Collection fileNameParameters = inputParameters2.findAll { param -> param.name.startsWith('@') }
		Collection textParameters = inputParameters2.findAll { param -> !param.name.startsWith('@') }
		Map fileNameBinding = fileNameParameters.collectEntries([:]) {param -> [param.name, param.value] }
		Map textBinding = textParameters.collectEntries([:]) {param -> [param.name, param.value] }


		def fileExclusionFilter = cfg.textBinding?.fileExclusionFilter

		new TemplateFolderToFolderCreator(
			gdtHome: gdtHome,
			targetDir: targetDir,
			componentName: componentName,
			templateName: templateName,
			fileExclusionFilter: fileExclusionFilter,
		).createTargetFolder(fileNameBinding, textBinding)

		true
	}

}
