package de.bassmech.findra.web.view;

import java.math.BigDecimal;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContext;

import de.bassmech.findra.web.handler.FacesMessageHandler;
import de.bassmech.findra.web.model.AccountDialogViewModel;
import de.bassmech.findra.web.model.AccountType;
import de.bassmech.findra.web.model.AccountViewModel;
import de.bassmech.findra.web.model.AccountingMonthViewModel;
import de.bassmech.findra.web.model.AccountingYearViewModel;
import de.bassmech.findra.web.model.TransactionViewModel;
import de.bassmech.findra.web.service.AccountService;
import de.bassmech.findra.web.service.SettingService;
import de.bassmech.findra.web.util.LocalizedMessageUtil;
import de.bassmech.findra.web.util.statics.FormIds;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.bean.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;

@Component
@ViewScoped
public class AccountView extends ViewBase {

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private SettingService settingService;
	
	private List<AccountViewModel> selectableAccounts = new ArrayList<>();
	private AccountViewModel selectedAccount;

	private List<AccountingYearViewModel> accountingYears = new ArrayList<>();
	private AccountingMonthViewModel currentAccountingMonth = null;
	private TransactionViewModel selectedTransaction;

	private TreeMap<Integer, String> selectableMonths = new TreeMap<>();
	private int selectedMonth;

	private List<Integer> selectableYears = new ArrayList<>();
	private int selectedYear;
	
	private AccountDialogViewModel accountDialog = new AccountDialogViewModel();
	
	@PostConstruct
	public void init() {
		checkLanguageChange();
		reloadSelectableAccounts();

		selectedMonth = 1;

		// TODO add years from account
		int startYear = Year.now().getValue();

		if (selectableAccounts.size() > 0) {
			accountingYears.add(accountService.getAccountMonthsForYear(selectableAccounts.get(0).getId(), startYear));
			currentAccountingMonth = accountingYears.get(0).getMonths().stream()
					.filter(month -> month.getMonth() == selectedMonth).findFirst().orElse(null);
			selectedAccount = selectableAccounts.get(0);
		}
		
		for (int i = 1; i <= 12; i++) {
			selectableMonths.put(i, java.time.Month.of(i).getDisplayName(TextStyle.FULL_STANDALONE, currentLocale));
			selectableYears.add(startYear);
			startYear++;
		}
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

	public void onMonthChanged() {
		logger.debug("onMonthChanged");
		logger.debug("Month was changed to: " + selectedMonth);
	}

	public void onYearChanged() {
		logger.debug("onYearChanged");
		logger.debug("Year was changed to: " + selectedYear);
	}

	public void onNextMonthClick() {
		logger.debug("onNextMonthClick: ");
		if (selectedMonth == 12) {
			selectedMonth = 1;
		} else {
			selectedMonth++;
		}
		logger.debug("Month was changed to: " + selectedMonth);
	}

	public void onPreviousMonthClick() {
		logger.debug("onPreviousMonthClick");
		if (selectedMonth == 1) {
			selectedMonth = 12;
		} else {
			selectedMonth--;
		}
		logger.debug("Month was changed to: " + selectedMonth);
	}

	public void onDeleteTransactionClick() {
		logger.debug("onDeleteTransactionClick: " + selectedTransaction.getId());
	}
	
	public void openAccountDetailDialogNew() {
		accountDialog = new AccountDialogViewModel();	
		
		PrimeFaces.current().ajax().update("@form");
		PrimeFaces.current().executeScript("PF('accountDetailDialog').show()");
	}
	
	public void openAccountDetailDialogEdit() {
		accountDialog = new AccountDialogViewModel();	
		accountDialog.setId(selectedAccount.getId());
		accountDialog.setTitle(selectedAccount.getTitle());
		accountDialog.setDescription(selectedAccount.getDescription());
		accountDialog.setType(AccountType.METAL.getDbValue());
		
		PrimeFaces.current().ajax().update(FormIds.MAIN_FORM.getValue());
		PrimeFaces.current().executeScript("PF('accountDetailDialog').show()");
	}
	
	public boolean isAccountDialogValid() {
		boolean isValid = true;
		if (accountDialog.getTitle() == null || accountDialog.getTitle().isBlank()) {
			FacesMessageHandler.addMessage(FacesMessage.SEVERITY_ERROR, LocalizedMessageUtil.getMessage("error", currentLocale)
					, LocalizedMessageUtil.getMessage("error.title.must.not.be.empty", currentLocale));
			isValid = false;
		}
//		if (accountDialog.getDescription() == null || accountDialog.getDescription().isBlank()) {
//			FacesMessageHandler.addMessage(FacesMessage.SEVERITY_INFO, "Info Message", "Message Content");
//			isValid = false;
//		}
		return isValid;
	}
	
	public void closeAndSaveAccountDialog() {
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
	
	public Map<Integer, String> getAccountTypes() {
		return accountService.getAccountTypes(currentLocale);
	}

	public AccountDialogViewModel getAccountDialog() {
		return accountDialog;
	}

	public void setAccountDialog(AccountDialogViewModel accountDialog) {
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

}
