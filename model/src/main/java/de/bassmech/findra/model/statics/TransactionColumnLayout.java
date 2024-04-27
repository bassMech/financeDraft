package de.bassmech.findra.model.statics;

public enum TransactionColumnLayout {
	SINGLE(1, "column.single", "col-12"),
	DOUBLE(2, "column.double", "col-6"),
	TRIPLE(3, "column.triple", "col-4"),
	;
	
	private String tagKey;
	private int dbValue;
	private String flexColSpan;
	
	private TransactionColumnLayout(int dbValue, String tagString, String flexColSpan) {
		this.dbValue = dbValue;
		this.tagKey = tagString;
		this.flexColSpan = flexColSpan;
	}
	
	public String getTagKey() {
		return tagKey;
	}
	public int getDbValue() {
		return dbValue;
	}
	
		public String getFlexColSpan() {
		return flexColSpan;
	}
	
	public int getRenderColumnCount() {
		return dbValue;
	}
	
	public static String getTagStringByDbValue(int dbValue) {
		for (TransactionColumnLayout interval : values()) {
			if (interval.dbValue == dbValue) {
				return interval.tagKey;
			}
		}
		return null;
	}

	public static TransactionColumnLayout fromDbValue(int dbValue) {
		for (TransactionColumnLayout interval : values()) {
			if (interval.dbValue == dbValue) {
				return interval;
			}
		}
		return null;
	}
	
//	COLUMN_SINGLE("column.single", "col-12", 1),
//	COLUMN_DOUBLE("column.double", "col-6", 2),
//	COLUMN_TRIPLE("column.triple", "col-4", 3),
//	;
//			
//	private String tagKey;
//	private String flexColSpan;
//	private int renderColumnCount;
//	
//	private AccountTransactionLayout(String tagKey, String flexColSpan, int renderColumnCount) {
//		this.tagKey = tagKey;
//		this.flexColSpan = flexColSpan;
//		this.renderColumnCount = renderColumnCount;
//	}
//
//	public String getTagKey() {
//		return tagKey;
//	}
//
//	public String getFlexColSpan() {
//		return flexColSpan;
//	}
//
//	public int getRenderColumnCount() {
//		return renderColumnCount;
//	}
	
}
