package de.bassmech.findra.web.service;

import java.math.BigDecimal;
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
import de.bassmech.findra.core.repository.AccountTransactionDraftRepository;
import de.bassmech.findra.core.repository.AccountTransactionRepository;
import de.bassmech.findra.core.repository.AccountingMonthRepository;
import de.bassmech.findra.core.repository.AccountingYearRepository;
import de.bassmech.findra.model.entity.Account;
import de.bassmech.findra.model.entity.AccountTransaction;
import de.bassmech.findra.model.entity.AccountTransactionDraft;
import de.bassmech.findra.model.entity.AccountingMonth;
import de.bassmech.findra.model.entity.AccountingYear;
import de.bassmech.findra.model.entity.Client;
import de.bassmech.findra.model.entity.Tag;
import de.bassmech.findra.model.entity.TransactionBase;
import de.bassmech.findra.model.statics.Interval;
import de.bassmech.findra.web.service.exception.NotImplementedException;
import de.bassmech.findra.web.util.CalculationUtil;
import de.bassmech.findra.web.util.LocalizationUtil;
import de.bassmech.findra.web.util.ToViewModelUtil;
import de.bassmech.findra.web.view.model.AccountDetailDialogViewModel;
import de.bassmech.findra.web.view.model.AccountViewModel;
import de.bassmech.findra.web.view.model.AccountingMonthViewModel;
import de.bassmech.findra.web.view.model.AccountingYearViewModel;
import de.bassmech.findra.web.view.model.DraftViewModel;
import de.bassmech.findra.web.view.model.TagViewModel;
import de.bassmech.findra.web.view.model.TransactionBaseViewModel;
import de.bassmech.findra.web.view.model.TransactionDetailBaseDialogViewModel;
import de.bassmech.findra.web.view.model.TransactionDetailDialogViewModel;
import de.bassmech.findra.web.view.model.TransactionDraftDetailDialogViewModel;
import de.bassmech.findra.web.view.model.type.AccountType;

@Service
public class AccountService {
	protected Logger logger = LoggerFactory.getLogger(AccountService.class);

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private AccountingYearRepository accountingYearRepository;

	@Autowired
	private AccountingMonthRepository accountingMonthRepository;

//	@Autowired
//	private AccountTransactionRepository tagRepository;

	@Autowired
	private AccountTransactionRepository transactionRepository;

	@Autowired
	private AccountTransactionDraftRepository draftRepository;

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
			}
			if (yearModel != null) {
				addDraftsToYearViewModel(accountId, yearModel);
			}
		}

		return yearModel;
	}

	public List<DraftViewModel> getDraftsForYear(Integer accountId, int year) {
		// drafts
		reloadDraftsByAccountId(accountId, false);

		return loadedDraftsByAccountId.get(accountId).stream().filter(draft -> draft.getStartsAt().getYear() <= year
				&& (draft.getEndsAt() == null || draft.getEndsAt().getYear() >= year)).toList();
	}

	public Map<Integer, String> getAccountTypes(Locale locale) {
		Map<Integer, String> result = accountTypesByLocale.get(locale);
		if (result == null) {
			result = new HashMap<>();
			for (AccountType entry : AccountType.values()) {
				result.put(entry.getDbValue(), LocalizationUtil.getTag(entry.getTagKey(), locale));
			}
			accountTypesByLocale.put(locale, result);
		}
		return result;
	}

	public AccountViewModel saveAccount(Client client, AccountDetailDialogViewModel accountDialog) {
		Account account;
		if (accountDialog.getId() == null) {
			account = new Account();
		} else {
			account = accountRepository.findById(accountDialog.getId().longValue()).get();
		}
		account.setTitle(accountDialog.getTitle());
		account.setDescription(accountDialog.getDescription());
		account.setStartingYear(accountDialog.getStartingYear());
		account.setClient(client);
		account.setCreatedAt(Instant.now());
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
			accountVm = ToViewModelUtil.toViewModel(account);
			loadedAccounts.add(accountVm);
		} else {
			int accountIndex = loadedAccounts.indexOf(accountVm);
			loadedAccounts.set(accountIndex, ToViewModelUtil.toViewModel(account));
		}
		return accountVm;
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

	public void saveTransaction(TransactionDetailDialogViewModel transactionDialog) {
		int year = transactionDialog.getSelectedAccountingYear();
		int month = transactionDialog.getSelectedAccountingMonth();
		logger.debug(String.format("saveTransactionBase: with dialog: %s", transactionDialog.toString()));
		Integer accountId = transactionDialog.getAccountId();
		Account account = accountRepository.findById(accountId.longValue()).get();
		boolean valueChanged = false;

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
			accountingYear = accountingYearRepository.findByAccountIdAndYear(accountId, year);
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
		if (!transactionDialog.isDraft() && transactionDialog.getId() != null) {
			transaction = newMonth.getTransactions().stream().filter(tr -> tr.getId() == transactionDialog.getId())
					.findFirst().orElse(null);
			if (transaction == null) {
				transaction = transactionRepository.findById(transactionDialog.getId().longValue()).orElse(null);
			}
			if (transaction.getAccountingMonth().getMonth() != transactionDialog.getSelectedAccountingMonth()
					|| transaction.getAccountingMonth().getAccountingYear().getYear() != transactionDialog
							.getSelectedAccountingYear()) {
				AccountingMonth oldMonth = transaction.getAccountingMonth();
				oldMonth.getTransactions().remove(transaction);
				accountingMonthRepository.save(oldMonth);
				newMonth.getTransactions().add(transaction);
			}

			if (transaction.getValue().compareTo(transactionDialog.getValue()) != 0) {
				valueChanged = true;
			}
		} else {
			transaction = new AccountTransaction();
			transaction.setCreatedAt(Instant.now());
			transaction.setAccountingMonth(newMonth);
			valueChanged = true;
			if (transactionDialog.isDraft()) {
				AccountTransactionDraft draft = draftRepository.findById(transactionDialog.getId().longValue())
						.orElseGet(null);
				transaction.setDraft(draft);
			}
		}

		transaction.setTitle(transactionDialog.getTitle());
		transaction.setDescription(transactionDialog.getDescription());
		transaction.setValue(transactionDialog.getValue());
		transaction.setExecutedAt(transactionDialog.getExecutedAt() == null ? null
				: transactionDialog.getExecutedAt().atStartOfDay(ZoneOffset.UTC).toInstant());
		transaction.setExpectedDay(transactionDialog.getExpectedDay());

		rebuildTransactionTags(transactionDialog, account, transaction);

		transaction = transactionRepository.save(transaction);

		if (transactionDialog.isDraft() || transactionDialog.getId() == null) {
			newMonth.getTransactions().add(transaction);
		}

		// TODO merge with "recalculateAndSaveAccountingYear" ?
		accountingYear = accountingYearRepository.findByAccountIdAndYear(accountId, year);
		yearVm = ToViewModelUtil.toViewModel(accountingYear);

		loadedAccountingYearsByAccountId.get(accountId).removeIf(yearX -> yearX.getYear() == year);
		loadedAccountingYearsByAccountId.get(accountId).add(yearVm);

		if (valueChanged) {
			recalculateAndSaveAccountingYear(accountId, year);
		}
	}

	private void rebuildTransactionTags(TransactionDetailBaseDialogViewModel transactionDialog, Account account,
			TransactionBase transaction) {
		for (TagViewModel tag : transactionDialog.getTagsAssigned()) {
			// tag not assigned
			if (transaction.getTags().stream().noneMatch(tagX -> tagX.getId().equals(tag.getId()))) {
				transaction.getTags().add(account.getTags().stream().filter(tagX -> tagX.getId().equals(tag.getId()))
						.findFirst().orElse(null));
			}
		}

		Iterator<Tag> tagIt = transaction.getTags().iterator();
		while (tagIt.hasNext()) {
			Tag tag = tagIt.next();
			if (transactionDialog.getTagsAssigned().stream().noneMatch(tagX -> tagX.getId().equals(tag.getId()))) {
				tagIt.remove();
			}
		}
	}

	private void recalculateAndSaveAccountingYear(int accountId, int year) {
		logger.debug(String.format("recalculateAndSaveAccountingYear: id: %d year: %d", accountId, year));
		AccountingYearViewModel yearVm = loadedAccountingYearsByAccountId.get(accountId).stream()
				.filter(yearX -> yearX.getYear() == year).findFirst().orElse(null);

		yearVm.recalculateTransactionSum();
		AccountingYear accountingYear = accountingYearRepository.findByAccountIdAndYear(accountId, year);
		if (accountingYear != null) {
			accountingYear.setStartValue(yearVm.getStartValue());
			accountingYear.setClosingValue(yearVm.getTransactionSum());

			for (AccountingMonth month : accountingYear.getMonths()) {
				AccountingMonthViewModel monthVm = yearVm.getMonths().stream()
						.filter(monthX -> monthX.getMonth() == month.getMonth()).findFirst().orElse(null);
				if (monthVm != null) {
					month.setStartValue(monthVm.getStartValue());
					month.setClosingValue(monthVm.getClosingValue());
				}
			}

			accountingYear = accountingYearRepository.save(accountingYear);
			yearVm = ToViewModelUtil.toViewModel(accountingYear);
			// TODO add drafts ?

			loadedAccountingYearsByAccountId.get(accountId).removeIf(yearX -> yearX.getYear() == year);
			loadedAccountingYearsByAccountId.get(accountId).add(yearVm);
		}
		// TODO all existing after
	}

	public void deleteTransaction(Integer transactionId, int accountId) {
		AccountTransaction transaction = transactionRepository.findById(transactionId.longValue()).orElse(null);

		AccountingYear accountingYear = accountingYearRepository.findByAccountIdAndYear(accountId,
				transaction.getAccountingMonth().getAccountingYear().getId());
		AccountingYearViewModel yearVm = ToViewModelUtil.toViewModel(accountingYear);

		transactionRepository.delete(transaction);

		loadedAccountingYearsByAccountId.get(accountId).removeIf(yearX -> yearX.getYear() == accountingYear.getYear());
		loadedAccountingYearsByAccountId.get(accountId).add(yearVm);

	}

	public void addDraftsToYearViewModel(int accountId, AccountingYearViewModel modelYear) {
		List<DraftViewModel> drafts = getDraftsForYear(accountId, modelYear.getYear());
		AccountingMonthViewModel previousMonth = null;
		for (int i = 1; i <= 12; i++) {
			YearMonth currentYm = YearMonth.of(modelYear.getYear(), i);
			final int monthNo = i;
			AccountingMonthViewModel month = modelYear.getMonths().stream()
					.filter(monthX -> monthX.getMonth() == monthNo).findFirst().orElse(null);
			if (month == null) {
				month = new AccountingMonthViewModel();
				month.setAccountYearId(modelYear.getId());
				month.setMonth(i);
				month.setYear(modelYear.getYear());
				modelYear.getMonths().add(month);
			}
			for (DraftViewModel draft : drafts) {
				// check if draft applies
				if (isDraftApplicable(draft, month.getYear(), month.getMonth())) {
					month.getTransactions().add(draft);
				}
//				if (currentYm.compareTo(draft.getStartsAt()) >= 0
//						&& (draft.getEndsAt() == null || currentYm.compareTo(draft.getEndsAt()) <= 0)) {
//					if (month.getTransactions().stream().noneMatch(
//							tr -> !tr.isDraft() && draft.getId().equals(((TransactionViewModel) tr).getDraftId()))) {
//						month.getTransactions().add(draft);
//					}
//				}
			}
			month.setStartValue(previousMonth == null ? modelYear.getStartValue() : previousMonth.getClosingValue());
			month.recalculateTransactions();
			previousMonth = month;
		}
		modelYear.recalculateTransactionSum();
	}

	public AccountingYearViewModel getOrCreateAccountingYear(int accountId, int requestedYear) {
		logger.debug(
				String.format("getOrCreateAccountingYear accountId: %d requestedYear: %d", accountId, requestedYear));

		if (!loadedAccountingYearsByAccountId.containsKey(accountId)) {
			if (loadedAccounts.stream().anyMatch(acc -> acc.getId() == accountId)) {
				loadedAccountingYearsByAccountId.put(accountId, new ArrayList<>());
			} else {
				throw new IllegalArgumentException("Account not found");
			}
		}

		AccountingYearViewModel modelYear = loadedAccountingYearsByAccountId.get(accountId).stream()
				.filter(year -> year.getYear() == requestedYear).findFirst().orElse(null);
		if (modelYear == null) {
			modelYear = getAccountYear(accountId, requestedYear);
			if (modelYear != null) {
				loadedAccountingYearsByAccountId.get(accountId).add(modelYear);
			}
		}
		if (modelYear == null) {
			logger.debug(String.format("Creating new year %d for account with id: %d", requestedYear, accountId));

			BigDecimal lastYearClosingValue = BigDecimal.ZERO;
			if (requestedYear > loadedAccounts.stream().filter(acc -> acc.getId() == accountId).findFirst().orElse(null)
					.getStartingYear()) {
				AccountingYearViewModel previousModelYear = loadedAccountingYearsByAccountId.get(accountId).stream()
						.filter(year -> year.getYear() == requestedYear - 1).findFirst().orElse(null);
				if (previousModelYear == null) {
					previousModelYear = getOrCreateAccountingYear(accountId, requestedYear - 1);
				}
				lastYearClosingValue = previousModelYear.getTransactionSum();
			}

			modelYear = new AccountingYearViewModel();
			modelYear.setAccountId(accountId);
			modelYear.setYear(requestedYear);
			loadedAccountingYearsByAccountId.get(accountId).add(modelYear);
			modelYear.addDraftMonths();
			modelYear.setStartValue(lastYearClosingValue);
			addDraftsToYearViewModel(accountId, modelYear);

			modelYear.recalculateTransactionSum();
		}
		return modelYear;
	}

	public void saveDraft(TransactionDraftDetailDialogViewModel dialogVm) {
		Account account = accountRepository.findById(dialogVm.getAccountId().longValue()).orElse(null);
		AccountTransactionDraft draft = null;
		if (dialogVm.getId() != null) {
			draft = draftRepository.findById(dialogVm.getId().longValue()).orElse(null);
		} else {
			draft = new AccountTransactionDraft();
			draft.setAccount(account);
			draft.setCreatedAt(Instant.now());

		}
		draft.setTitle(dialogVm.getTitle());
		draft.setDescription(dialogVm.getDescription());
		draft.setStartsAt(YearMonth.of(dialogVm.getSelectedStartYear(), dialogVm.getSelectedStartMonth()));
		draft.setInterval(Interval.fromDbValue(dialogVm.getSelectedInterval()));
		draft.setExpectedDay(dialogVm.getExpectedDay());
		draft.setValue(dialogVm.getValue());

		if (dialogVm.getSelectedEndMonth() != null && dialogVm.getSelectedEndYear() != null) {
			draft.setEndsAt(YearMonth.of(dialogVm.getSelectedEndYear(), dialogVm.getSelectedEndMonth()));
		}

		rebuildTransactionTags(dialogVm, account, draft);

		draft = draftRepository.save(draft);

		if (dialogVm.getId() != null) {
			loadedDraftsByAccountId.get(dialogVm.getAccountId()).removeIf(draftX -> draftX.getId() == dialogVm.getId());
		}

		DraftViewModel draftVm = ToViewModelUtil.toViewModel(draft);
		loadedDraftsByAccountId.get(dialogVm.getAccountId()).add(draftVm);

		replaceOrAddDraftOnLoadedYears(draftVm, account.getId());
	}

	private void reloadDraftsByAccountId(Integer accountId, boolean forced) {
		if (forced || loadedDraftsByAccountId.get(accountId) == null) {
			Account account = accountRepository.findById(accountId.longValue()).orElse(null);
			loadedDraftsByAccountId.put(accountId, ToViewModelUtil.toDraftViewModelList(account.getDrafts()));
			logger.debug(String.format("Loaded %d drafts for account with id: %d",
					loadedDraftsByAccountId.get(accountId).size(), accountId));
		}
	}

	public List<DraftViewModel> getLoadedDraftsByAccountId(Integer accountId) {
		reloadDraftsByAccountId(accountId, false);
		return loadedDraftsByAccountId.get(accountId);
	}

	private void replaceOrAddDraftOnLoadedYears(DraftViewModel draftVm, int accountId) {
		List<AccountingYearViewModel> yearVms = loadedAccountingYearsByAccountId.get(accountId);
		if (yearVms != null) {
			for (AccountingYearViewModel yearVm : yearVms) {
				boolean oldDraftFound = false;
				boolean newDraftApplied = false;
				for (AccountingMonthViewModel monthVm : yearVm.getMonths()) {
					Iterator<TransactionBaseViewModel> transactionIt = monthVm.getTransactions().iterator();
					while (transactionIt.hasNext()) {
						TransactionBaseViewModel transactionVm = transactionIt.next();
						if (transactionVm.isDraft() && transactionVm.getId().equals(draftVm.getId())) {
							oldDraftFound = true;
							transactionIt.remove();
						}
					}
					if (isDraftApplicable(draftVm, monthVm.getYear(), monthVm.getMonth())) {
						newDraftApplied = true;
						monthVm.getTransactions().add(draftVm);
					}
				}
				if (oldDraftFound || newDraftApplied) {
					yearVm.recalculateTransactionSum();
				}
			}
		}
	}

	private boolean isDraftApplicable(DraftViewModel draftVm, int year, int month) {
		YearMonth yearMonth = YearMonth.of(year, month);
		if (yearMonth.compareTo(draftVm.getStartsAt()) >= 0
				&& (draftVm.getEndsAt() == null || yearMonth.compareTo(draftVm.getEndsAt()) <= 0)) {
			switch (draftVm.getInterval()) {
			case EVERY_MONTH:
				return true;
			case EVERY_OTHER_MONTH:
				return draftVm.getStartsAt().getMonth().getValue() % 2 == month % 2;
			case EVERY_THIRD_MONTH:
			case EVERY_FOURTH_MONTH:
			case EVERY_FIFTH_MONTH:
			case EVERY_SIXTH_MONTH:
			case EVERY_SEVENTH_MONTH:
			case EVERY_EIGHTH_MONTH:
			case EVERY_NINTH_MONTH:
			case EVERY_TENTH_MONTH:
			case EVERY_ELEVENTH_MONTH:
			case EVERY_YEAR:
				return CalculationUtil.getYearMonthDiffInMonth(draftVm.getStartsAt(), yearMonth) % draftVm.getInterval().getDbValue() == 0;
			default:
				throw new NotImplementedException("Interval", draftVm.getInterval().name());
			}
		}

		return false;
	}

	public DraftViewModel getDraftViewModelById(Integer draftId, int accountId) {
		return getLoadedDraftsByAccountId(accountId).stream().filter(draft -> draft.getId().equals(draftId)).findFirst()
				.orElse(null);
	}

}
