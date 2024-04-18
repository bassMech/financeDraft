package de.bassmech.findra.model.statics;

public enum AccountCategory {
	CURRENT_ACCOUNT(0, "account.current"),
	SAVINGS_ACCOUNT(1, "account.savings"),
	;
	
	private String tagKey;
	private int dbValue;
	
	private AccountCategory(int dbValue, String tagString) {
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
		for (AccountCategory interval : values()) {
			if (interval.dbValue == dbValue) {
				return interval.tagKey;
			}
		}
		return null;
	}

	public static AccountCategory fromDbValue(int dbValue) {
		for (AccountCategory interval : values()) {
			if (interval.dbValue == dbValue) {
				return interval;
			}
		}
		return null;
	}
	
}
