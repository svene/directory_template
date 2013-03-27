package org.svenehrke.directorytemplate.tool
import org.eclipse.jgit.api.CloneCommand
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.svenehrke.directorytemplate.DTUtil
import org.svenehrke.directorytemplate.TemplateFolderToFolderCreator

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
			}
			else {
				println 'Non github mode not supported yet'
				System.exit(0)
			}

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
		else if (command == 'list') {
			Map<String, File> at = availableTemplates()
			at.each() {k, v ->
				println "$k ($v)"
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
			def fileNameBinding = ['@packagename@': DTUtil.dotsToSlashes('org.svenehrke')]
			def textBinding = ['packagename': 'org.svenehrke']

			new TemplateFolderToFolderCreator(
				targetDir: '.'
				, templateSourceDirectoryName: templateSourceDirectory.absolutePath
				, templateName: templateName
			).createTargetFolder(fileNameBinding, textBinding)
		}
		else {
			println "unknown command '${command}'"
		}
	}

	/**
	 * Map of available template directories by template name
	 *
	 * TODO: handle name class in case two template collections have the same name. E.g.:
	 *   dt_java/simplejava
	 *   dt_misc/simplejava
	 */
	Map<String, File> availableTemplates() {
		def result = [:]
		new File(gdtHome).eachDir { f ->
			new File("${gdtHome}/${f.name}/templatedirectory").eachDir { dt ->
				result[dt.name] = dt
			}
		}
		result
	}

	def printUsage() {
		println """
	usage:
	  groovy gdt2.groovy install -github <user> <repo> <templatedirectory (default='templatedirectory')>
	  //groovy gdt2.groovy install <repourl> <templatedirectory (default='templatedirectory')>
	  groovy gdt2.groovy list
	  groovy gdt2.groovy apply <directorytemplate>

	examples:
	  groovy gdt2.groovy install -github svene dt_java
	  //groovy gdt2.groovy https://github.com/svene/dt_java.git
	  groovy gdt2.groovy apply simplejava
	"""
	}
}
