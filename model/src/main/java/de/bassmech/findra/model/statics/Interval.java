package de.bassmech.findra.model.statics;

public enum Interval {
	EVERY_MONTH(1, "month.every"),
	EVERY_OTHER_MONTH(2, "month.every.other"),
	EVERY_THIRD_MONTH(3, "month.every.third"),
	EVERY_FOURTH_MONTH(4, "month.every.fourth"),
	EVERY_FIFTH_MONTH(5, "month.every.fifth"),
	EVERY_SIXTH_MONTH(6, "month.every.sixth"),
	EVERY_SEVENTH_MONTH(7, "month.every.seventh"),
	EVERY_EIGHTH_MONTH(8, "month.every.eighth"),
	EVERY_NINTH_MONTH(9, "month.every.ninth"),
	EVERY_TENTH_MONTH(10, "month.every.tenth"),
	EVERY_ELEVENTH_MONTH(11, "month.every.eleventh"),
	EVERY_YEAR(12, "year.every"),
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
