package org.svenehrke.directorytemplate.tool

import org.apache.commons.io.FileUtils
import org.eclipse.jgit.api.CloneCommand
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder

class InstallCommand {

	String gdtHome
	String[] args
	Usage usage

	boolean run() {
		String ghUserName = 'svene'
		String ghRepoName = 'dt_java'
		String remoteUrl
		String localRepoLocation

		def gitHubMode = args[1] == '-github'
		def folderMode = args[1] == '-folder'

		InstallMode installMode = gitHubMode ? InstallMode.GITHUB : folderMode ? InstallMode.FOLDER : InstallMode.UNKNOWN
		if (installMode.is(InstallMode.UNKNOWN)) {
			usage.show()
			return false
		}

		if (installMode.is(InstallMode.GITHUB)) {
			if (args.size() < 4) {
				usage.show()
				return false
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
		else if (installMode.is(InstallMode.FOLDER)) {
			if (args.size() != 3) {
				usage.show()
				return false
			}
			String sourceFolderName = args[2]
			File sourceFolder = new File(sourceFolderName)
			if (!sourceFolder.exists()) {
				println "Folder '$sourceFolderName' not found"
				return false
			}
			if (!new File("$sourceFolderName/templates").exists()) {
				println "Source folder '$sourceFolderName' is not a valid template component (does not contain a subfolder named 'templates')"
				return false
			}
			String componentName = sourceFolder.getName()

			String targetFolderName = "$gdtHome/$componentName"
			File targetFolder = new File(targetFolderName)
			if (targetFolder.exists()) {
				println "Target folder '$targetFolderName' already exists. Please remove it first."
				return false
			}

			FileUtils.copyDirectory(sourceFolder, targetFolder)
			println "template component '$componentName' successfully installed"
		}
		else {
			throw IllegalStateException("unknown installmode: $installMode")
		}

	}
}
