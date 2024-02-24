package de.bassmech.findra.model.statics;

public enum ConfigurationCode {
	PROJECT_VERSION("project.version"),
	YEAR_RANGE_MIN("year.range.min"),
	YEAR_RANGE_MAX("year.range.max"),
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
