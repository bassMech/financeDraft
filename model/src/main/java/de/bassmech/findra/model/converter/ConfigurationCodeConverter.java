package de.bassmech.findra.model.converter;

import de.bassmech.findra.model.statics.ConfigurationCode;
import jakarta.persistence.AttributeConverter;

public class ConfigurationCodeConverter implements AttributeConverter<ConfigurationCode, String>{

	@Override
	public String convertToDatabaseColumn(ConfigurationCode attribute) {
		return attribute.getDbValue();
	}

	@Override
	public ConfigurationCode convertToEntityAttribute(String dbData) {
		return ConfigurationCode.fromDbValue(dbData);
	}
	
}
