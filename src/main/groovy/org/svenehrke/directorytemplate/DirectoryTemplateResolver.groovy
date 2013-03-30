package org.svenehrke.directorytemplate

import groovy.util.logging.Log

@Log
class DirectoryTemplateResolver {

	static void applyTextBindingToFolder(String aRootDir, fileExclusionFilter, Map<String, String> aTextBinding) {
		if (aTextBinding) {
			new File(aRootDir).eachFileRecurse { file ->
				if (!file.directory) {
						println "handling file ${file.name}"
						if (fileExclusionFilter?.call(file)) {
							log.info"excluded: '$file.name' from text binding processing"
						}
						else {
							log.info "massaging $file.path"
							try {
								String st = file.text
								aTextBinding.entrySet().each { entry ->
									String k = entry.key
									String v = entry.value
									log.info "*** $k=$v"
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
