package de.bassmech.findra.model.statics;

public enum SettingCode {
	LOCALE("locale"),
	CURRENCY("currency"),
	DATE_FORMAT("date.format"),
	NUMBER_GROUPING("number.separator"),
	CURRENCY_SYMBOL_POSITION("currency.symbol_position"),
	ACCOUNT_TRANSACTION_LAYOUT("account.transaction.layout"), 
	;
	
	private String dbValue;
	
	private SettingCode(String dbValue) {
		this.dbValue = dbValue;
	}

	public String getDbValue() {
		return dbValue;
	}

	public static SettingCode fromDbValue(String dbData) {
		for (SettingCode value : SettingCode.values()) {
			if (value.dbValue.equals(dbData)) {
				return value;
			}
		}
		return null;
	}
	
}
