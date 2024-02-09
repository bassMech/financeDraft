package de.bassmech.findra.model.converter;

import java.time.Instant;

import de.bassmech.findra.model.statics.ConfigurationCode;
import jakarta.persistence.AttributeConverter;

public class NumberToInstantConverter implements AttributeConverter<Instant, Integer>{

	@Override
	public Integer convertToDatabaseColumn(Instant attribute) {
		return attribute == null ? null : Math.toIntExact(attribute.getEpochSecond());
	}

	@Override
	public Instant convertToEntityAttribute(Integer dbData) {
		return dbData == null ? null : Instant.ofEpochSecond(dbData.longValue());
	}
	
}
