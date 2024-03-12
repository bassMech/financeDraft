package de.bassmech.findra.web.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.bassmech.findra.core.repository.AccountRepository;
import de.bassmech.findra.core.repository.AccountingYearRepository;
import de.bassmech.findra.core.repository.AccountTransactionRepository;
import de.bassmech.findra.model.entity.Account;
import de.bassmech.findra.model.entity.AccountTransaction;
import de.bassmech.findra.model.entity.AccountTransactionDraft;
import de.bassmech.findra.model.entity.AccountingMonth;
import de.bassmech.findra.model.entity.AccountingYear;
import de.bassmech.findra.model.entity.Tag;
import de.bassmech.findra.web.util.LocalizedMessageUtil;
import de.bassmech.findra.web.util.ToViewModelUtil;
import de.bassmech.findra.web.view.model.AccountDetailDialogViewModel;
import de.bassmech.findra.web.view.model.AccountViewModel;
import de.bassmech.findra.web.view.model.AccountingMonthViewModel;
import de.bassmech.findra.web.view.model.AccountingYearViewModel;
import de.bassmech.findra.web.view.model.DraftViewModel;
import de.bassmech.findra.web.view.model.TagViewModel;
import de.bassmech.findra.web.view.model.TransactionDetailDialogViewModel;
import de.bassmech.findra.web.view.model.TransactionExecutedDialogViewModel;
import de.bassmech.findra.web.view.model.TransactionViewModel;
import de.bassmech.findra.web.view.model.type.AccountType;

@Service
public class AccountService {
	protected Logger logger = LoggerFactory.getLogger(AccountService.class);

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private AccountingYearRepository accountingYearRepository;
	
	@Autowired
	private AccountTransactionRepository tagRepository;
	
	@Autowired
	private AccountTransactionRepository transactionRepository;

	private List<AccountViewModel> loadedAccounts = new ArrayList<>();
	private Map<Integer, List<AccountingYearViewModel>> loadedAccountingYearsByAccountId = new HashMap<>();
	private Map<Integer, List<DraftViewModel>> loadedDraftsByAccountId = new HashMap<>();
	private Map<Locale, Map<Integer, String>> accountTypesByLocale = new HashMap<>();

	public List<AccountViewModel> getAccountList() {
		if (loadedAccounts.size() == 0) {
			List<Account> accounts = accountRepository.findAllByDeletedAtIsNull();
			for (Account account : accounts) {
				loadedAccounts.add(ToViewModelUtil.toViewModel(account));
			}
		}

		return loadedAccounts;
	}

	public AccountingYearViewModel getAccountYear(int accountId, int year) {
		AccountingYearViewModel yearModel = null;
		if (loadedAccountingYearsByAccountId.containsKey(accountId)) {
			yearModel = loadedAccountingYearsByAccountId.get(accountId).stream()
					.filter(entry -> entry.getYear() == year).findFirst().orElse(null);
		}
		if (yearModel == null) {
			AccountingYear entity = accountingYearRepository.findByAccountIdAndYear(accountId, year);
			if (entity != null) {
				yearModel = ToViewModelUtil.toViewModel(entity);
				if (!loadedAccountingYearsByAccountId.containsKey(accountId)) {
					loadedAccountingYearsByAccountId.put(accountId, new ArrayList<>());
				}
				loadedAccountingYearsByAccountId.get(accountId).add(yearModel);
				
				addDraftsToYearViewModel(accountId, yearModel);
			}
		}
		return yearModel;
	}
	
	public List<DraftViewModel> getDraftsForYear(Integer accountId, int year) {
		
		//drafts
		if (loadedDraftsByAccountId.get(accountId) == null) {
			Account account = accountRepository.findById(accountId.longValue()).orElse(null);
			loadedDraftsByAccountId.put(accountId, ToViewModelUtil.toDraftViewModelList(account.getDrafts()));
		}

		//Instant yearStart = LocalDate.of(year, 1, 1).atStartOfDay(ZoneOffset.UTC).toInstant();
		YearMonth nextYearStart = YearMonth.of(year + 1, 1);// LocalDate.of(year + 1, 1, 1).atStartOfDay(ZoneOffset.UTC).toInstant();
		
		return loadedDraftsByAccountId.get(accountId).stream().filter(draft -> draft.getStartsAt().compareTo(nextYearStart) <= 0
				&& (draft.getEndsAt() == null || draft.getEndsAt().compareTo(nextYearStart) <= 0)).toList();
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

	public void saveAccount(AccountDetailDialogViewModel accountDialog) {
		Account account;
		if (accountDialog.getId() == null) {
			account = new Account();
		} else {
			account = accountRepository.findById(accountDialog.getId().longValue()).get();
		}
		account.setTitle(accountDialog.getTitle());
		account.setDescription(accountDialog.getDescription());
		account.setStartingYear(accountDialog.getStartingYear());
		account = accountRepository.save(account);

		if (accountDialog.getId() == null) {
			AccountingYear ay = new AccountingYear();
			ay.setAccount(account);
			ay.setYear(Year.now().getValue());

			AccountingMonth am = new AccountingMonth();
			am.setAccountingYear(ay);
			am.setMonth(LocalDate.now().getMonthValue());
			ay.getMonths().add(am);
			ay = accountingYearRepository.save(ay);
		}

		AccountViewModel accountVm = loadedAccounts.stream().filter(acc -> acc.getId().equals(accountDialog.getId()))
				.findFirst().orElse(null);
		if (accountVm == null) {
			loadedAccounts.add(ToViewModelUtil.toViewModel(account));
		} else {
			int accountIndex = loadedAccounts.indexOf(accountVm);
			loadedAccounts.set(accountIndex, ToViewModelUtil.toViewModel(account));
		}
	}

	public void deleteAccount(Integer accountId) {
		logger.debug(String.format("Deleting account with id: %d", accountId));
		Account account = accountRepository.findById(accountId.longValue()).orElseGet(null);
		if (account == null) {
			logger.error(String.format("account to delete with id %d not found", accountId));
			return;
		}
		account.setDeletedAt(Instant.now());
		accountRepository.save(account);

		loadedAccounts.removeIf(acc -> acc.getId().equals(accountId));
	}

	public void saveTransaction(TransactionDetailDialogViewModel transactionDialog, int year, int month) {
		if (transactionDialog.isDraft()) {
			logger.error("got draft but thats not allowed");
			return;
		}
		
		Integer accountId = transactionDialog.getAccountId();
		Account account = accountRepository.findById(accountId.longValue()).get();
		// check loaded
		AccountingYearViewModel yearVm = loadedAccountingYearsByAccountId.get(accountId).stream()
				.filter(yearX -> yearX.getYear() == year).findFirst().orElse(null);
		// check repo
		AccountingYear accountingYear = null;
		if (yearVm == null) {
			accountingYear = accountingYearRepository.findByAccountIdAndYear(accountId, year);
			if (accountingYear == null) {
				accountingYear = new AccountingYear();
				accountingYear.setAccount(account);
				accountingYear.setYear(year);

				accountingYear = accountingYearRepository.save(accountingYear);
			}
			yearVm = ToViewModelUtil.toViewModel(accountingYear);
			loadedAccountingYearsByAccountId.get(accountId).add(yearVm);
		}

		AccountingMonthViewModel monthVm = yearVm.getMonths().stream().filter(monthX -> monthX.getMonth() == month)
				.findFirst().orElse(null);
		AccountingMonth newMonth = null;
		if (accountingYear == null) {
			accountingYear = accountingYearRepository.findByAccountIdAndYear(accountId,
					year);
		}
		if (monthVm == null || monthVm.getId() == null) {
			newMonth = new AccountingMonth();
			newMonth.setAccountingYear(accountingYear);
			newMonth.setMonth(month);
			accountingYear.getMonths().add(newMonth);

			accountingYear = accountingYearRepository.save(accountingYear);
			yearVm.getMonths().removeIf(x -> x.getMonth() == month);
			newMonth = accountingYear.getMonths().stream().filter(monthX -> monthX.getMonth() == month).findFirst()
					.orElse(null);

			monthVm = ToViewModelUtil.toViewModel(newMonth);
			yearVm.getMonths().add(monthVm);
		}

		AccountTransaction transaction;
		if (newMonth == null) {
			newMonth = accountingYear.getMonths().stream().filter(monthX -> monthX.getMonth() == month).findFirst()
					.orElse(null);
		}
		if (transactionDialog.getId() != null) {
			transaction = newMonth.getTransactions().stream().filter(tr -> tr.getId() == transactionDialog.getId()).findFirst().orElse(null);
		} else {
			transaction = new AccountTransaction();
			transaction.setAccountingMonth(newMonth);
		}
		
		transaction.setTitle(transactionDialog.getTitle());
		transaction.setDescription(transactionDialog.getDescription());
		transaction.setValue(transactionDialog.getValue());
		transaction.setExecutedAt(transactionDialog.getExecutedAt() == null ? null : transactionDialog.getExecutedAt().atStartOfDay(ZoneOffset.UTC).toInstant());
		transaction.setExpectedDay(transactionDialog.getExpectedDay());
		
		
		for (TagViewModel tag : transactionDialog.getTagsAssigned()) {
			// tag not assigned
			if (transaction.getTags().stream().noneMatch(tagX -> tagX.getId().equals(tag.getId()))) {
				transaction.getTags().add(account.getTags().stream().filter(tagX -> tagX.getId().equals(tag.getId())).findFirst().orElse(null));
			}
		}
		
		Iterator<Tag> tagIt = transaction.getTags().iterator();
		while(tagIt.hasNext()) {
			Tag tag = tagIt.next();
			if (transactionDialog.getTagsAssigned().stream().noneMatch(tagX -> tagX.getId().equals(tag.getId()))) {
				tagIt.remove();
			}
		}
		
		transaction = transactionRepository.save(transaction);

		if (transactionDialog.getId() == null) {
			newMonth.getTransactions().add(transaction);
		}		

		accountingYear = accountingYearRepository.findByAccountIdAndYear(accountId, year);
		yearVm = ToViewModelUtil.toViewModel(accountingYear);
				
		loadedAccountingYearsByAccountId.get(accountId).removeIf(yearX -> yearX.getYear() == year);
		loadedAccountingYearsByAccountId.get(accountId).add(yearVm);
	}

	public void deleteTransaction(Integer transactionId, int accountId, int year) {
		transactionRepository.deleteById(transactionId.longValue());
		
		AccountingYear accountingYear = accountingYearRepository.findByAccountIdAndYear(accountId, year);
		AccountingYearViewModel yearVm = ToViewModelUtil.toViewModel(accountingYear);
				
		loadedAccountingYearsByAccountId.get(accountId).removeIf(yearX -> yearX.getYear() == year);
		loadedAccountingYearsByAccountId.get(accountId).add(yearVm);
		
	}

	public void addDraftsToYearViewModel(int accountId, AccountingYearViewModel modelYear) {
		List<DraftViewModel> drafts = getDraftsForYear(accountId, modelYear.getYear());
		for (int i = 1; i <= 12; i++) {
			YearMonth currentYm = YearMonth.of(modelYear.getYear(), i);
			final int monthNo = i;
			AccountingMonthViewModel month =  modelYear.getMonths().stream().filter(monthX -> monthX.getMonth() == monthNo).findFirst().orElse(null);
			if (month == null) {
				month = new AccountingMonthViewModel();
				month.setAccountYearId(modelYear.getId());
				month.setMonth(i);
				month.setYear(modelYear.getYear());
				modelYear.getMonths().add(month);
			}
			for (DraftViewModel draft : drafts) {
				// check if draft applies
				if (currentYm.compareTo(draft.getStartsAt()) >= 0 && (draft.getEndsAt() == null || currentYm.compareTo(draft.getEndsAt()) <= 0)) {
					if (month.getTransactions().stream().noneMatch(tr -> !tr.isDraft() 
							&& draft.getId().equals(((TransactionViewModel) tr).getDraftId()))) {
						month.getTransactions().add(draft);
					}
				}
			}
			month.recalculateTransactions();
		}
		
	}

}
