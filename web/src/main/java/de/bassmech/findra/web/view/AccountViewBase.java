package de.bassmech.findra.web.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import de.bassmech.findra.model.entity.Client;
import de.bassmech.findra.model.statics.TransactionColumnLayout;
import de.bassmech.findra.web.auth.SessionHandler;
import de.bassmech.findra.web.handler.FacesMessageHandler;
import de.bassmech.findra.web.service.AccountService;
import de.bassmech.findra.web.service.ConfigurationService;
import de.bassmech.findra.web.service.SettingService;
import de.bassmech.findra.web.service.TagService;
import de.bassmech.findra.web.view.model.AccountDetailDialogViewModel;
import de.bassmech.findra.web.view.model.AccountItemGroupViewModel;
import de.bassmech.findra.web.view.model.AccountViewModel;
import de.bassmech.findra.web.view.model.AccountingMonthViewModel;
import de.bassmech.findra.web.view.model.AccountingYearViewModel;
import de.bassmech.findra.web.view.model.DraftListDialogViewModel;
import de.bassmech.findra.web.view.model.TransactionBaseViewModel;
import de.bassmech.findra.web.view.model.TransactionDetailBaseDialogViewModel;
import de.bassmech.findra.web.view.model.TransactionDetailDialogViewModel;
import de.bassmech.findra.web.view.model.TransactionExecutedDialogViewModel;
import jakarta.faces.application.FacesMessage;

public abstract class AccountViewBase {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected AccountService accountService;

	@Autowired
	protected SettingService settingService;

	@Autowired
	protected TagService tagService;

	@Autowired
	protected SessionHandler sessionHandler;

	@Autowired
	protected ConfigurationService configurationService;

	protected Client client;
	protected List<AccountViewModel> selectableAccounts = new ArrayList<>();
	protected AccountViewModel selectedAccount;
	protected Integer selectedAccountId;

	protected TransactionBaseViewModel selectedTransaction;

	protected TreeMap<Integer, String> selectableMonths = new TreeMap<>();
	protected int selectedMonth;

	protected List<Integer> selectableYears = new ArrayList<>();
	protected int selectedYear;

	protected AccountDetailDialogViewModel accountDialog = new AccountDetailDialogViewModel("");
	protected TransactionDetailBaseDialogViewModel transactionDialog = new TransactionDetailDialogViewModel("", false);
	protected TransactionExecutedDialogViewModel transactionExecutedDialog = new TransactionExecutedDialogViewModel();
	protected DraftListDialogViewModel draftListDialog;

	protected AccountingMonthViewModel firstAccountingMonth = null;
	protected AccountingMonthViewModel secondAccountingMonth = null;
	protected AccountingMonthViewModel thirdAccountingMonth = null;

	protected HashMap<Integer, String> selectableExpectedDay = new LinkedHashMap<>();

	protected int[] yearRange = new int[2];

	protected TransactionColumnLayout transactionColumnLayout = TransactionColumnLayout.DOUBLE;

	protected List<AccountItemGroupViewModel> groupList;

	protected HashMap<Integer, String> selectableTransactionColumnDisplayOptions = new LinkedHashMap<>();
	protected HashMap<Integer, String> selectableTransactionGroupingDisplayOptions = new LinkedHashMap<>();

	///
	/// Month and year navigation
	////

	public void onNextYear() {
		logger.debug("onNextYear");
		if (selectedYear + 1 > yearRange[1]) {
			FacesMessageHandler.addMessageFromKey(FacesMessage.SEVERITY_WARN, "year.max.reached", yearRange[1]);
			return;
		}
		selectedYear++;
		updateMonthTransactions();
	}

	public void onPreviousYear() {
		logger.debug("onPreviousYear");
		if (selectedYear - 1 < yearRange[0]) {
			FacesMessageHandler.addMessageFromKey(FacesMessage.SEVERITY_WARN, "year.min.reached", yearRange[0]);
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
				FacesMessageHandler.addMessageFromKey(FacesMessage.SEVERITY_WARN, "year.max.reached", yearRange[1]);
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
				FacesMessageHandler.addMessageFromKey(FacesMessage.SEVERITY_WARN, "year.min.reached", yearRange[0]);
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

	protected void updateMonthTransactions() {
		logger.debug("updateMonthTransactions");
		// TODO add layout check and load only necessary
		AccountingYearViewModel modelYear = accountService.getOrCreateAccountingYear(selectedAccountId, selectedYear);

		firstAccountingMonth = modelYear.getMonths().stream().filter(month -> month.getMonth() == selectedMonth)
				.findFirst().orElse(null);

		int secondMonthNumber = selectedMonth + 1;
		if (selectedMonth + 1 == 13) {
			modelYear = accountService.getOrCreateAccountingYear(selectedAccountId, selectedYear + 1);
			secondMonthNumber = 1;
		}
		final int finalSecondMonthNumber = secondMonthNumber;
		secondAccountingMonth = modelYear.getMonths().stream()
				.filter(month -> month.getMonth() == finalSecondMonthNumber).findFirst().orElse(null);
		if (secondAccountingMonth == null) {
			secondAccountingMonth = new AccountingMonthViewModel();
			secondAccountingMonth.setAccountYearId(selectedAccountId);
			secondAccountingMonth.setMonth(finalSecondMonthNumber);
			secondAccountingMonth.setYear(modelYear.getYear());
		}

		int thirdMonthNumber = selectedMonth + 2;
		if (selectedMonth + 2 == 13) {
			modelYear = accountService.getOrCreateAccountingYear(selectedAccountId, selectedYear + 1);
			thirdMonthNumber = 1;
		} else if (selectedMonth + 2 == 14) {
			thirdMonthNumber = 2;
		}
		final int finalThirdMonthNumber = thirdMonthNumber;
		thirdAccountingMonth = modelYear.getMonths().stream().filter(month -> month.getMonth() == finalThirdMonthNumber)
				.findFirst().orElse(null);
		if (thirdAccountingMonth == null) {
			thirdAccountingMonth = new AccountingMonthViewModel();
			thirdAccountingMonth.setAccountYearId(selectedAccountId);
			thirdAccountingMonth.setMonth(finalThirdMonthNumber);
			thirdAccountingMonth.setYear(modelYear.getYear());
		}
	}

	public List<AccountItemGroupViewModel> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<AccountItemGroupViewModel> groupList) {
		this.groupList = groupList;
	}

	public HashMap<Integer, String> getSelectableTransactionColumnDisplayOptions() {
		return selectableTransactionColumnDisplayOptions;
	}

	public void setSelectableTransactionColumnDisplayOptions(
			HashMap<Integer, String> selectableTransactionColumnDisplayOptions) {
		this.selectableTransactionColumnDisplayOptions = selectableTransactionColumnDisplayOptions;
	}

	public HashMap<Integer, String> getSelectableTransactionGroupingDisplayOptions() {
		return selectableTransactionGroupingDisplayOptions;
	}

	public void setSelectableTransactionGroupingDisplayOptions(
			HashMap<Integer, String> selectableTransactionGroupingDisplayOptions) {
		this.selectableTransactionGroupingDisplayOptions = selectableTransactionGroupingDisplayOptions;
	}

}
