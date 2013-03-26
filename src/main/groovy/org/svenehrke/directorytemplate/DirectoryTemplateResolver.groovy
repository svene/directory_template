package org.svenehrke.directorytemplate
import groovy.util.logging.Log

@Log
class DirectoryTemplateResolver {

	static void applyTextBindingToExpandedZip(String aRootDir, List<String> aExclusions, Map<String, String> aTextBinding) {
		if (aTextBinding) {
			new File(aRootDir).eachFileRecurse { file ->
				if (!file.directory) {
						if (aExclusions.any { x -> file.name.endsWith(x) }) {
							//println "excluded: $file.name"
						}
						else {
					println "massaging $file.path"
							try {
								String st = file.text
								aTextBinding.entrySet().each { entry ->
									String k = entry.key
									String v = entry.value
							println "*** $k=$v"
									String sOld = ''
									while (sOld != st) {
										sOld = st
										st = st.replace(('${' + k + '}'), v)
									}
								}
								file.text = st
							} catch (Exception e) {
								println file.text
								throw e
							}
						}
				}
			}
		}

	}

}
