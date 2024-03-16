package de.bassmech.findra.web.view;

import java.math.BigDecimal;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.bassmech.findra.model.statics.ConfigurationCode;
import de.bassmech.findra.model.statics.ExpectedDay;
import de.bassmech.findra.web.handler.FacesMessageHandler;
import de.bassmech.findra.web.service.AccountService;
import de.bassmech.findra.web.service.ConfigurationService;
import de.bassmech.findra.web.service.SettingService;
import de.bassmech.findra.web.service.TagService;
import de.bassmech.findra.web.util.FormatterUtil;
import de.bassmech.findra.web.util.LocalizedMessageUtil;
import de.bassmech.findra.web.util.statics.FormIds;
import de.bassmech.findra.web.util.statics.TagName;
import de.bassmech.findra.web.view.model.AccountDetailDialogViewModel;
import de.bassmech.findra.web.view.model.AccountViewModel;
import de.bassmech.findra.web.view.model.AccountingMonthViewModel;
import de.bassmech.findra.web.view.model.AccountingYearViewModel;
import de.bassmech.findra.web.view.model.DraftViewModel;
import de.bassmech.findra.web.view.model.TagViewModel;
import de.bassmech.findra.web.view.model.TransactionBaseViewModel;
import de.bassmech.findra.web.view.model.TransactionDetailDialogViewModel;
import de.bassmech.findra.web.view.model.TransactionExecutedDialogViewModel;
import de.bassmech.findra.web.view.model.TransactionViewModel;
import de.bassmech.findra.web.view.model.type.AccountTransactionLayout;
import de.bassmech.findra.web.view.model.type.AccountType;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.view.ViewScoped;

@Component
@ViewScoped
public class AccountView {
	private Logger logger = LoggerFactory.getLogger(AccountView.class);

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private SettingService settingService;
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private ConfigurationService configurationService;
		
	private List<AccountViewModel> selectableAccounts = new ArrayList<>();
	private AccountViewModel selectedAccount;
	private Integer selectedAccountId;

	private List<AccountingYearViewModel> accountingYears = new ArrayList<>();
	private TransactionBaseViewModel selectedTransaction;

	private TreeMap<Integer, String> selectableMonths = new TreeMap<>();
	private int selectedMonth;

	private List<Integer> selectableYears = new ArrayList<>();
	private int selectedYear;
	
	private AccountDetailDialogViewModel accountDialog = new AccountDetailDialogViewModel("");
	private TransactionDetailDialogViewModel transactionDialog = new TransactionDetailDialogViewModel("", false);
	private TransactionExecutedDialogViewModel transactionExecutedDialog = new TransactionExecutedDialogViewModel();
	
	private AccountingMonthViewModel firstAccountingMonth = null;
	private AccountingMonthViewModel secondAccountingMonth = null;
	private AccountingMonthViewModel thirdAccountingMonth = null;
	
	private HashMap<Integer, String> selectableExpectedDay = new LinkedHashMap<>();
	
	private int[] yearRange = new int[2];
	
	private AccountTransactionLayout transactionLayout = AccountTransactionLayout.COLUMN_SINGLE;
	
	@PostConstruct
	public void init() {
		logger.debug("init called");
		
		reloadSelectableAccounts();

		selectedMonth = YearMonth.now().getMonthValue();

		// TODO add years from account
		int startYear = Year.now().getValue();
		selectedYear = startYear;

		if (selectableAccounts.size() > 0) {
			accountingYears.add(accountService.getAccountYear(selectableAccounts.get(0).getId(), startYear));
//			firstAccountingMonth = accountingYears.get(0).getMonths().stream()
//					.filter(month -> month.getMonth() == selectedMonth).findFirst().orElse(null);
			selectedAccount = selectableAccounts.get(0);
			selectedAccountId = selectedAccount.getId();
			updateMonthTransactions();
		}
		
		for (int i = 1; i <= 12; i++) {
			selectableMonths.put(i, java.time.Month.of(i).getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()));
		}
		
		yearRange[0] = Integer.parseInt(configurationService.getByCode(ConfigurationCode.YEAR_RANGE_MIN));
		yearRange[1] = Integer.parseInt(configurationService.getByCode(ConfigurationCode.YEAR_RANGE_MAX));
		
		for (int i = yearRange[0]; i <= yearRange[1]; i++) {
			selectableYears.add(i);
		}
		
		setSelectableExpectedDay();
	}
	
	private void setSelectableExpectedDay() {
		selectableExpectedDay = new LinkedHashMap<>();
		selectableExpectedDay.put(ExpectedDay.UNKNOWN.getDbValue(), LocalizedMessageUtil.getTag(ExpectedDay.UNKNOWN.getTagString()));
		selectableExpectedDay.put(ExpectedDay.ULTIMO.getDbValue(), LocalizedMessageUtil.getTag(ExpectedDay.ULTIMO.getTagString()));
		int maxDayInMonth = 31;
		for (int i = 1; i <= maxDayInMonth; i++) {
			selectableExpectedDay.put(i, String.valueOf(i));
		}
	}
	
	private void reloadSelectableAccounts() {
		selectableAccounts = accountService.getAccountList();
	}

	public String getTransactionRowColorString(TransactionBaseViewModel vm) {
		if (BigDecimal.ZERO.compareTo(vm.getValue()) > 0) {
			return vm instanceof DraftViewModel || ((TransactionViewModel) vm).getExecutedAt() == null ? "transaction-negative-expected" : "transaction-negative-executed";
		} else if (BigDecimal.ZERO.compareTo(vm.getValue()) < 0) {	
			return vm instanceof DraftViewModel || ((TransactionViewModel) vm).getExecutedAt() == null ? "transaction-positive-expected" : "transaction-positive-executed" ;
		} else {
			return null;
		}
	}
	
	public void onAccountChanged() {
		logger.debug("onAccountChanged");
		
		selectedAccount = selectableAccounts.stream().filter(acc -> acc.getId().equals(selectedAccountId)).findFirst().orElse(null);
		
		if (selectedYear > selectedAccount.getStartingYear()) {
			selectedYear = selectedAccount.getStartingYear();
		}
		accountingYears.clear();
		accountingYears.add(accountService.getAccountYear(selectedAccountId, selectedYear));

		updateMonthTransactions();
	}
	public void onNextYear() {
		logger.debug("onNextYear");
		if (selectedYear + 1 > yearRange[1]) {
			FacesMessageHandler.addMessage(FacesMessage.SEVERITY_WARN, LocalizedMessageUtil.getMessage("hint", Locale.getDefault())
					, LocalizedMessageUtil.getMessage("year.max.reached", Locale.getDefault(), yearRange[1]));
			return;
		}
		selectedYear++;
		updateMonthTransactions();
	}
	
	public void onPreviousYear() {
		logger.debug("onPreviousYear");
		if (selectedYear - 1 < yearRange[0]) {
			FacesMessageHandler.addMessage(FacesMessage.SEVERITY_WARN, LocalizedMessageUtil.getMessage("hint", Locale.getDefault())
					, LocalizedMessageUtil.getMessage("year.min.reached", Locale.getDefault(), yearRange[0]));
			return;
		}
		selectedYear--;
		updateMonthTransactions();
	}
	
	public void onYearChanged() {
		logger.debug("Year was changed to: " + selectedYear);
		updateMonthTransactions();
	}

	public void onMonthChanged() {
		logger.debug("Month was changed to: " + selectedMonth);
		updateMonthTransactions();
	}

	public void onNextMonthClick() {
		logger.debug("onNextMonthClick: ");
		if (selectedMonth == 12) {
			if (selectedYear >= yearRange[1]) {
				logger.debug("Max year reached");
				FacesMessageHandler.addMessage(FacesMessage.SEVERITY_WARN, LocalizedMessageUtil.getMessage("hint", Locale.getDefault())
						, LocalizedMessageUtil.getMessage("year.max.reached", Locale.getDefault(), yearRange[1]));
				return;
			}
			selectedYear++;
			selectedMonth = 1;
		} else {
			selectedMonth++;
		}
		logger.debug("Month was changed to: " + selectedMonth);
		updateMonthTransactions();
	}
	
	public void onPreviousMonthClick() {
		if (selectedMonth == 1) {
			if (selectedYear <= yearRange[0]) {
				logger.debug("Min year reached");
				FacesMessageHandler.addMessage(FacesMessage.SEVERITY_WARN, LocalizedMessageUtil.getMessage("hint", Locale.getDefault())
						, LocalizedMessageUtil.getMessage("year.min.reached", Locale.getDefault(), yearRange[0]));
				return;
			}
			selectedYear--;
			selectedMonth = 12;
		} else {
			selectedMonth--;
		}
		logger.debug("Month was changed to: " + selectedMonth);
		updateMonthTransactions();
	}
	
	private void updateMonthTransactions() {
		logger.debug("updateMonthTransactions");
		AccountingYearViewModel modelYear = getOrCreateAccountingYear(selectedYear);
		
		firstAccountingMonth = modelYear.getMonths().stream().filter(month -> month.getMonth() == selectedMonth).findFirst().orElse(null);
		
		int secondMonthNumber = selectedMonth + 1;
		if (selectedMonth + 1 == 13) {
			modelYear = getOrCreateAccountingYear(selectedYear + 1);
			secondMonthNumber = 1;
		}
		final int finalSecondMonthNumber = secondMonthNumber;
		secondAccountingMonth = modelYear.getMonths().stream().filter(month -> month.getMonth() == finalSecondMonthNumber).findFirst().orElse(null);
		if (secondAccountingMonth == null) {
			secondAccountingMonth = new AccountingMonthViewModel();
			secondAccountingMonth.setAccountYearId(selectedAccountId);
			secondAccountingMonth.setMonth(finalSecondMonthNumber);
			secondAccountingMonth.setYear(modelYear.getYear());
		}
		
		int thirdMonthNumber = selectedMonth + 2;
		if (selectedMonth + 2 == 13) {
			modelYear = getOrCreateAccountingYear(selectedYear + 1);
			thirdMonthNumber = 1;
		} else if (selectedMonth + 2 == 14) {
			thirdMonthNumber = 2;
		}
		final int finalThirdMonthNumber = thirdMonthNumber;
		thirdAccountingMonth = modelYear.getMonths().stream().filter(month -> month.getMonth() == finalThirdMonthNumber).findFirst().orElse(null);
		if (thirdAccountingMonth == null) {
			thirdAccountingMonth = new AccountingMonthViewModel();
			thirdAccountingMonth.setAccountYearId(selectedAccountId);
			thirdAccountingMonth.setMonth(finalThirdMonthNumber);
			thirdAccountingMonth.setYear(modelYear.getYear());
		}
	}
	
	private AccountingYearViewModel getOrCreateAccountingYear(int requestedYear) {	
		AccountingYearViewModel modelYear = accountingYears.stream().filter(year -> year.getYear() == requestedYear).findFirst().orElse(null);
		if (modelYear == null) {
			modelYear = accountService.getAccountYear(selectedAccountId, requestedYear);
			if (modelYear != null) {
				accountingYears.add(modelYear);
			}
		}
		if (modelYear == null) {
			logger.debug(String.format("Creating new year for account with id: %d", selectedAccount.getId()));
			
			BigDecimal lastYearClosingValue = BigDecimal.ZERO;
			if (requestedYear > selectedAccount.getStartingYear()) {
//				//TODO fill not existing
				AccountingYearViewModel previousModelYear = accountingYears.stream().filter(year -> year.getYear() == requestedYear - 1).findFirst().orElse(null);
				if (previousModelYear == null) {
					previousModelYear = getOrCreateAccountingYear(requestedYear - 1);
					lastYearClosingValue = previousModelYear.getTransactionSum();
				}
			}
			
			modelYear = new AccountingYearViewModel();
			modelYear.setAccountId(selectedAccount.getId());
			modelYear.setYear(requestedYear);
			accountingYears.add(modelYear);
			modelYear.addDraftMonths();
			modelYear.setStartValue(lastYearClosingValue);
			accountService.addDraftsToYearViewModel(selectedAccount.getId(), modelYear);
			
			modelYear.recalculateTransactionSum();		
		}
		return modelYear;
	}



	public void onDeleteTransaction() {
		logger.debug("onDeleteTransaction: " + transactionDialog.getId());
		
		accountService.deleteTransaction(transactionDialog.getId(), transactionDialog.getAccountId(), transactionDialog.getAccountingMonth().getYear());
		reloadAccountingYear();
		PrimeFaces.current().ajax().update(FormIds.MAIN_FORM.getValue());
	}
	
	public void openAccountDetailDialogNew() {
		logger.debug("openAccountDetailDialogNew");
		accountDialog = new AccountDetailDialogViewModel(LocalizedMessageUtil.getTag(TagName.ACCOUNT_NEW.getValue()));	
		
		PrimeFaces.current().ajax().update("@form");
		PrimeFaces.current().executeScript("PF('accountDetailDialog').show()");
	}
	
	public void openAccountDetailDialogEdit() {
		logger.debug("openAccountDetailDialogEdit");
		accountDialog = new AccountDetailDialogViewModel(LocalizedMessageUtil.getTag(TagName.ACCOUNT_EDIT.getValue()));	
		accountDialog.setId(selectedAccount.getId());
		accountDialog.setTitle(selectedAccount.getTitle());
		accountDialog.setDescription(selectedAccount.getDescription());
		accountDialog.setType(AccountType.TRANSCATION.getDbValue());
		
		PrimeFaces.current().ajax().update(FormIds.MAIN_FORM.getValue());
		PrimeFaces.current().executeScript("PF('accountDetailDialog').show()");
	}
	
	private boolean isAccountDialogValid() {
		boolean isValid = true;
		if (accountDialog.getTitle() == null || accountDialog.getTitle().isBlank()) {
			FacesMessageHandler.addMessage(FacesMessage.SEVERITY_ERROR, LocalizedMessageUtil.getMessage("error", Locale.getDefault())
					, LocalizedMessageUtil.getMessage("error.title.must.not.be.empty", Locale.getDefault()));
			isValid = false;
		}
		if (accountDialog.getStartingYear() < yearRange[0] || accountDialog.getStartingYear() > yearRange[1]) {
			FacesMessageHandler.addMessage(FacesMessage.SEVERITY_ERROR, LocalizedMessageUtil.getMessage("error", Locale.getDefault())
					, LocalizedMessageUtil.getMessage("error.starting.year.must.be.in.range", Locale.getDefault(), yearRange[0], yearRange[1]));
			isValid = false;
		}
		return isValid;
	}
	
	public void closeDialogAndSaveAccount() {
		logger.debug("Saving account");
		
		if (isAccountDialogValid()) {
			accountService.saveAccount(accountDialog);
			reloadSelectableAccounts();
			PrimeFaces.current().ajax().update(FormIds.MAIN_FORM.getValue());
		}
	}
	
	public void deleteAccount() {
		logger.debug("Deleting account with id: " + accountDialog.getId());
		accountService.deleteAccount(accountDialog.getId());
		reloadSelectableAccounts();
		PrimeFaces.current().executeScript("PF('accountDetailDialog').hide()");
		PrimeFaces.current().ajax().update(FormIds.MAIN_FORM.getValue());
	}

///
/// Draft related
///
	public void onDraftEdit(int draftId, AccountingMonthViewModel accountingMonth) {
		logger.debug("onDraftEdit id: " + draftId);
		
		//prepareAndOpenTransactionDetailDialogForDraft(draftId, accountingMonth);
	}
		
	public void onDraftNew(AccountingMonthViewModel accountingMonth) {
		logger.debug("onDraftNew");

		prepareAndOpenTransactionDetailDialogForDraft(null, accountingMonth);
	}
	
	private void prepareAndOpenTransactionDetailDialogForDraft(Integer draftId, AccountingMonthViewModel accountingMonth) {
		transactionDialog = new TransactionDetailDialogViewModel(LocalizedMessageUtil.getTag(draftId == null ? TagName.DRAFT_NEW.getValue() : TagName.DRAFT_EDIT.getValue()), true);	
		transactionDialog.setAccountingMonth(accountingMonth);
		//TODO GOON
		if (draftId == null) {
			transactionDialog.getTagsAvailable().addAll(tagService.getTagsForAccount(selectedAccountId, false));
		} else {
			TransactionBaseViewModel vm = accountingMonth.getTransactions().stream().filter(tr -> tr.getId().equals(draftId)).findFirst().orElse(null);
//			transactionDialog.setId(vm.getId());
//			transactionDialog.setAccountId(selectedAccountId);
//			transactionDialog.setExecutedAt(vm.getExecutedAt() == null ? null : vm.getExecutedAt().atZone(ZoneOffset.UTC).toLocalDate());
//			transactionDialog.setExpectedDay(vm.getExpectedDay());
//			transactionDialog.setTitle(vm.getTitle());
//			transactionDialog.setDescription(vm.getDescription());
//			transactionDialog.setValue(vm.getValue());
//			
//			List<TagViewModel> tagsForAccount = tagService.getTagsForAccount(selectedAccountId, false);
//			for (TagViewModel tag : tagsForAccount) {
//				if (vm.getTags().contains(tag)) {
//					transactionDialog.getTagsAssigned().add(tag);
//				} else {
//					transactionDialog.getTagsAvailable().add(tag);
//				}
//			}
		}
		
//		int maxDay = YearMonth.of(accountingMonth.getYear(), accountingMonth.getMonth()).atEndOfMonth().getDayOfMonth();
//		transactionDialog.setSelectableExpectedDay(selectableExpectedDay.entrySet().stream()
//				.filter(entry -> entry.getKey() <= maxDay || entry.getKey() > 31)
//				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> {throw new RuntimeException();}, LinkedHashMap::new)));
//		//transactionDialog.setSelectableExpectedDay(selectableExpectedDay.sub (transactionId));
//		
//		PrimeFaces.current().ajax().update(FormIds.MAIN_FORM.getValue());
//		PrimeFaces.current().executeScript("PF('transactionDetailDialog').show()");
	}
	
///
/// Transaction related
///
	
	private boolean isTransactionDialogValid() {
		boolean isValid = true;
		if (transactionDialog.getTitle() == null || transactionDialog.getTitle().isBlank()) {
			FacesMessageHandler.addMessage(FacesMessage.SEVERITY_ERROR, LocalizedMessageUtil.getMessage("error", Locale.getDefault())
					, LocalizedMessageUtil.getMessage("error.title.must.not.be.empty", Locale.getDefault()));
			isValid = false;
		}

		return isValid;
	}
	
	public void onTransactionEdit(int transactionId, AccountingMonthViewModel accountingMonth) {
		logger.debug("onTransactionEdit id: " + transactionId);
		
		prepareAndOpenTransactionDetailDialogForTransaction(transactionId, accountingMonth);
	}
		
	public void onTransactionNew(AccountingMonthViewModel accountingMonth) {
		logger.debug("onTransactionNew");

		prepareAndOpenTransactionDetailDialogForTransaction(null, accountingMonth);
	}
	
	private void prepareAndOpenTransactionDetailDialogForTransaction(Integer transactionId, AccountingMonthViewModel accountingMonth) {
		transactionDialog = new TransactionDetailDialogViewModel(LocalizedMessageUtil.getTag(transactionId == null ? TagName.TRANSACTION_NEW.getValue() : TagName.TRANSACTION_EDIT.getValue()), false);	
		transactionDialog.setAccountingMonth(accountingMonth);
		
		if (transactionId == null) {
			transactionDialog.getTagsAvailable().addAll(tagService.getTagsForAccount(selectedAccountId, false));
		} else {
			TransactionBaseViewModel vmb = accountingMonth.getTransactions().stream().filter(tr -> !tr.isDraft() && tr.getId().equals(transactionId)).findFirst().orElse(null);
			TransactionViewModel vm = (TransactionViewModel) vmb;
			transactionDialog.setId(vm.getId());
			transactionDialog.setAccountId(selectedAccountId);
			transactionDialog.setExecutedAt(vm.getExecutedAt() == null ? null : vm.getExecutedAt().atZone(ZoneOffset.UTC).toLocalDate());
			transactionDialog.setExpectedDay(vm.getExpectedDay());
			transactionDialog.setTitle(vm.getTitle());
			transactionDialog.setDescription(vm.getDescription());
			transactionDialog.setValue(vm.getValue());
			
			List<TagViewModel> tagsForAccount = tagService.getTagsForAccount(selectedAccountId, false);
			for (TagViewModel tag : tagsForAccount) {
				if (vm.getTags().contains(tag)) {
					transactionDialog.getTagsAssigned().add(tag);
				} else {
					transactionDialog.getTagsAvailable().add(tag);
				}
			}
		}
		
		int maxDay = YearMonth.of(accountingMonth.getYear(), accountingMonth.getMonth()).atEndOfMonth().getDayOfMonth();
		transactionDialog.setSelectableExpectedDay(selectableExpectedDay.entrySet().stream()
				.filter(entry -> entry.getKey() <= maxDay || entry.getKey() > 31)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> {throw new RuntimeException();}, LinkedHashMap::new)));
		//transactionDialog.setSelectableExpectedDay(selectableExpectedDay.sub (transactionId));
		
		PrimeFaces.current().ajax().update(FormIds.MAIN_FORM.getValue());
		PrimeFaces.current().executeScript("PF('transactionDetailDialog').show()");
	}
		
	public void closeDialogAndSaveTransaction() {
		logger.debug("Saving transaction");
		
		if (isTransactionDialogValid()) {
			transactionDialog.setAccountId(selectedAccountId);
			
			accountService.saveTransactionBase(transactionDialog, transactionDialog.getAccountingMonth().getYear(), transactionDialog.getAccountingMonth().getMonth());
			reloadAccountingYear();
			PrimeFaces.current().ajax().update(FormIds.MAIN_FORM.getValue());
		}
	}
	
	public void reloadAccountingYear() {
		accountingYears.removeIf(yearX -> yearX.getYear() == selectedYear);
		getOrCreateAccountingYear(selectedYear);
		
		if (selectedMonth >= 11) {
			accountingYears.removeIf(yearX -> yearX.getYear() == selectedYear + 1);
			accountingYears.add(accountService.getAccountYear(selectedAccountId, selectedYear + 1));
		}
//		AccountingYearViewModel yearVm = accountingYears.stream().filter(year -> year.getYear() == selectedYear).findFirst().orElse(null);
//		firstAccountingMonth = yearVm.getMonths().stream().filter(month -> month.getMonth() == selectedMonth).findFirst().orElse(null);
		
		updateMonthTransactions();
	}
	
	///
	/// TransactionExecutedDialog
	///
	public void onTransactionBaseExecuted(int transactionId, boolean isDraft, AccountingMonthViewModel accountingMonth) {
		logger.debug("onTransactionExecuted id: " + transactionId);
		transactionExecutedDialog = new TransactionExecutedDialogViewModel();
		transactionExecutedDialog.setAccountId(selectedAccountId);
		transactionExecutedDialog.setId(transactionId);
		transactionExecutedDialog.setDraft(isDraft);
		transactionExecutedDialog.setAccountingMonth(accountingMonth);
		
		PrimeFaces.current().ajax().update(FormIds.MAIN_FORM.getValue());
		PrimeFaces.current().executeScript("PF('transactionExecutedDialog').show()");
	}
	
	public boolean isTransactionBaseExecutedDialogValid() {
		boolean isValid = true;
		if (transactionExecutedDialog.getExecutedAt() == null) {
			FacesMessageHandler.addMessage(FacesMessage.SEVERITY_ERROR, LocalizedMessageUtil.getMessage("error", Locale.getDefault())
					, LocalizedMessageUtil.getMessage("error.date.must.not.be.empty", Locale.getDefault()));
			isValid = false;
		}

		return isValid;
	}
	
	public void closeDialogAndSaveTransactionBaseExecutedAt() {
		logger.debug("Saving transaction executedAt");
		if (isTransactionBaseExecutedDialogValid()) {
			
			TransactionBaseViewModel vmb = transactionExecutedDialog.getAccountingMonth().getTransactions().stream().filter(tr -> tr.getId().equals(transactionExecutedDialog.getId())).findFirst().orElse(null);
			if (transactionExecutedDialog.isDraft()) {
				
			} else {
				
			}
			
			//TransactionViewModel vm = (TransactionViewModel) vmb;
			
			TransactionDetailDialogViewModel detailDialog = new TransactionDetailDialogViewModel("", false);
			
			detailDialog.setId(vmb.getId());
			detailDialog.setAccountId(selectedAccountId);
			detailDialog.setExecutedAt(transactionExecutedDialog.getExecutedAt());
			detailDialog.setExpectedDay(vmb.getExpectedDay());
			detailDialog.setTitle(vmb.getTitle());
			detailDialog.setDescription(vmb.getDescription());
			detailDialog.setValue(vmb.getValue());
			detailDialog.setAccountingMonth(transactionExecutedDialog.getAccountingMonth());
			detailDialog.setDraft(transactionExecutedDialog.isDraft());
			
			accountService.saveTransactionBase(detailDialog, transactionExecutedDialog.getAccountingMonth().getYear(), transactionExecutedDialog.getAccountingMonth().getMonth());
			reloadAccountingYear();
			PrimeFaces.current().ajax().update(FormIds.MAIN_FORM.getValue());
		}
	}
	
	///
	/// Misc
	///
	
	public Locale getCurrentLocale() {
		return Locale.getDefault();
	}
	
	public String getYearRangeForDatePicker() {
		return String.format("%d:%d", yearRange[0], yearRange[1]);
	}
	
	public String getMonthLocalizedByNumber(int month) {
		String mon =  Month.of(month).getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault());
		return mon;
	}
	
	public String getCurrencyFormatted(BigDecimal value) {
		return FormatterUtil.getCurrencyNumberFormattedWithSymbol(value, settingService.getDbNumberGrouping(), settingService.getDbCurrency().getSymbol(), settingService.getDbCurrencySymbolPosition() == "p" );
	}
	
	public String getDateFormat() {
		return settingService.getDbDateFormat();
	}
	
	public String getCurrencySymbolPosition() {
		return settingService.getDbCurrencySymbolPosition();
	}
	
	public String getNumberThousandSepartor() {
		return settingService.getDbNumberThousandSepartor();
	}
	
	public String getNumberDigitSeparator() {
		return settingService.getDbNumberDigitSeparator();
	}

///
/// getters / setter
///

	
	public Map<Integer, String> getAccountTypes() {
		return accountService.getAccountTypes(Locale.getDefault());
	}

	public AccountDetailDialogViewModel getAccountDialog() {
		return accountDialog;
	}

	public void setAccountDialog(AccountDetailDialogViewModel accountDialog) {
		this.accountDialog = accountDialog;
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

	public TreeMap<Integer, String> getSelectableMonths() {
		return selectableMonths;
	}

	public int getSelectedMonth() {
		return selectedMonth;
	}

	public int getSelectedYear() {
		return selectedYear;
	}

	public void setSelectedYear(int selectedYear) {
		this.selectedYear = selectedYear;
	}

	public List<Integer> getSelectableYears() {
		return selectableYears;
	}

	public void setSelectedMonth(int selectedMonth) {
		this.selectedMonth = selectedMonth;
	}

	public List<AccountingYearViewModel> getAccountingYears() {
		return accountingYears;
	}

	public void setAccountingYears(List<AccountingYearViewModel> accountingYears) {
		this.accountingYears = accountingYears;
	}

	public TransactionBaseViewModel getSelectedTransaction() {
		return selectedTransaction;
	}

	public void setSelectedTransaction(TransactionBaseViewModel selectedTransaction) {
		this.selectedTransaction = selectedTransaction;
	}

	public SettingService getSettingService() {
		return settingService;
	}

	public Integer getSelectedAccountId() {
		return selectedAccountId;
	}

	public void setSelectedAccountId(Integer selectedAccountId) {
		this.selectedAccountId = selectedAccountId;
	}

	public TransactionDetailDialogViewModel getTransactionDialog() {
		return transactionDialog;
	}

	public void setTransactionDialog(TransactionDetailDialogViewModel transactionDialog) {
		this.transactionDialog = transactionDialog;
	}

	public int[] getYearRange() {
		return yearRange;
	}

	public TransactionExecutedDialogViewModel getTransactionExecutedDialog() {
		return transactionExecutedDialog;
	}

	public void setTransactionExecutedDialog(TransactionExecutedDialogViewModel transactionExecutedDialog) {
		this.transactionExecutedDialog = transactionExecutedDialog;
	}

	public AccountingMonthViewModel getFirstAccountingMonth() {
		return firstAccountingMonth;
	}

	public void setFirstAccountingMonth(AccountingMonthViewModel firstAccountingMonth) {
		this.firstAccountingMonth = firstAccountingMonth;
	}

	public AccountingMonthViewModel getSecondAccountingMonth() {
		return secondAccountingMonth;
	}

	public void setSecondAccountingMonth(AccountingMonthViewModel secondAccountingMonth) {
		this.secondAccountingMonth = secondAccountingMonth;
	}

	public AccountingMonthViewModel getThirdAccountingMonth() {
		return thirdAccountingMonth;
	}

	public void setThirdAccountingMonth(AccountingMonthViewModel thirdAccountingMonth) {
		this.thirdAccountingMonth = thirdAccountingMonth;
	}

	public HashMap<Integer, String> getSelectableExpectedDay() {
		return selectableExpectedDay;
	}

	public void setSelectableExpectedDay(HashMap<Integer, String> selectableExpectedDay) {
		this.selectableExpectedDay = selectableExpectedDay;
	}

}
