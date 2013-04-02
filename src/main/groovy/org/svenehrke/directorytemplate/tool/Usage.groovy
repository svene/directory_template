package org.svenehrke.directorytemplate.tool

class Usage {

	def show() {
		println """
	usage:

	  install template component from github repository:
	    gdt.sh install -github <user> <repo>>
	    example: gdt.sh install -github svene dt_java

	  install template component from local folder:
	    gdt.sh install -folder <path to>/<template component>
	    example: gdt.sh install -folder ~/template_components/dt_java

	  list installed templates:
	    gdt.sh list

	  apply a template:
	    gdt.sh <directorytemplate>
	    example: gdt.sh simplejava

	  update a template:
	    gdt.sh update <template component>
	    example: gdt.sh update dt_java

	"""
	}
}
