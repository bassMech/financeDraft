package de.bassmech.findra.model.converter;

import de.bassmech.findra.model.statics.TransactionColumnLayout;
import jakarta.persistence.AttributeConverter;

public class TransactionColumnLayoutConverter implements AttributeConverter<TransactionColumnLayout, Integer>{

	@Override
	public Integer convertToDatabaseColumn(TransactionColumnLayout attribute) {
		return attribute.getDbValue();
	}

	@Override
	public TransactionColumnLayout convertToEntityAttribute(Integer dbData) {
		return TransactionColumnLayout.fromDbValue(dbData);
	}
	
}
