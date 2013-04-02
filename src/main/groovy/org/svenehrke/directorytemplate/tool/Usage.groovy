package org.svenehrke.directorytemplate.tool

class Usage {

	def show() {
		println """
	usage:
	  groovy start.groovy install -github <user> <repo>>
	  groovy start.groovy install -folder <path to>/<template component>
	  groovy start.groovy list
	  groovy start.groovy <directorytemplate>

	examples:
	  groovy start.groovy install -github svene dt_java
	  groovy start.groovy install -folder ~/template_components/dt_java
	  groovy start.groovy simplejava
	"""
	}
}
