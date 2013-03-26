package org.svenehrke.directorytemplate
import spock.lang.Specification

public class ApplyTest extends Specification {

	def "testApply"() {
		when:
			DirectoryTemplateResolver.createFolderFromTemplateFolder()
			def s = 'a'
		then:
			s == 'a'
//		println "hallo"
//		DirectoryTemplateResolver.createFolderFromTemplateFolder()
	}

}
