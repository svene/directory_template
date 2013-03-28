package org.svenehrke.directorytemplate

class DTInputParameterStorage {

	def loadParameters(DTMetaInfo inMetaInformation, Collection<DTInputParameter> inInputParameters) {
		DTMetaInfoFolder metaFolder = new DTMetaInfoFolder(metaInformation: inMetaInformation)

		// Load properties from previous runs:
		if (new File(inMetaInformation.inputParametersFilename()).exists()) {
			Properties props = metaFolder.newPropertiesFromFile()

			// Apply property values from previous runs to values of defined input parameters:
			new DTInputParameters().applyPropertiesToInputParameters(inInputParameters, props)
		}
	}

	/** Store possibly modified parameters back to property file */
	def storeParameters(DTMetaInfo inMetaInformation, Collection<DTInputParameter> inInputParameters) {
		DTMetaInfoFolder metaFolder = new DTMetaInfoFolder(metaInformation: inMetaInformation)
		metaFolder.createMetaInfoFolder()
		Properties props = metaFolder.newPropertiesFromFile()

		new DTInputParameters().applyParametersToProperties(inInputParameters, props)
		metaFolder.storeProperties(props)

	}
}
