package org.svenehrke.directorytemplate.tool

class ListCommand {

	String gdtHome

	boolean run() {
		Map<String, File> at = new GdtInfo(gdtHome: gdtHome).availableTemplates()
		if (at.isEmpty()) {
			println "no templates are installed"
		}
		else {
			int max = (at.keySet().max { it.length() }).length()
			at.each() {k, v ->
				println "${k.padLeft(max, ' ')}: ($v)"
			}
		}
		true
	}

}
