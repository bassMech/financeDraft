package de.bassmech.findra.model.converter;

import de.bassmech.findra.model.statics.TransactionGrouping;
import jakarta.persistence.AttributeConverter;

public class TransactionGroupingConverter implements AttributeConverter<TransactionGrouping, Integer> {

	@Override
	public Integer convertToDatabaseColumn(TransactionGrouping attribute) {
		return attribute.getDbValue();
	}

	@Override
	public TransactionGrouping convertToEntityAttribute(Integer dbData) {
		return dbData == null ? null : TransactionGrouping.fromDbValue(dbData);
	}

}
