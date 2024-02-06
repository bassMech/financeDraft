package de.bassmech.findra.core.statics;

public enum FrontendLanguageType {
	GERMAN("de"),
	ENGLISH("en")
	;
	
	private String code;
	
	private FrontendLanguageType(String code) {
		this.code = code;
	}

	public static boolean hasCode(String code) {
		for (FrontendLanguageType entry : values()) {
			if (entry.code.equals(code)) {
				return true;
			}
		}
		return false;
	}
}
