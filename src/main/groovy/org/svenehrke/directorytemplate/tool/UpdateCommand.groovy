package org.svenehrke.directorytemplate.tool
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.PullCommand
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder

class UpdateCommand {

	String gdtHome
	String[] args

	boolean run() {
		if (args.size() != 2) {
			new Usage().show()
			return false
		}

		String templateName = args[1]

		String localRepoLocation = "$gdtHome/$templateName"

		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = builder.setWorkTree(new File(localRepoLocation)).build()
		Git git = new Git(repository)
		PullCommand pull = git.pull()
		pull.call();

		println "template '$templateName' updated to latest version"

		true
	}
}
