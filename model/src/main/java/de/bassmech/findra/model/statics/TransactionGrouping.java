package de.bassmech.findra.model.statics;

public enum TransactionGrouping {
	SINGLE(0, "transaction.grouping.no"),
	DOUBLE(1, "transaction.grouping.single"),
	TRIPLE(2, "transaction.grouping.double"),
	;
	
	private String tagKey;
	private int dbValue;
	
	private TransactionGrouping(int dbValue, String tagString) {
		this.dbValue = dbValue;
		this.tagKey = tagString;
	}
	
	public String getTagKey() {
		return tagKey;
	}
	public int getDbValue() {
		return dbValue;
	}
	
	public static String getTagStringByDbValue(int dbValue) {
		for (TransactionGrouping interval : values()) {
			if (interval.dbValue == dbValue) {
				return interval.tagKey;
			}
		}
		return null;
	}

	public static TransactionGrouping fromDbValue(int dbValue) {
		for (TransactionGrouping interval : values()) {
			if (interval.dbValue == dbValue) {
				return interval;
			}
		}
		return null;
	}
	
}
