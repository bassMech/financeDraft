package de.bassmech.findra.web.util.statics.enums;

public enum FormIds {
	MAIN_FORM("mainForm"),;

	private String value;

	private FormIds(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	
}
