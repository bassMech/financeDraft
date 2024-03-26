package de.bassmech.findra.web.util;

import java.time.YearMonth;

public class CalculationUtil {
	public static int getYearMonthDiffInMonth(YearMonth first, YearMonth second) {
		return (first.getYear() - second.getYear()) * 12 + first.getMonthValue() - second.getMonthValue();
	}
}
