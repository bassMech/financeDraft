package de.bassmech.findra.model.entity;

import de.bassmech.findra.model.statics.AccountCategory;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("0")
public class CurrentAccount extends Account {
	
	public CurrentAccount() {
		super();
		setCategory(AccountCategory.CURRENT_ACCOUNT);
	}

}
