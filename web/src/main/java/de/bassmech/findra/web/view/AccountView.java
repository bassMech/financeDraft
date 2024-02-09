package de.bassmech.findra.web.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import de.bassmech.findra.web.model.AccountViewModel;
import de.bassmech.findra.web.service.AccountService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class AccountView implements Serializable {
	private List<AccountViewModel> selectableAccounts = new ArrayList<>();
	private AccountViewModel selectedAccount;
	
	@Autowired
	private AccountService accountService;
	
	@PostConstruct
	public void init() {
		selectableAccounts = accountService.getAccountList();
	}

	public AccountViewModel getSelectedAccount() {
		return selectedAccount;
	}

	public void setSelectedAccount(AccountViewModel selectedAccount) {
		this.selectedAccount = selectedAccount;
	}

	public List<AccountViewModel> getSelectableAccounts() {
		return selectableAccounts;
	}
	
	
}
