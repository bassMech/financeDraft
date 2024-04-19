package de.bassmech.findra.web.view.model.type;

public enum AccountTransactionLayout {
	COLUMN_SINGLE("column.single", "col-12", 1),
	COLUMN_DOUBLE("column.double", "col-6", 2),
	COLUMN_TRIPLE("column.triple", "col-4", 3),
	;
			
	private String dbValue;
	private String flexColSpan;
	private int renderColumnCount;
	
	private AccountTransactionLayout(String dbValue, String flexColSpan, int renderColumnCount) {
		this.dbValue = dbValue;
		this.flexColSpan = flexColSpan;
		this.renderColumnCount = renderColumnCount;
	}

	public String getDbValue() {
		return dbValue;
	}

	public String getFlexColSpan() {
		return flexColSpan;
	}

	public int getRenderColumnCount() {
		return renderColumnCount;
	}
	
}
