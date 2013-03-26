package org.svenehrke.directorytemplate.tool

import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.CloneCommand
import org.svenehrke.directorytemplate.DirectoryTemplateResolver

class GdtMain {
	String userHome = System.properties['user.home']
	String gdtHome = System.properties['user.home'] + '/.gdt'
	String templateDirectory = 'templatedirectory'

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
			new File(gdtHome).eachDir { f ->
				println "${f.name}:"
				new File("${gdtHome}/${f.name}/templatedirectory").eachDir { dt ->
					println "  ${dt.name}"
				}
			}
		}
		else if (command == 'apply') {
			if (args.size() < 2) {
				printUsage()
				System.exit(1)
			}
			// todo: dynamically read in installed directory templates. Until then hard coded here:
			String dtName = args[1]
			DirectoryTemplateResolver.createFolderFromTemplateFolder()
		}
		else {
			println "unknown command '${command}'"
		}
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
