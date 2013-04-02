package org.svenehrke.directorytemplate.tool

class GdtInfo {

	String gdtHome

	/**
	 * Map of available template directories by template name
	 *
	 * TODO: handle name clash in case two template collections have the same name. E.g.:
	 *   dt_java/simplejava
	 *   dt_misc/simplejava
	 */
	Map<String, File> availableTemplates() {
		def result = [:]
		new File(gdtHome).eachDir { component ->
			if (!(['bin']).contains(component.name)) {
				new File("${gdtHome}/${component.name}/templates").eachDir { dt ->
					result[dt.name] = dt
				}
			}
		}
		result
	}

}
