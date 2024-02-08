package de.bassmech.findra.web.util;

import de.bassmech.findra.model.entity.Allocation;
import de.bassmech.findra.web.model.AccountAllocationViewModel;

public class ToViewModelUtil {
	public AccountAllocationViewModel toViewModel(Allocation entity) {
		AccountAllocationViewModel vm = new AccountAllocationViewModel();
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
