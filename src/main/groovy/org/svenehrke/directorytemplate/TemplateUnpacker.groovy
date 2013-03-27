package org.svenehrke.directorytemplate

import groovy.util.logging.Log

import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

@Log
class TemplateUnpacker {
	String targetFolderName
	Map<String, String> filenameBinding

/*
	TemplateUnpacker(String aTargetFolderName, Map<String, String> aFilenameBinding) {
		targetFolderName = aTargetFolderName
		filenameBinding = aFilenameBinding
	}
*/

	void createFolderFromZipInputStream(ZipInputStream inZipInputStream) {

		def bindingResolver = new BindingResolver(filenameBinding)
		ZipEntry ze
		while ((ze = inZipInputStream.getNextEntry()) != null) {

			def newName = bindingResolver.apply(ze.name)
			String fn = "$targetFolderName/$newName"
			if (ze.directory) {
				log.info "folder: $ze.name, new: $fn"
				new File(fn).mkdirs()
			}
			else {
				// see http://snipplr.com/view/1745/extract-zipentry-into-a-bytearrayoutputstream/
				log.info "unzipping file $fn"
				OutputStream output = new File(fn).newDataOutputStream()
				int data = 0;
				while( ( data = inZipInputStream.read() ) != -1 )
				{
					output.write( data );
				}
				// The ZipEntry is extracted in the output
				output.close();
			}
		}
		inZipInputStream.close()
	}
}
