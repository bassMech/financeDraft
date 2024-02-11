package de.bassmech.findra.model.converter;

import de.bassmech.findra.model.statics.SettingCode;
import jakarta.persistence.AttributeConverter;

public class SettingCodeConverter implements AttributeConverter<SettingCode, String>{

	@Override
	public String convertToDatabaseColumn(SettingCode attribute) {
		return attribute.getDbValue();
	}

	@Override
	public SettingCode convertToEntityAttribute(String dbData) {
		return SettingCode.fromDbValue(dbData);
	}
	
}
