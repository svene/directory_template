package org.svenehrke.directorytemplate

class DTInputParameter {
	String name, value, prompt

	@Override
	public String toString() {
		return "DTInputParameter{" +
			"name='" + name + '\'' +
			", value='" + value + '\'' +
			", prompt='" + prompt + '\'' +
			'}';
	}
}