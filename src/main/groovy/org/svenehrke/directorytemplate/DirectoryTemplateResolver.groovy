package org.svenehrke.directorytemplate

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

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

	static void createFolderFromZipResource(String aRootFolderName, ClassLoader aClassLoader, String aTemplateName, Map<String, String> aBinding) {
		ZipInputStream zis = new ZipInputStream(aClassLoader.getResourceAsStream(aTemplateName))
		ZipEntry ze
		while ((ze = zis.getNextEntry()) != null) {

			def newName = DirectoryTemplateResolver.applyBindings(ze.name, aBinding)
			String fn = "${aRootFolderName}/$newName"
			if (ze.directory) {
				new File(fn).mkdirs()
			}
			else {
				// see http://snipplr.com/view/1745/extract-zipentry-into-a-bytearrayoutputstream/
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

	static void applyTextBindingToExpandedZip(String aRootFolderName, List<String> aExclusions, Map<String, String> aTextBinding) {
		if (aTextBinding) {
			new File("$aRootFolderName/DT").eachFileRecurse { file ->
				if (!file.directory) {
						if (aExclusions.any { x -> file.name.endsWith(x) }) {
							//println "excluded: $file.name"
						}
						else {
//					println "massaging $file.path"
							try {
								String st = file.text
								aTextBinding.entrySet().each { entry ->
									String k = entry.key
									String v = entry.value
//							println "*** $k=$v"
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
