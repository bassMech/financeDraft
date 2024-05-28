package de.bassmech.findra.model.converter;

import java.time.YearMonth;

import jakarta.persistence.AttributeConverter;

public class YearMonthDbConverter implements AttributeConverter<YearMonth, String>{

	@Override
	public String convertToDatabaseColumn(YearMonth attribute) {
		return attribute == null ? null : attribute.getYear() + "-" + attribute.getMonthValue();
	}

	@Override
	public YearMonth convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return null;
		}
		String[] split = dbData.split("-");
		return YearMonth.of(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
	}
	
}
