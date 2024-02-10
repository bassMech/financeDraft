package de.bassmech.findra.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.bassmech.findra.core.repository.AccountRepository;
import de.bassmech.findra.core.repository.AccountingYearRepository;
import de.bassmech.findra.model.entity.Account;
import de.bassmech.findra.model.entity.AccountingYear;
import de.bassmech.findra.web.model.AccountViewModel;
import de.bassmech.findra.web.model.AccountingYearViewModel;
import de.bassmech.findra.web.util.ToViewModelUtil;

@Service
public class AccountService {
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private AccountingYearRepository accountingYearRepository;
	
	private List<AccountViewModel> loadedAccounts = new ArrayList<>();
	private Map<Integer, List<AccountingYearViewModel>> loadedAccountingYearsByAccountId = new HashMap<>();
	
	public List<AccountViewModel> getAccountList() {
		if (loadedAccounts.size() == 0) {
			List<Account> accounts =accountRepository.findAllByDeletedAtIsNull();
			for (Account account : accounts) {
				loadedAccounts.add(ToViewModelUtil.toViewModel(account));
			}
		}
		
		return loadedAccounts;
	}

	public AccountingYearViewModel getAccountMonthsForYear(int accountId, int year) {
		AccountingYearViewModel yearModel = null; 
		if (loadedAccountingYearsByAccountId.containsKey(accountId)) {
			yearModel = loadedAccountingYearsByAccountId.get(accountId).stream().filter(entry -> entry.getYear() == year).findFirst().orElse(null);
		}
		if (yearModel == null) {
			AccountingYear entity = accountingYearRepository.findByAccountIdAndYear(accountId, year);
			if (entity != null) {
				yearModel = ToViewModelUtil.toViewModel(entity);
				if (!loadedAccountingYearsByAccountId.containsKey(accountId)) {
					loadedAccountingYearsByAccountId.put(accountId, new ArrayList<>());
				}
				loadedAccountingYearsByAccountId.get(accountId).add(yearModel);
			}
		}
		// what to do if non found?
		return yearModel;
	}
}
