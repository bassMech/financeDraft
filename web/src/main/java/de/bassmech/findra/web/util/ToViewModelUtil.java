package de.bassmech.findra.web.util;

import de.bassmech.findra.model.entity.Account;
import de.bassmech.findra.model.entity.Allocation;
import de.bassmech.findra.web.model.AccountAllocationModel;
import de.bassmech.findra.web.model.AccountViewModel;

public class ToViewModelUtil {
	
	public static AccountViewModel toViewModel(Account entity) {
		AccountViewModel vm = new AccountViewModel();
		
		vm.setId(entity.getId());
		vm.setTitle(entity.getTitle());
		vm.setDescription(entity.getDescription());
		vm.setDeletedAt(entity.getDeletedAt());
		
		return vm;
	}
	
	public static AccountAllocationModel toViewModel(Allocation entity) {
		AccountAllocationModel vm = new AccountAllocationModel();
		vm.setId(entity.getId());
		vm.setTitle(entity.getTitle());
		vm.setDescription(entity.getDescription());
		vm.setMonth(entity.getAccountingMonth().getMonth());
		vm.setYear(entity.getAccountingMonth().getAccountingYear().getYear());
		vm.setExpectedDay(entity.getExpectedDay());
		vm.setExecutedAt(entity.getExecutedAt());
		
		return vm;
	}
}
