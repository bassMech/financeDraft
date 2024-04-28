package de.bassmech.findra.model.statics;

public enum TransactionGrouping {
	NONE(0, "transaction.grouping.no", "accountingMonthTransactions.xhtml"),
	SINGLE(1, "transaction.grouping.single", "accountingMonthTransactionsGroupedSingle.xhtml"),
	DOUBLE(2, "transaction.grouping.double", "accountingMonthTransactionsGroupedDouble.xhtml"),;

	private String tagKey;
	private int dbValue;
	private String groupingXhtml;

	private TransactionGrouping(int dbValue, String tagString, String groupingXhtml) {
		this.dbValue = dbValue;
		this.tagKey = tagString;
		this.groupingXhtml = groupingXhtml;
	}

	public String getTagKey() {
		return tagKey;
	}

	public int getDbValue() {
		return dbValue;
	}

	public String getGroupingXhtml() {
		return groupingXhtml;
	}

	public void setGroupingXhtml(String groupingXhtml) {
		this.groupingXhtml = groupingXhtml;
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
