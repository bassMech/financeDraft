package de.bassmech.findra.model.entity;

import de.bassmech.findra.model.statics.AccountCategory;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("1")
public class SavingsAccount extends Account {
	
	public SavingsAccount() {
		super();
		setCategory(AccountCategory.SAVINGS_ACCOUNT);
	}

}
