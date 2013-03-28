package org.svenehrke.directorytemplate
import spock.lang.Specification

public class DTUtilTest extends Specification {

	def "test InputParametersAsMap"() {
		given:
			def ips = [
			    new DTInputParameter(name: 'n1', value: 'v1'),
			    new DTInputParameter(name: 'n2', value: 'v2'),
			]

		when:
			def result = DTUtil.inputParametersAsMap(ips)
		then:
			assert result instanceof Map
			assert result.n1.is(ips[0])
			assert result.n2.is(ips[1])
	}

}
