package de.bassmech.findra.model.converter;

import java.time.Instant;

import de.bassmech.findra.model.statics.ConfigurationCode;
import jakarta.persistence.AttributeConverter;

public class NumberToInstantConverter implements AttributeConverter<Instant, Float>{

	@Override
	public Float convertToDatabaseColumn(Instant attribute) {
		return Float.valueOf(attribute.getEpochSecond());
	}

	@Override
	public Instant convertToEntityAttribute(Float dbData) {
		return Instant.ofEpochSecond(dbData.longValue());
	}
	
}
