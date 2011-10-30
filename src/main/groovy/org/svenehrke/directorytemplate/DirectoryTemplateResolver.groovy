package org.svenehrke.directorytemplate

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import groovy.util.logging.Log

@Log
class DirectoryTemplateResolver {

	static final Logger logger = LoggerFactory.getLogger(DirectoryTemplateResolver.class)

	/** aValue with substrings appearing in aBinding's keys got replaced with their associated values.
	 * See Test for examples
	 */
	static String applyBindings(String aValue, Map aBinding) {
		String result = aValue
		aBinding?.each {key, value ->
			if (aValue.contains(key)) {
				result = result.replaceAll(key, value)
				logger?.debug ":\n** key=$key, value=$value: ${aValue} --> ${result}"
			}
		}
		result
	}

	static void createFolderFromZipResource(ClassLoader aClassLoader, String aZipName, String aTargetFolderName, Map<String, String> aBinding) {
		log.info "aZipName: $aZipName"
		ZipInputStream zis = new ZipInputStream(aClassLoader.getResourceAsStream(aZipName))
		ZipEntry ze
		while ((ze = zis.getNextEntry()) != null) {

			def newName = DirectoryTemplateResolver.applyBindings(ze.name, aBinding)
			String fn = "$aTargetFolderName/$newName"
			if (ze.directory) {
				log.info "folder: $ze.name, new: $fn"
				new File(fn).mkdirs()
			}
			else {
				// see http://snipplr.com/view/1745/extract-zipentry-into-a-bytearrayoutputstream/
				log.info "unzipping file $fn"
				OutputStream output = new File(fn).newDataOutputStream()
				int data = 0;
				while( ( data = zis.read() ) != - 1 )
				{
					output.write( data );
				}
				// The ZipEntry is extracted in the output
				output.close();
			}
		}
		zis.close()
	}

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
