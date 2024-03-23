package de.bassmech.findra.web.util.statics;

public enum CssReference {
	TRANSCTION_SUM_POSITIVE("transaction-sum-positive"),
	TRANSCTION_SUM_NEGATIVE("transaction-sum-negative"),
	
	TRANSCTION_BG_NEGATIVE_EXPECTED("transaction-negative-expected"),
	TRANSCTION_BG_NEGATIVE_EXECUTED("transaction-negative-executed"),
	TRANSCTION_BG_POSITIVE_EXPECTED("transaction-positive-expected"),
	TRANSCTION_BG_POSITIVE_EXECUTED("transaction-positive-executed"),
	;

	private String value;

	private CssReference(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
