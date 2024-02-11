package de.bassmech.findra.web.util;

import java.util.ArrayList;
import java.util.List;

import de.bassmech.findra.model.entity.Account;
import de.bassmech.findra.model.entity.AccountingMonth;
import de.bassmech.findra.model.entity.AccountingYear;
import de.bassmech.findra.model.entity.Allocation;
import de.bassmech.findra.web.model.AccountViewModel;
import de.bassmech.findra.web.model.AccountingMonthViewModel;
import de.bassmech.findra.web.model.AccountingYearViewModel;
import de.bassmech.findra.web.model.AllocationViewModel;


public class ToViewModelUtil {
	
	public static AccountViewModel toViewModel(Account entity) {
		AccountViewModel vm = new AccountViewModel();
		
		vm.setId(entity.getId());
		vm.setTitle(entity.getTitle());
		vm.setDescription(entity.getDescription());
		vm.setDeletedAt(entity.getDeletedAt());
		
		return vm;
	}
	
	public static List<AllocationViewModel> toViewModeList(List<Allocation> entities) {
		List<AllocationViewModel> resultList = new ArrayList<>();
		for (Allocation month : entities) {
			resultList.add(toViewModel(month));
		}
		return resultList;
	}
	
	public static AllocationViewModel toViewModel(Allocation entity) {
		AllocationViewModel vm = new AllocationViewModel();
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
		vm.setAllocationSum(entity.getAllocationSum());
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
		
		for (Allocation allocation : entity.getAllocations()) {
			vm.getAllocations().add(toViewModel(allocation));
		}
		
		return vm;
	}
}
