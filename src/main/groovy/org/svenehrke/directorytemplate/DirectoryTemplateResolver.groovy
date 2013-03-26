package org.svenehrke.directorytemplate
import groovy.util.logging.Log
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.util.zip.ZipInputStream

@Log
class DirectoryTemplateResolver {

	static final Logger logger = LoggerFactory.getLogger(DirectoryTemplateResolver.class)

	/** aValue with substrings appearing in aBinding's keys got replaced with their associated values.
	 * See Test for examples
	 */

	static void createFolderFromZipResource(ClassLoader aClassLoader, String aZipName, String aTargetFolderName, Map<String, String> aBinding) {
		log.info "aZipName: $aZipName"
		def zipInputStream = new ZipInputStream(aClassLoader.getResourceAsStream(aZipName))
		new TemplateUnpacker(aTargetFolderName, aBinding).createFolderFromZipInputStream(zipInputStream)
	}


	static void createFolderFromTemplateFolder() {
		String dtName = 'simplejava'

		String workingDir = '/home/sven/tmp/gdt'
		String aTemplateDirectoryFolderName = '/home/sven/.gdt/dt_java/templatedirectory'
		String aSourceFolderName = '/home/sven/.gdt/dt_java/templatedirectory/simplejava'
		String zipFileName = "${workingDir}/${dtName}.zip"

		// zip template directory:
		new AntBuilder().zip(basedir: aTemplateDirectoryFolderName, destfile: zipFileName, includes: "${dtName}/**")
		def zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFileName)))

		// unzip again with bindings applied:
		String aTargetFolderName = "${workingDir}/ttt"
		Map<String, String> aFilenameBinding = ['@packagename@': DTUtil.dotsToSlashes('org.svenehrke')]
		Map<String, String> aTextBinding = ['packagename': 'org.svenehrke']

		new TemplateUnpacker(aTargetFolderName, aFilenameBinding).createFolderFromZipInputStream(zipInputStream)

		// Apply textBinding on extracted files:
		DirectoryTemplateResolver.applyTextBindingToExpandedZip(aTargetFolderName, [], aTextBinding)

		// Move folders from temporary directory to current folder:
		new File("$aTargetFolderName/$dtName").eachFile { File f ->
			f.renameTo("$workingDir/$f.name")
		}
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
