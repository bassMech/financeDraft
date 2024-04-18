package de.bassmech.findra.model.converter;

import de.bassmech.findra.model.statics.AccountCategory;
import de.bassmech.findra.model.statics.ConfigurationCode;
import jakarta.persistence.AttributeConverter;

public class AccountCategoryConverter implements AttributeConverter<AccountCategory, Integer>{

	@Override
	public Integer convertToDatabaseColumn(AccountCategory attribute) {
		return attribute.getDbValue();
	}

	@Override
	public AccountCategory convertToEntityAttribute(Integer dbData) {
		return AccountCategory.fromDbValue(dbData);
	}
	
}
