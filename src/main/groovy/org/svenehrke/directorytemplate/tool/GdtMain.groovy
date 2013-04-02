package org.svenehrke.directorytemplate.tool

class GdtMain {
	String userHome = System.properties['user.home']
	String gdtHome = "${userHome}/.gdt"

	def run(String[] args) {
		if (args.size() < 1) {
			new Usage().show()
			System.exit(1)
		}

		def command = args[0]
		def cmd = null

		if (command == 'install') {
			cmd = new InstallCommand(args: args, gdtHome: gdtHome)
		}
		else if (command == 'list') {
			cmd = new ListCommand(gdtHome: gdtHome)
		}
		else if (command == 'update') {
			cmd = new UpdateCommand(gdtHome: gdtHome, args: args)
		}
		else { // assume command is 'apply'
			cmd = new ApplyCommand(gdtHome: gdtHome, args: args)
		}

		if (cmd) {
			boolean success = cmd.run()
			if (!success) {
				System.exit(1)
			}

		}

	}

}
