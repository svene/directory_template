package org.svenehrke.directorytemplate;

import spock.lang.Specification
import spock.lang.Unroll

public class DirectoryTemplateResolverTest extends Specification {

	@Unroll
	def "testApplyBindings(#s,#b) -> #x"() {
		expect:
			x == DirectoryTemplateResolver.applyBindings(s, b)

		where:
			s                  | b                                | x
			'hallo'            | [:]                              | 'hallo'
			'hello @name'      | ['@name':'sven']                 | 'hello sven'
			'hello @one/@two'  | ['@one':'eins', '@two':'zwei']   | 'hello eins/zwei'
	}

	@Unroll
	def "testReplaceAll(#s) -> #x"() {
		expect:
			x == s.replaceAll('\\.', '/')

		where:
			s                      | x
			'a.b'                  | 'a/b'
			'org.svenehrke.gradle' | 'org/svenehrke/gradle'
	}
}
