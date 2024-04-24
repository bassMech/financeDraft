package de.bassmech.findra.web.view.model;

import java.util.HashMap;
import java.util.Map;

import de.bassmech.findra.web.util.LocalizationUtil;
import de.bassmech.findra.web.view.model.type.AccountTransactionLayout;

public class AccountDisplayOptionsDialogViewModel {
	private static final long serialVersionUID = 1L;

	private AccountViewModel account;
	private String dialogTitle;
	private Map<Integer, String> layoutMap = new HashMap<>();
	
	public AccountDisplayOptionsDialogViewModel() {
		this.dialogTitle = LocalizationUtil.getTag("account.display.options");
		for (AccountTransactionLayout layout : AccountTransactionLayout.values()) {
			layoutMap.put(layout.getRenderColumnCount(), LocalizationUtil.getTag(layout.getTagKey()));
		}
	}
	
	public AccountViewModel getAccount() {
		return account;
	}

	public void setAccount(AccountViewModel account) {
		this.account = account;
	}

	public Map<Integer, String> getLayoutMap() {
		return layoutMap;
	}

	public void setLayoutMap(Map<Integer, String> layoutMap) {
		this.layoutMap = layoutMap;
	}

	public String getDialogTitle() {
		return dialogTitle;
	}

	public void setDialogTitle(String dialogTitle) {
		this.dialogTitle = dialogTitle;
	}

}
