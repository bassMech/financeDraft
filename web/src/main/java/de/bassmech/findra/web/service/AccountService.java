package de.bassmech.findra.web.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.bassmech.findra.core.repository.AccountRepository;
import de.bassmech.findra.model.entity.Account;
import de.bassmech.findra.web.model.AccountViewModel;
import de.bassmech.findra.web.util.ToViewModelUtil;

@Service
public class AccountService {
	@Autowired
	private AccountRepository accountRepository;
	
	private List<AccountViewModel> loadedAccounts = new ArrayList<>();
	
	public List<AccountViewModel> getAccountList() {
		if (loadedAccounts.size() == 0) {
			List<Account> accounts =accountRepository.findAllByDeletedAtIsNull();
			for (Account account : accounts) {
				loadedAccounts.add(ToViewModelUtil.toViewModel(account));
			}
		}
		
		return loadedAccounts;
	}
}
