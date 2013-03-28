package org.svenehrke.directorytemplate

class DTInputParameterStorage {

	def applyStoredPropertiesToInputParameters(DTMetaInfo inMetaInformation, Collection<DTInputParameter> inInputParameters) {
		DTMetaInfoFolder metaFolder = new DTMetaInfoFolder(metaInformation: inMetaInformation)

		// Load properties from previous runs:
		Properties props = metaFolder.newPropertiesFromFile()

		// Apply property values from previous runs to values of defined input parameters:
		new DTInputParameters().applyPropertiesToInputParameters(inInputParameters, props)
	}

	/** Store possibly modified parameters back to property file */
	def applyParametersToProperties(DTMetaInfo inMetaInformation, Collection<DTInputParameter> inInputParameters) {
		DTMetaInfoFolder metaFolder = new DTMetaInfoFolder(metaInformation: inMetaInformation)
		Properties props = metaFolder.newPropertiesFromFile()

		new DTInputParameters().applyParametersToProperties(inInputParameters, props)
		metaFolder.storeProperties(props)

	}
}
