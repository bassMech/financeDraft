package de.bassmech.findra.model.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "account_item_group")
public class AccountItemGroup extends AccountItemBase {

	@ManyToOne(targetEntity = Account.class)
	@JoinColumn(referencedColumnName = "id", name = "account_id")
	private Account account;

	@OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
	private List<AccountItem> items = new ArrayList<>();

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public List<AccountItem> getItems() {
		return items;
	}

	public void setItems(List<AccountItem> items) {
		this.items = items;
	}

}
