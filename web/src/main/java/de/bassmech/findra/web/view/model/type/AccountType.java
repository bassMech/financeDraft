package de.bassmech.findra.web.view.model.type;

public enum AccountType {
	TRANSCATION(0, "account.transaction"),
	//METAL(1, "account.metal"),
	//SECURITIES(2, "account.securities")
	;
			
	private int dbValue;
	private String tagKey;
	
	private AccountType(int dbValue, String tagKey) {
		this.dbValue = dbValue;
		this.tagKey = tagKey;
	}

	public int getDbValue() {
		return dbValue;
	}

	public String getTagKey() {
		return tagKey;
	}

	
}
