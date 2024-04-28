package de.bassmech.findra.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "account_item")
public class AccountItem extends AccountItemBase {

	@ManyToOne(targetEntity = AccountItemGroup.class)
	@JoinColumn(referencedColumnName = "id", name = "account_item_group_id")
	private AccountItemGroup group;

	public AccountItemGroup getGroup() {
		return group;
	}

	public void setGroup(AccountItemGroup group) {
		this.group = group;
	}

}
