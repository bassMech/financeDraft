package de.bassmech.findra.web.view;

import java.math.BigDecimal;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.bassmech.findra.web.model.AccountViewModel;
import de.bassmech.findra.web.model.AccountingMonthViewModel;
import de.bassmech.findra.web.model.AccountingYearViewModel;
import de.bassmech.findra.web.model.TransactionViewModel;
import de.bassmech.findra.web.service.AccountService;
import de.bassmech.findra.web.service.SettingService;
import jakarta.annotation.PostConstruct;
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

	@PostConstruct
	public void init() {
		checkLanguageChange();
		selectableAccounts = accountService.getAccountList();

		selectedMonth = 1;

		// TODO add years from account
		int startYear = Year.now().getValue();

		if (selectableAccounts.size() > 0) {
			accountingYears.add(accountService.getAccountMonthsForYear(selectableAccounts.get(0).getId(), startYear));
			currentAccountingMonth = accountingYears.get(0).getMonths().stream()
					.filter(month -> month.getMonth() == selectedMonth).findFirst().orElse(null);
		}

		for (int i = 1; i <= 12; i++) {
			selectableMonths.put(i, java.time.Month.of(i).getDisplayName(TextStyle.FULL_STANDALONE, currentLocale));
			selectableYears.add(startYear);
			startYear++;
		}
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
