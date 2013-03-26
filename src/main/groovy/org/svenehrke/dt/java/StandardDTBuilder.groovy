package org.svenehrke.dt.java

import org.svenehrke.directorytemplate.BaseDirectoryTemplateBuilder
import org.svenehrke.directorytemplate.DTInputParameter

abstract class StandardDTBuilder extends BaseDirectoryTemplateBuilder {

	@Override
	Map<String, String> newFilenameBinding(Map<String, DTInputParameter> aInputParameters) {
		[:]
	}

	@Override
	Map<String, String> newTextBinding(Map<String, DTInputParameter> aInputParameters) {
		[:]
	}

	@Override
	List<String> getExclusions() {
		['.gif', '.png']
	}

}
