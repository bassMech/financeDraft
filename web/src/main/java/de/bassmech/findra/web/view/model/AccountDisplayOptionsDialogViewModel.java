package de.bassmech.findra.web.view.model;

import java.util.HashMap;
import java.util.Map;

import de.bassmech.findra.web.view.model.type.AccountTransactionLayout;

public class AccountDisplayOptionsDialogViewModel {
	private static final long serialVersionUID = 1L;

	private AccountViewModel account;
	private Map<Integer, AccountTransactionLayout> layoutMap = new HashMap<>();

	public AccountViewModel getAccount() {
		return account;
	}

	public void setAccount(AccountViewModel account) {
		this.account = account;
	}

	public Map<Integer, AccountTransactionLayout> getLayoutMap() {
		return layoutMap;
	}

	public void setLayoutMap(Map<Integer, AccountTransactionLayout> layoutMap) {
		this.layoutMap = layoutMap;
	}

}
