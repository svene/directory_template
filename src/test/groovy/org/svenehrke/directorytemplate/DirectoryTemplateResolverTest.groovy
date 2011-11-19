package org.svenehrke.directorytemplate;

import spock.lang.Specification
import spock.lang.Unroll

public class DirectoryTemplateResolverTest extends Specification {

	@Unroll({"applyBindings(#s,#b) -> #x"})
	def testApplyBindings() {
		expect:
			x == DirectoryTemplateResolver.applyBindings(s, b)

		where:
			s                  | b                                | x
			'hallo'            | [:]                              | 'hallo'
			'hello @name'      | ['@name':'sven']                 | 'hello sven'
			'hello @one/@two'  | ['@one':'eins', '@two':'zwei']   | 'hello eins/zwei'
	}

	@Unroll({"replaceAll(#s) -> #x"})
	def testRegEx() {
		expect:
			x == s.replaceAll('\\.', '/')

		where:
			s                      | x
			'a.b'                  | 'a/b'
			'org.svenehrke.gradle' | 'org/svenehrke/gradle'
	}
}
