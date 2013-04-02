package org.svenehrke.directorytemplate.tool

import org.svenehrke.directorytemplate.*

class GdtMain {
	String userHome = System.properties['user.home']
	String gdtHome = "${userHome}/.gdt"

	def run(String[] args) {
		if (args.size() < 1) {
			printUsage()
			System.exit(1)
		}

		def command = args[0]

		if (command == 'install') {
			boolean success = new InstallCommand(
				args: args,
				gdtHome: gdtHome,
				usage: new Usage(),
			).run()
			if (!success) {
				System.exit(1)
			}

		}
		else if (command == 'list') {
			Map<String, File> at = availableTemplates()
			if (at.isEmpty()) {
				println "no templates are installed"
			}
			else {
				int max = (at.keySet().max { it.length() }).length()
				at.each() {k, v ->
					println "${k.padLeft(max, ' ')}: ($v)"
				}
			}
		}
		else { // assume command is 'apply'
			if (args.size() != 1) {
				printUsage()
				System.exit(1)
			}
			// todo: dynamically read in installed directory templates. Until then hard coded here:
			String templateName = args[0]
			File templateSourceDirectory = availableTemplates()[templateName]
			if (!templateSourceDirectory.exists()) {
				println("template folder '${templateSourceDirectory.absolutePath}' not found.")
				System.exit(1)
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
		}
	}

	/**
	 * Map of available template directories by template name
	 *
	 * TODO: handle name clash in case two template collections have the same name. E.g.:
	 *   dt_java/simplejava
	 *   dt_misc/simplejava
	 */
	Map<String, File> availableTemplates() {
		def result = [:]
		new File(gdtHome).eachDir { f ->
			if (!(['bin']).contains(f.name)) {
				new File("${gdtHome}/${f.name}/templates").eachDir { dt ->
					result[dt.name] = dt
				}
			}
		}
		result
	}

	def printUsage() {
		new Usage().show()
	}
}
