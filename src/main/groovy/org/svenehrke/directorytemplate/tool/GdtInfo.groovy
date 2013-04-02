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
		new File(gdtHome).eachDir { f ->
			if (!(['bin']).contains(f.name)) {
				new File("${gdtHome}/${f.name}/templates").eachDir { dt ->
					result[dt.name] = dt
				}
			}
		}
		result
	}

}
