package org.svenehrke.directorytemplate
import spock.lang.Specification

public class ApplyTest extends Specification {

	def "testApply"() {
		given:
			String userHome = System.properties['user.home']
			String gdtHome = "${userHome}/.gdt"

			String dtComponent = 'dt_java'
			String dtTemplate = 'simplejava'

		when:
			def fileNameBinding = ['@packagename@': DTUtil.dotsToSlashes('org.svenehrke')]
			def textBinding = ['packagename': 'org.svenehrke']

			new TemplateFolderToFolderCreator(
				gdtHome: gdtHome,
				targetDir: '/home/sven/tmp/gdt',
				templateName: 'simplejava',
				componentName: 'dt_java',
			).createTargetFolder(fileNameBinding, textBinding)


			def s = 'a'
		then:
			s == 'a'
	}

}
