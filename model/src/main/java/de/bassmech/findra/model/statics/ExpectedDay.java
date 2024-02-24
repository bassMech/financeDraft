package de.bassmech.findra.model.statics;

public enum ExpectedDay {
	UNKNOWN(0, "unknown"),
	AS_IS(1, "as_is"),
	ULTIMO(32, "unltimo"),
	;
	
	private String tagString;
	private int dbValue;
	
	private ExpectedDay(int dbValue, String tagString) {
		
	}
	
	public String getTagString() {
		return tagString;
	}
	public int getDbValue() {
		return dbValue;
	}
	
	
}
