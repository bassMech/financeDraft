package de.bassmech.findra.web.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class FormatterUtil {
	public static String getCurrencyNumberFormatted(BigDecimal value, String numberGrouping) {
		String[] split = numberGrouping.split("_");
		DecimalFormat df = new DecimalFormat("###,##0.00");

		DecimalFormatSymbols customSymbol = new DecimalFormatSymbols();
		customSymbol.setGroupingSeparator(split[0].charAt(0));
		customSymbol.setDecimalSeparator(split[1].charAt(0));
		df.setDecimalFormatSymbols(customSymbol);
		
		return df.format(value);
	}
	
	public static String getCurrencyNumberFormattedWithSymbol(BigDecimal value, String numberGrouping, String currencySymbol, boolean isSymbolPositionPrefix) {
		String prefix = "";
		String suffix = "";
		if (isSymbolPositionPrefix) {
			prefix = currencySymbol + " ";
		} else {
			suffix = " " + currencySymbol;
		}

		return prefix + getCurrencyNumberFormatted(value, numberGrouping) + suffix;
	}
}
