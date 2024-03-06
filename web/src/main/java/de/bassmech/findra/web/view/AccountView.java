package de.bassmech.findra.web.view;

import java.math.BigDecimal;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.bassmech.findra.model.statics.ConfigurationCode;
import de.bassmech.findra.web.handler.FacesMessageHandler;
import de.bassmech.findra.web.service.AccountService;
import de.bassmech.findra.web.service.ConfigurationService;
import de.bassmech.findra.web.service.SettingService;
import de.bassmech.findra.web.util.LocalizedMessageUtil;
import de.bassmech.findra.web.util.statics.FormIds;
import de.bassmech.findra.web.util.statics.TagName;
import de.bassmech.findra.web.view.model.AccountDetailDialogViewModel;
import de.bassmech.findra.web.view.model.AccountViewModel;
import de.bassmech.findra.web.view.model.AccountingMonthViewModel;
import de.bassmech.findra.web.view.model.AccountingYearViewModel;
import de.bassmech.findra.web.view.model.TransactionDetailDialogViewModel;
import de.bassmech.findra.web.view.model.TransactionViewModel;
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
	private ConfigurationService configurationService;
		
	private List<AccountViewModel> selectableAccounts = new ArrayList<>();
	private AccountViewModel selectedAccount;
	private Integer selectedAccountId;

	private List<AccountingYearViewModel> accountingYears = new ArrayList<>();
	private AccountingMonthViewModel currentAccountingMonth = null;
	private TransactionViewModel selectedTransaction;

	private TreeMap<Integer, String> selectableMonths = new TreeMap<>();
	private int selectedMonth;

	private List<Integer> selectableYears = new ArrayList<>();
	private int selectedYear;
	
	private AccountDetailDialogViewModel accountDialog = new AccountDetailDialogViewModel("");
	private TransactionDetailDialogViewModel transactionDialog = new TransactionDetailDialogViewModel();
	
	private int[] yearRange = new int[2];
	
	@PostConstruct
	public void init() {
		logger.debug("init called");
		
		reloadSelectableAccounts();

		selectedMonth = 1;

		// TODO add years from account
		int startYear = Year.now().getValue();
		selectedYear = startYear;

		if (selectableAccounts.size() > 0) {
			accountingYears.add(accountService.getAccountYear(selectableAccounts.get(0).getId(), startYear));
			currentAccountingMonth = accountingYears.get(0).getMonths().stream()
					.filter(month -> month.getMonth() == selectedMonth).findFirst().orElse(null);
			selectedAccount = selectableAccounts.get(0);
			selectedAccountId = selectedAccount.getId();
		}
		
		for (int i = 1; i <= 12; i++) {
			selectableMonths.put(i, java.time.Month.of(i).getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()));
			selectableYears.add(startYear);
			startYear++;
		}
		
		yearRange[0] = Integer.parseInt(configurationService.getByCode(ConfigurationCode.YEAR_RANGE_MIN));
		yearRange[1] = Integer.parseInt(configurationService.getByCode(ConfigurationCode.YEAR_RANGE_MAX));
	}
	
	private void reloadSelectableAccounts() {
		selectableAccounts = accountService.getAccountList();
	}

	public String getTransactionRowColorString(TransactionViewModel vm) {
		if (BigDecimal.ZERO.compareTo(vm.getValue()) > 0) {
			return vm.getExecutedAt() == null ? "transaction-negative-expected" : "transaction-negative-executed";
		} else if (BigDecimal.ZERO.compareTo(vm.getValue()) < 0) {	
			return vm.getExecutedAt() == null ? "transaction-positive-expected" : "transaction-positive-executed" ;
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
		AccountingYearViewModel modelYear = accountingYears.stream().filter(year -> year.getYear() == selectedYear).findFirst().orElse(null);
		if (modelYear == null) {
			logger.debug(String.format("Creating new year for account with id: %d", selectedAccount.getId()));
			modelYear = new AccountingYearViewModel();
			modelYear.setAccountId(selectedAccount.getId());
			modelYear.setYear(selectedMonth);
			accountingYears.add(modelYear);
			modelYear.addDraftMonths();
		}
		
		currentAccountingMonth = modelYear.getMonths().stream().filter(month -> month.getMonth() == selectedMonth).findFirst().orElse(null);
	}



	public void onDeleteTransactionClick() {
		logger.debug("onDeleteTransactionClick: " + selectedTransaction.getId());
	}
	
	public void openAccountDetailDialogNew() {
		accountDialog = new AccountDetailDialogViewModel(LocalizedMessageUtil.getTag(TagName.ACCOUNT_NEW.getValue()));	
		
		PrimeFaces.current().ajax().update("@form");
		PrimeFaces.current().executeScript("PF('accountDetailDialog').show()");
	}
	
	public void openAccountDetailDialogEdit() {
		accountDialog = new AccountDetailDialogViewModel(LocalizedMessageUtil.getTag(TagName.ACCOUNT_EDIT.getValue()));	
		accountDialog.setId(selectedAccount.getId());
		accountDialog.setTitle(selectedAccount.getTitle());
		accountDialog.setDescription(selectedAccount.getDescription());
		accountDialog.setType(AccountType.TRANSCATION.getDbValue());
		
		PrimeFaces.current().ajax().update(FormIds.MAIN_FORM.getValue());
		PrimeFaces.current().executeScript("PF('accountDetailDialog').show()");
	}
	
	public boolean isAccountDialogValid() {
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
/// Transaction related
///
	
	public boolean isTransactionDialogValid() {
		boolean isValid = true;
		if (transactionDialog.getTitle() == null || transactionDialog.getTitle().isBlank()) {
			FacesMessageHandler.addMessage(FacesMessage.SEVERITY_ERROR, LocalizedMessageUtil.getMessage("error", Locale.getDefault())
					, LocalizedMessageUtil.getMessage("error.title.must.not.be.empty", Locale.getDefault()));
			isValid = false;
		}

		return isValid;
	}
	
	public void openTransactionDetailDialogNew() {
		transactionDialog = new TransactionDetailDialogViewModel();	
		
		PrimeFaces.current().ajax().update("@form");
		PrimeFaces.current().executeScript("PF('transactionDetailDialog').show()");
	}
	
	public void closeDialogAndSaveTransaction() {
		logger.debug("Saving transaction");
		
		if (isTransactionDialogValid()) {
			transactionDialog.setAccountId(selectedAccountId);
			
			accountService.saveTransaction(transactionDialog, selectedYear, selectedMonth);
			accountingYears.removeIf(yearX -> yearX.getYear() == selectedYear);
			accountingYears.add(accountService.getAccountYear(selectedAccountId, selectedYear));
			AccountingYearViewModel yearVm = accountingYears.stream().filter(year -> year.getYear() == selectedYear).findFirst().orElse(null);
			currentAccountingMonth = yearVm.getMonths().stream().filter(month -> month.getMonth() == selectedMonth).findFirst().orElse(null);
			PrimeFaces.current().ajax().update(FormIds.MAIN_FORM.getValue());
		}
	}
	
	public Locale getCurrentLocale() {
		return Locale.getDefault();
	}
	
	public String getYearRangeForDatePicker() {
		return String.format("%d:%d", yearRange[0], yearRange[1]);
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

	public TransactionViewModel getSelectedTransaction() {
		return selectedTransaction;
	}

	public void setSelectedTransaction(TransactionViewModel selectedTransaction) {
		this.selectedTransaction = selectedTransaction;
	}

	public AccountingMonthViewModel getCurrentAccountingMonth() {
		return currentAccountingMonth;
	}

	public void setCurrentAccountingMonth(AccountingMonthViewModel currentAccountingMonth) {
		this.currentAccountingMonth = currentAccountingMonth;
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

}
