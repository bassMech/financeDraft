package de.bassmech.findra.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.bassmech.findra.core.repository.AccountRepository;
import de.bassmech.findra.core.repository.AccountingYearRepository;
import de.bassmech.findra.model.entity.Account;
import de.bassmech.findra.model.entity.AccountingYear;
import de.bassmech.findra.web.model.AccountDialogViewModel;
import de.bassmech.findra.web.model.AccountType;
import de.bassmech.findra.web.model.AccountViewModel;
import de.bassmech.findra.web.model.AccountingYearViewModel;
import de.bassmech.findra.web.util.LocalizedMessageUtil;
import de.bassmech.findra.web.util.ToViewModelUtil;
import de.bassmech.findra.web.view.ViewBase;

@Service
public class AccountService {
	protected Logger logger = LoggerFactory.getLogger(AccountService.class);
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private AccountingYearRepository accountingYearRepository;
	
	private List<AccountViewModel> loadedAccounts = new ArrayList<>();
	private Map<Integer, List<AccountingYearViewModel>> loadedAccountingYearsByAccountId = new HashMap<>();
	private Map<Locale, Map<Integer, String>> accountTypesByLocale = new HashMap<>();
	
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
	
	public Map<Integer, String> getAccountTypes(Locale locale) {
		Map<Integer, String> result = accountTypesByLocale.get(locale);
		if (result == null) {
			result = new HashMap<>();
			for (AccountType entry : AccountType.values()) {
				result.put(entry.getDbValue(), LocalizedMessageUtil.getTag(entry.getTagKey(), locale));
			}
			accountTypesByLocale.put(locale, result);
		}
		return result;
	}

	public void save(AccountDialogViewModel accountDialog) {
		Account account;
		if (accountDialog.getId() == null) {
			account = new Account();
		} else {
			account = accountRepository.findById(accountDialog.getId().longValue()).get();
		}
		account.setTitle(accountDialog.getTitle());
		account.setDescription(accountDialog.getDescription());
		account = accountRepository.save(account);
		
		AccountViewModel accountVm = loadedAccounts.stream().filter(acc -> acc.getId().equals(accountDialog.getId())).findFirst().orElse(null);
		if (accountVm == null) {
			loadedAccounts.add(ToViewModelUtil.toViewModel(account));
		} else {
			int accountIndex = loadedAccounts.indexOf(accountVm);
			loadedAccounts.set(accountIndex, ToViewModelUtil.toViewModel(account));
		}
	}
}
