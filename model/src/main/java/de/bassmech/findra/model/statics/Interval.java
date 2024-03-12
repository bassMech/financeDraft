package de.bassmech.findra.model.statics;

public enum Interval {
	EVERY_MONTH(0, "month.every"),
	EVERY_OTHER_MONTH(1, "month.every.other"),
	;
	
	private String tagString;
	private int dbValue;
	
	private Interval(int dbValue, String tagString) {
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
		for (Interval interval : values()) {
			if (interval.dbValue == dbValue) {
				return interval.tagString;
			}
		}
		return null;
	}

	public static Interval fromDbValue(int dbValue) {
		for (Interval interval : values()) {
			if (interval.dbValue == dbValue) {
				return interval;
			}
		}
		return null;
	}
	
}
