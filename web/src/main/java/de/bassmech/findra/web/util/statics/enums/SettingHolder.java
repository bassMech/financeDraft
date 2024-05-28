package de.bassmech.findra.web.util.statics.enums;

import java.util.Currency;
import java.util.Locale;

public class SettingHolder {
	private static SettingHolder instance;
	
	private Locale dbLocale;
	private Currency dbCurrency;
	private String dbDateFormat;
	private String dbNumberThousandSeparator;
	private String dbNumberDecimalSeparator;
	private String dbNumberGrouping;
	private String dbCurrencySymbolPosition;
	
	private SettingHolder() {
		
	}
	
	public static SettingHolder getInstance() {
		if (instance == null) {
			instance = new SettingHolder();
		}
		return instance;
	}

	public Locale getDbLocale() {
		return dbLocale;
	}

	public void setDbLocale(Locale dbLocale) {
		this.dbLocale = dbLocale;
	}

	public Currency getDbCurrency() {
		return dbCurrency;
	}

	public void setDbCurrency(Currency dbCurrency) {
		this.dbCurrency = dbCurrency;
	}

	public String getDbDateFormat() {
		return dbDateFormat;
	}

	public void setDbDateFormat(String dbDateFormat) {
		this.dbDateFormat = dbDateFormat;
	}

	public String getDbNumberThousandSeparator() {
		return dbNumberThousandSeparator;
	}

	public void setDbNumberThousandSeparator(String dbNumberThousandSeparator) {
		this.dbNumberThousandSeparator = dbNumberThousandSeparator;
	}

	public String getDbNumberDecimalSeparator() {
		return dbNumberDecimalSeparator;
	}

	public void setDbNumberDecimalSeparator(String dbNumberDecimalSeparator) {
		this.dbNumberDecimalSeparator = dbNumberDecimalSeparator;
	}

	public String getDbNumberGrouping() {
		return dbNumberGrouping;
	}

	public void setDbNumberGrouping(String dbNumberGrouping) {
		this.dbNumberGrouping = dbNumberGrouping;
	}

	public String getDbCurrencySymbolPosition() {
		return dbCurrencySymbolPosition;
	}

	public void setDbCurrencySymbolPosition(String dbCurrencySymbolPosition) {
		this.dbCurrencySymbolPosition = dbCurrencySymbolPosition;
	}

	public void setInstance(SettingHolder instance) {
		this.instance = instance;
	}
	
	
}
