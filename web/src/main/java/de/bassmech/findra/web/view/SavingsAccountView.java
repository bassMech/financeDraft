package de.bassmech.findra.web.view;

import java.math.BigDecimal;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.format.TextStyle;
import java.util.Currency;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import de.bassmech.findra.model.statics.AccountCategory;
import de.bassmech.findra.model.statics.ConfigurationCode;
import de.bassmech.findra.model.statics.ExpectedDay;
import de.bassmech.findra.web.handler.FacesMessageHandler;
import de.bassmech.findra.web.util.FormatterUtil;
import de.bassmech.findra.web.util.LocalizationUtil;
import de.bassmech.findra.web.util.statics.CssReference;
import de.bassmech.findra.web.util.statics.FormIds;
import de.bassmech.findra.web.util.statics.TagName;
import de.bassmech.findra.web.view.model.AccountDetailDialogViewModel;
import de.bassmech.findra.web.view.model.AccountItemGroupViewModel;
import de.bassmech.findra.web.view.model.AccountViewModel;
import de.bassmech.findra.web.view.model.AccountingMonthViewModel;
import de.bassmech.findra.web.view.model.AccountingYearViewModel;
import de.bassmech.findra.web.view.model.DraftListDialogViewModel;
import de.bassmech.findra.web.view.model.DraftViewModel;
import de.bassmech.findra.web.view.model.TagViewModel;
import de.bassmech.findra.web.view.model.TransactionBaseViewModel;
import de.bassmech.findra.web.view.model.TransactionDetailBaseDialogViewModel;
import de.bassmech.findra.web.view.model.TransactionDetailDialogViewModel;
import de.bassmech.findra.web.view.model.TransactionDraftDetailDialogViewModel;
import de.bassmech.findra.web.view.model.TransactionExecutedDialogViewModel;
import de.bassmech.findra.web.view.model.TransactionViewModel;
import de.bassmech.findra.web.view.model.type.AccountTransactionLayout;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;

@Component
@SessionScoped
public class SavingsAccountView extends AccountViewBase {
	
	private List<AccountItemGroupViewModel> groupList;

	@PostConstruct
	@DependsOn(value = { "SettingService", "AccountService"})
	public void init() {
		logger.debug("init called");
				
		client = sessionHandler.getLoggedInClientWithSessionCheck();
		
		reloadSelectableAccounts();

		selectedMonth = YearMonth.now().getMonthValue();

		// TODO add years from account
		int startYear = Year.now().getValue();
		selectedYear = startYear;

		if (selectableAccounts.size() > 0) {
			//accountingYears.add(accountService.getAccountYear(selectableAccounts.get(0).getId(), startYear));

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
	
		groupList = accountService.getAccountItemGroupsByAccountId(selectedAccountId);
	}
	
	private void setSelectableExpectedDay() {
		selectableExpectedDay = new LinkedHashMap<>();
		selectableExpectedDay.put(ExpectedDay.UNKNOWN.getDbValue(), LocalizationUtil.getTag(ExpectedDay.UNKNOWN.getTagString()));
		selectableExpectedDay.put(ExpectedDay.ULTIMO.getDbValue(), LocalizationUtil.getTag(ExpectedDay.ULTIMO.getTagString()));
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
			return vm instanceof DraftViewModel || ((TransactionViewModel) vm).getExecutedAt() == null ? CssReference.TRANSCTION_BG_NEGATIVE_EXPECTED.getValue() : CssReference.TRANSCTION_BG_NEGATIVE_EXECUTED.getValue();
		} else if (BigDecimal.ZERO.compareTo(vm.getValue()) < 0) {	
			return vm instanceof DraftViewModel || ((TransactionViewModel) vm).getExecutedAt() == null ? CssReference.TRANSCTION_BG_POSITIVE_EXPECTED.getValue() : CssReference.TRANSCTION_BG_POSITIVE_EXECUTED.getValue();
		} else {
			return null;
		}
	}
		
	///
	/// account related
	///
	public void onAccountChanged() {
		logger.debug("onAccountChanged");
		
		selectedAccount = selectableAccounts.stream().filter(acc -> acc.getId().equals(selectedAccountId)).findFirst().orElse(null);
		
		if (selectedYear > selectedAccount.getStartingYear()) {
			selectedYear = selectedAccount.getStartingYear();
		}
		updateMonthTransactions();
	}
	
	public void openAccountDetailDialogNew() {
		logger.debug("openAccountDetailDialogNew");
		accountDialog = new AccountDetailDialogViewModel(LocalizationUtil.getTag(TagName.ACCOUNT_NEW.getValue()));	
		
		PrimeFaces.current().ajax().update("@form");
		PrimeFaces.current().executeScript("PF('accountDetailDialog').show()");
	}
	
	public void openAccountDetailDialogEdit() {
		logger.debug("openAccountDetailDialogEdit");
		accountDialog = new AccountDetailDialogViewModel(LocalizationUtil.getTag(TagName.ACCOUNT_EDIT.getValue()));	
		accountDialog.setId(selectedAccount.getId());
		accountDialog.setTitle(selectedAccount.getTitle());
		accountDialog.setDescription(selectedAccount.getDescription());
		accountDialog.setCategory(AccountCategory.CURRENT_ACCOUNT.getDbValue());
		
		PrimeFaces.current().ajax().update(FormIds.MAIN_FORM.getValue());
		PrimeFaces.current().executeScript("PF('accountDetailDialog').show()");
	}
	
	private boolean isAccountDialogValid() {
		boolean isValid = true;
		if (accountDialog.getTitle() == null || accountDialog.getTitle().isBlank()) {
			FacesMessageHandler.addMessageFromKey (FacesMessage.SEVERITY_ERROR
					, "error.title.must.not.be.empty");
			isValid = false;
		}
		if (accountDialog.getStartingYear() < yearRange[0] || accountDialog.getStartingYear() > yearRange[1]) {
			FacesMessageHandler.addMessageFromKey(FacesMessage.SEVERITY_ERROR
					, "error.starting.year.must.be.in.range", yearRange[0], yearRange[1]);
			isValid = false;
		}
		return isValid;
	}
	
	public void closeDialogAndSaveAccount() {
		logger.debug("Saving account");
		
		if (isAccountDialogValid()) {
			AccountViewModel newAccount = accountService.saveAccount(client, accountDialog);
			reloadSelectableAccounts();
			selectedAccount = selectableAccounts.stream().filter(acc -> acc.getId().equals(newAccount.getId())).findFirst().orElse(null);
			selectedAccountId = selectedAccount.getId();
			init();
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
		
		prepareAndOpenTransactionDetailDialogForDraft(draftId);
	}
		
	public void onDraftNew() {
		logger.debug("onDraftNew");
		
		AccountingYearViewModel yearVm = accountService.getOrCreateAccountingYear(selectedAccountId, selectedYear);
		AccountingMonthViewModel monthVm =  yearVm == null ? null : yearVm.getMonths().stream().filter(month -> month.getMonth() == selectedMonth).findFirst().orElse(null);
				
		prepareAndOpenTransactionDetailDialogForDraft(null);
	}
	
	private void prepareAndOpenTransactionDetailDialogForDraft(Integer draftId) {
		TransactionDraftDetailDialogViewModel transactionDialog = new TransactionDraftDetailDialogViewModel(LocalizationUtil.getTag(draftId == null ? TagName.DRAFT_NEW.getValue() : TagName.DRAFT_EDIT.getValue()), true);	

		if (draftId == null) {
			transactionDialog.getTagsAvailable().addAll(tagService.getTagsForAccount(selectedAccountId, false));
			transactionDialog.setSelectedStartMonth(selectedMonth);
			transactionDialog.setSelectedStartYear(selectedYear);
		} else {
			DraftViewModel vm = accountService.getDraftViewModelById(draftId, selectedAccountId);
			transactionDialog.setId(vm.getId());
			transactionDialog.setAccountId(selectedAccountId);
			transactionDialog.setExpectedDay(vm.getExpectedDay());
			transactionDialog.setTitle(vm.getTitle());
			transactionDialog.setDescription(vm.getDescription());
			transactionDialog.setValue(vm.getValue());
			transactionDialog.setSelectedInterval(vm.getInterval().getDbValue());
			
			transactionDialog.setSelectedStartMonth(vm.getStartsAt().getMonthValue());
			transactionDialog.setSelectedStartYear(vm.getStartsAt().getYear());
			
			if (vm.getEndsAt() != null) {
				transactionDialog.setSelectedEndMonth(vm.getEndsAt().getMonthValue());
				transactionDialog.setSelectedEndYear(vm.getEndsAt().getYear());
			}
						
			List<TagViewModel> tagsForAccount = tagService.getTagsForAccount(selectedAccountId, false);
			for (TagViewModel tag : tagsForAccount) {
				if (vm.getTags().contains(tag)) {
					transactionDialog.getTagsAssigned().add(tag);
				} else {
					transactionDialog.getTagsAvailable().add(tag);
				}
			}
		}
				
		int maxDay = YearMonth.of(2024, 12).atEndOfMonth().getDayOfMonth();
		transactionDialog.setSelectableExpectedDay(selectableExpectedDay.entrySet().stream()
				.filter(entry -> entry.getKey() <= maxDay || entry.getKey() > 31)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> {throw new RuntimeException();}, LinkedHashMap::new)));
		//transactionDialog.setSelectableExpectedDay(selectableExpectedDay.sub (transactionId));
		
		this.transactionDialog = transactionDialog;
		
		PrimeFaces.current().ajax().update(FormIds.MAIN_FORM.getValue());
		PrimeFaces.current().executeScript("PF('transactionDetailDialog').show()");
	}
	
	public void onShowDraftList() {
		logger.debug("onShowDraftList");
		
		draftListDialog = new DraftListDialogViewModel(accountService.getLoadedDraftsByAccountId(selectedAccountId));
		
		PrimeFaces.current().ajax().update(FormIds.MAIN_FORM.getValue());
		PrimeFaces.current().executeScript("PF('draftListDialog').show()");
	}
	
///
/// Transaction related
///
	
	public void onDeleteTransaction() {
		logger.debug("onDeleteTransaction: " + transactionDialog.getId());
		
		accountService.deleteTransaction(transactionDialog.getId(), transactionDialog.getAccountId());
	
		PrimeFaces.current().ajax().update(FormIds.MAIN_FORM.getValue());
	}
	
	private boolean isTransactionDialogValid() {
		boolean isValid = true;
		if (transactionDialog.getTitle() == null || transactionDialog.getTitle().isBlank()) {
			FacesMessageHandler.addMessageFromKey(FacesMessage.SEVERITY_ERROR
					, "error.title.must.not.be.empty");
			isValid = false;
		}
		
		if (transactionDialog.isDraft()) {
			TransactionDraftDetailDialogViewModel draftDialog = (TransactionDraftDetailDialogViewModel) transactionDialog;
			if (draftDialog.getSelectedEndMonth() != null && draftDialog.getSelectedEndYear() == null
					|| draftDialog.getSelectedEndMonth() == null && draftDialog.getSelectedEndYear() != null) {
				FacesMessageHandler.addMessageFromKey(FacesMessage.SEVERITY_ERROR
						, "error.draft.end.year.month.need.both");
				isValid = false;
			}
			if (draftDialog.getSelectedEndMonth() != null && draftDialog.getSelectedEndYear() != null) {
				YearMonth startYear = YearMonth.of(draftDialog.getSelectedStartYear(), draftDialog.getSelectedStartMonth());
				YearMonth endYear = YearMonth.of(draftDialog.getSelectedEndYear(), draftDialog.getSelectedEndMonth());
				if (endYear.isBefore(startYear)) {
					FacesMessageHandler.addMessage(FacesMessage.SEVERITY_ERROR
							, "error.draft.date.end.needs.after.date.start");
					isValid = false;
				}
			}
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
		TransactionDetailDialogViewModel transactionDialog = new TransactionDetailDialogViewModel(LocalizationUtil.getTag(transactionId == null ? TagName.TRANSACTION_NEW.getValue() : TagName.TRANSACTION_EDIT.getValue()), false);	
		transactionDialog.setSelectedAccountingYear(accountingMonth.getYear());
		transactionDialog.setSelectedAccountingMonth(accountingMonth.getMonth());
		
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
		
		this.transactionDialog = transactionDialog;
		
		PrimeFaces.current().ajax().update(FormIds.MAIN_FORM.getValue());
		PrimeFaces.current().executeScript("PF('transactionDetailDialog').show()");
	}
		
	public void closeDialogAndSaveTransaction() {
		logger.debug("Saving transaction");
		
		if (isTransactionDialogValid()) {
			transactionDialog.setAccountId(selectedAccountId);
			
			if (transactionDialog.isDraft()) {
				accountService.saveDraft((TransactionDraftDetailDialogViewModel) transactionDialog);
			} else {
				TransactionDetailDialogViewModel tempTransactionDialog = (TransactionDetailDialogViewModel) transactionDialog;
				accountService.saveTransaction(tempTransactionDialog);
			}
			
			updateMonthTransactions();
			PrimeFaces.current().ajax().update(FormIds.MAIN_FORM.getValue());
		}
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
			FacesMessageHandler.addMessageFromKey(FacesMessage.SEVERITY_ERROR
					, "error.date.must.not.be.empty");
			isValid = false;
		}

		return isValid;
	}
	
	public void closeDialogAndSaveTransactionBaseExecutedAt() {
		logger.debug("Saving transaction executedAt");
		if (isTransactionBaseExecutedDialogValid()) {
			
			TransactionBaseViewModel vmb = transactionExecutedDialog.getAccountingMonth().getTransactions().stream().filter(tr -> tr.getId().equals(transactionExecutedDialog.getId())).findFirst().orElse(null);
			
			TransactionDetailDialogViewModel detailDialog = new TransactionDetailDialogViewModel("", false);
			
			detailDialog.setId(vmb.getId());
			detailDialog.setAccountId(selectedAccountId);
			detailDialog.setExecutedAt(transactionExecutedDialog.getExecutedAt());
			detailDialog.setExpectedDay(vmb.getExpectedDay());
			detailDialog.setTitle(vmb.getTitle());
			detailDialog.setDescription(vmb.getDescription());
			detailDialog.setValue(vmb.getValue());
			detailDialog.setSelectedAccountingYear(transactionExecutedDialog.getAccountingMonth().getYear());
			detailDialog.setSelectedAccountingMonth(transactionExecutedDialog.getAccountingMonth().getMonth());
			detailDialog.setDraft(transactionExecutedDialog.isDraft());
			
			accountService.saveTransaction(detailDialog);
			//reloadAccountingYear();
			updateMonthTransactions();
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
		return FormatterUtil.getCurrencyNumberFormattedWithSymbol(value, settingService.getDbNumberGrouping(client), settingService.getDbCurrency(client).getSymbol(), settingService.getDbCurrencySymbolPosition(client) == "p" );
	}
	
	public String getDateFormat() {
		return settingService.getDbDateFormat(client);
	}
	
	public String getCurrencySymbolPosition() {
		return settingService.getDbCurrencySymbolPosition(client);
	}
	
	public Currency getCurrency() {
		return settingService.getDbCurrency(client);
	}
	
	public String getNumberThousandSepartor() {
		return settingService.getDbNumberThousandSepartor(client);
	}
	
	public String getNumberDigitSeparator() {
		return settingService.getDbNumberDigitSeparator(client);
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

	public TransactionBaseViewModel getSelectedTransaction() {
		return selectedTransaction;
	}

	public void setSelectedTransaction(TransactionBaseViewModel selectedTransaction) {
		this.selectedTransaction = selectedTransaction;
	}

	public Integer getSelectedAccountId() {
		return selectedAccountId;
	}

	public void setSelectedAccountId(Integer selectedAccountId) {
		this.selectedAccountId = selectedAccountId;
	}

	public TransactionDetailBaseDialogViewModel getTransactionDialog() {
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

	public DraftListDialogViewModel getDraftListDialog() {
		return draftListDialog;
	}

	public void setDraftListDialog(DraftListDialogViewModel draftListDialogViewModel) {
		this.draftListDialog = draftListDialogViewModel;
	}

	public AccountTransactionLayout getTransactionLayout() {
		return transactionLayout;
	}

	public List<AccountItemGroupViewModel> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<AccountItemGroupViewModel> groupList) {
		this.groupList = groupList;
	}

}
