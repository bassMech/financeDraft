package de.bassmech.findra.model.converter;

import de.bassmech.findra.model.statics.Interval;
import jakarta.persistence.AttributeConverter;

public class IntervalConverter implements AttributeConverter<Interval, Integer>{

	@Override
	public Integer convertToDatabaseColumn(Interval attribute) {
		return attribute == null ? null : attribute.getDbValue();
	}

	@Override
	public Interval convertToEntityAttribute(Integer dbData) {
		return dbData == null ? null : Interval.fromDbValue(dbData);
	}
	
}
