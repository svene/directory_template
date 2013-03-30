package org.svenehrke.directorytemplate.tool

import org.apache.commons.io.FileUtils
import org.eclipse.jgit.api.CloneCommand
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
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

			def gitHubMode = args[1] == '-github'
			def folderMode = args[1] == '-folder'

			String ghUserName = 'svene'
			String ghRepoName = 'dt_java'
			String remoteUrl
			String localRepoLocation
			if (gitHubMode) {
				if (args.size() < 4) {
					printUsage()
					System.exit(1)
				}
				ghUserName = args[2]
				ghRepoName = args[3]
				remoteUrl = "https://github.com/${ghUserName}/${ghRepoName}.git"
				localRepoLocation = "${gdtHome}/${ghRepoName}"

				FileRepositoryBuilder builder = new FileRepositoryBuilder();
				Repository repository = builder.setWorkTree(new File(gdtHome)).build()
				Git git = new Git(repository);
				CloneCommand clone = git.cloneRepository();
				clone.setBare(false);
				clone.setCloneAllBranches(true);
				clone.setDirectory(new File(localRepoLocation)).setURI(remoteUrl);
				clone.call();

				println "Repository '${remoteUrl}' cloned into local folder '${localRepoLocation}'"
			}
			else if (folderMode) {
				if (args.size() != 3) {
					printUsage()
					System.exit(1)
				}
				String sourceFolderName = args[2]
				File sourceFolder = new File(sourceFolderName)
				if (!sourceFolder.exists()) {
					println "Folder '$sourceFolderName' not found"
					System.exit(1)
				}
				if (!new File("$sourceFolderName/templates").exists()) {
					println "Source folder '$sourceFolderName' is not a valid template component (does not contain a subfolder named 'templates')"
					System.exit(1)
				}
				String componentName = sourceFolder.getName()

				String targetFolderName = "$gdtHome/$componentName"
				File targetFolder = new File(targetFolderName)
				if (targetFolder.exists()) {
					println "Target folder '$targetFolderName' already exists. Please remove it first."
					System.exit(1)
				}

				FileUtils.copyDirectory(sourceFolder, targetFolder)
				println "template component '$componentName' successfully installed"
			}
			else {
				printUsage()
				System.exit(0)
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
		else if (command == 'apply') {
			if (args.size() < 2) {
				printUsage()
				System.exit(1)
			}
			// todo: dynamically read in installed directory templates. Until then hard coded here:
			String templateName = args[1]
			File templateSourceDirectory = availableTemplates()[templateName]
			if (!templateSourceDirectory.exists()) {
				println("template folder '${templateSourceDirectory.absolutePath}' not found.")
				System.exit(1)
			}
			String componentName = templateSourceDirectory.parentFile.parentFile.getName()

			String targetDir = '.'

			// Collect input parameters:
			DTMetaInfo mi = new DTMetaInfo(metaInfoFolderName: "$targetDir/${DTConstants.META_INFO_FOLDERNAME}" , templateName: templateName)


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


			new TemplateFolderToFolderCreator(
				gdtHome: gdtHome,
				targetDir: targetDir,
				componentName: componentName,
				templateName: templateName,
			).createTargetFolder(fileNameBinding, textBinding)
		}
		else {
			println "unknown command '${command}'"
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
			new File("${gdtHome}/${f.name}/templates").eachDir { dt ->
				result[dt.name] = dt
			}
		}
		result
	}

	def printUsage() {
		println """
	usage:
	  groovy start.groovy install -github <user> <repo>>
	  groovy start.groovy install -folder <path to>/<template component>
	  groovy start.groovy list
	  groovy start.groovy apply <directorytemplate>

	examples:
	  groovy start.groovy install -github svene dt_java
	  groovy start.groovy install -folder ~/template_components/dt_java
	  groovy start.groovy apply simplejava
	"""
	}
}
