package de.bassmech.findra.model.statics;

public enum ConfigurationCode {
	PROJECT_VERSION("project.version")
	;
	
	private String dbValue;
	
	private ConfigurationCode(String dbValue) {
		this.dbValue = dbValue;
	}

	public String getDbValue() {
		return dbValue;
	}

	public static ConfigurationCode fromDbValue(String dbData) {
		for (ConfigurationCode value : ConfigurationCode.values()) {
			if (value.dbValue.equals(dbData)) {
				return value;
			}
		}
		return null;
	}
	
}
