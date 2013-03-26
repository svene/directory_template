package org.svenehrke.directorytemplate
import spock.lang.Specification

public class ApplyTest extends Specification {

	def "testApply"() {
		when:
			def fileNameBinding = ['@packagename@': DTUtil.dotsToSlashes('org.svenehrke')]
			def textBinding = ['packagename': 'org.svenehrke']

			new TemplateFolderToFolderCreator().createFolderFromTemplateFolder(fileNameBinding, textBinding)
			def s = 'a'
		then:
			s == 'a'
	}

}
