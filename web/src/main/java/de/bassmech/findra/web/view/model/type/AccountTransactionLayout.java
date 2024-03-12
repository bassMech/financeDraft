package de.bassmech.findra.web.view.model.type;

public enum AccountTransactionLayout {
	COLUMN_SINGLE("column.single"),
	COLUMN_DOUBLE("column.double"),
	COLUMN_TRIPLE("column.triple"),
	;
			
	private String dbValue;
	
	private AccountTransactionLayout(String dbValue) {
		this.dbValue = dbValue;
	}

	public String getDbValue() {
		return dbValue;
	}

	
}
