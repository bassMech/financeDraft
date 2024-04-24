package de.bassmech.findra.web.view.model.type;

public enum AccountTransactionLayout {
	COLUMN_SINGLE("column.single", "col-12", 1),
	COLUMN_DOUBLE("column.double", "col-6", 2),
	COLUMN_TRIPLE("column.triple", "col-4", 3),
	;
			
	private String tagKey;
	private String flexColSpan;
	private int renderColumnCount;
	
	private AccountTransactionLayout(String tagKey, String flexColSpan, int renderColumnCount) {
		this.tagKey = tagKey;
		this.flexColSpan = flexColSpan;
		this.renderColumnCount = renderColumnCount;
	}

	public String getTagKey() {
		return tagKey;
	}

	public String getFlexColSpan() {
		return flexColSpan;
	}

	public int getRenderColumnCount() {
		return renderColumnCount;
	}
	
}
