package de.bassmech.findra.model.statics;

public enum ExpectedDay {
	ULTIMO(32, "ultimo"),
	UNKNOWN(0, "unknown"),
	//AS_IS(1, "as_is"),
	;
	
	private String tagString;
	private int dbValue;
	
	private ExpectedDay(int dbValue, String tagString) {
		this.dbValue = dbValue;
		this.tagString = tagString;
	}
	
	public String getTagString() {
		return tagString;
	}
	public int getDbValue() {
		return dbValue;
	}
	
	public static String getTagStringByDbValue(int dbValue) {
		for (ExpectedDay day : values()) {
			if (day.dbValue == dbValue) {
				return day.tagString;
			}
		}
		return null;
	}
	
}
