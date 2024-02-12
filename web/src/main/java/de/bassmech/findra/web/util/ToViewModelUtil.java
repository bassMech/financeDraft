package de.bassmech.findra.web.util;

import java.util.ArrayList;
import java.util.List;

import de.bassmech.findra.model.entity.Account;
import de.bassmech.findra.model.entity.AccountingMonth;
import de.bassmech.findra.model.entity.AccountingYear;
import de.bassmech.findra.model.entity.AccountTransaction;
import de.bassmech.findra.web.model.AccountViewModel;
import de.bassmech.findra.web.model.AccountingMonthViewModel;
import de.bassmech.findra.web.model.AccountingYearViewModel;
import de.bassmech.findra.web.model.TransactionViewModel;


public class ToViewModelUtil {
	
	public static AccountViewModel toViewModel(Account entity) {
		AccountViewModel vm = new AccountViewModel();
		
		vm.setId(entity.getId());
		vm.setTitle(entity.getTitle());
		vm.setDescription(entity.getDescription());
		vm.setDeletedAt(entity.getDeletedAt());
		
		return vm;
	}
	
	public static List<TransactionViewModel> toViewModeList(List<AccountTransaction> entities) {
		List<TransactionViewModel> resultList = new ArrayList<>();
		for (AccountTransaction month : entities) {
			resultList.add(toViewModel(month));
		}
		return resultList;
	}
	
	public static TransactionViewModel toViewModel(AccountTransaction entity) {
		TransactionViewModel vm = new TransactionViewModel();
		vm.setId(entity.getId());
		vm.setTitle(entity.getTitle());
		vm.setDescription(entity.getDescription());
		vm.setMonth(entity.getAccountingMonth().getMonth());
		vm.setYear(entity.getAccountingMonth().getAccountingYear().getYear());
		vm.setExpectedDay(entity.getExpectedDay());
		vm.setExecutedAt(entity.getExecutedAt());
		vm.setValue(entity.getValue());
		
		return vm;
	}
	
	public static AccountingYearViewModel toViewModel(AccountingYear entity) {
		AccountingYearViewModel vm = new AccountingYearViewModel();
		vm.setId(entity.getId());
		vm.setYear(entity.getYear());
		vm.setTransactionSum(entity.getTransactionSum());
		vm.setAccountId(entity.getAccount().getId());
		
		for (AccountingMonth month : entity.getMonths()) {
			vm.getMonths().add(toViewModel(month));
		}
		
		return vm;
	}
	
	public static AccountingMonthViewModel toViewModel(AccountingMonth entity) {
		AccountingMonthViewModel vm = new AccountingMonthViewModel();
		vm.setId(entity.getId());
		vm.setAccountYearId(entity.getAccountingYear().getId());
		vm.setMonth(entity.getMonth());
		vm.setYear(entity.getAccountingYear().getId());
		vm.setStartValue(entity.getStartValue());
		
		for (AccountTransaction transaction : entity.getTransactions()) {
			vm.getTransactions().add(toViewModel(transaction));
		}
		
		vm.recalculateTransactions();
		return vm;
	}
}
