package de.bassmech.findra.web.util;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import de.bassmech.findra.model.entity.Account;
import de.bassmech.findra.model.entity.AccountItem;
import de.bassmech.findra.model.entity.AccountItemGroup;
import de.bassmech.findra.model.entity.AccountTransaction;
import de.bassmech.findra.model.entity.AccountTransactionDraft;
import de.bassmech.findra.model.entity.AccountingMonth;
import de.bassmech.findra.model.entity.AccountingYear;
import de.bassmech.findra.model.entity.Tag;
import de.bassmech.findra.model.statics.ExpectedDay;
import de.bassmech.findra.web.view.model.AccountItemGroupViewModel;
import de.bassmech.findra.web.view.model.AccountItemViewModel;
import de.bassmech.findra.web.view.model.AccountViewModel;
import de.bassmech.findra.web.view.model.AccountingMonthViewModel;
import de.bassmech.findra.web.view.model.AccountingYearViewModel;
import de.bassmech.findra.web.view.model.DraftViewModel;
import de.bassmech.findra.web.view.model.TagViewModel;
import de.bassmech.findra.web.view.model.TransactionViewModel;


public class ToViewModelUtil {
	
	public static AccountViewModel toViewModel(Account entity) {
		AccountViewModel vm = new AccountViewModel();
		vm.setId(entity.getId());
		vm.setUserId(entity.getClient().getId());
		vm.setTitle(entity.getTitle());
		vm.setDescription(entity.getDescription());
		vm.setStartingYear(entity.getStartingYear());
		vm.setDeletedAt(entity.getDeletedAt());
		vm.setCreatedAt(entity.getCreatedAt());
		vm.setCategory(entity.getCategory());
		
		return vm;
	}
	
	public static List<TransactionViewModel> toTransactionViewModelList(List<AccountTransaction> entities, List<AccountItemGroupViewModel> groups) {
		List<TransactionViewModel> resultList = new ArrayList<>();
		for (AccountTransaction month : entities) {
			resultList.add(toViewModel(month, groups));
		}
		return resultList;
	}
	
	public static TransactionViewModel toViewModel(AccountTransaction entity, List<AccountItemGroupViewModel> groups) {
		TransactionViewModel vm = new TransactionViewModel();
		vm.setId(entity.getId());
		vm.setTitle(entity.getTitle());
		vm.setDescription(entity.getDescription());
		vm.setMonth(entity.getAccountingMonth().getMonth());
		vm.setYear(entity.getAccountingMonth().getAccountingYear().getYear());
		vm.setExpectedDay(entity.getExpectedDay());
		vm.setExecutedAt(entity.getExecutedAt());
		vm.setValue(entity.getValue());
		vm.setCreatedAt(entity.getCreatedAt());
		if (entity.getItem() != null) {
			AccountItemGroupViewModel group = groups.stream()
					.filter(gr -> gr.getId().equals(entity.getItem().getGroup().getId())).findFirst().orElse(null);
			if (group != null) {
				vm.setItem(group.getItems().stream()
						.filter(item -> item.getId().equals(entity.getItem().getId())).findFirst()
						.orElse(null));
				vm.setGroup(group);
			}
		}
		
		if (entity.getExecutedAt() != null) {
			vm.setDayForDisplay(String.valueOf(LocalDate.ofInstant(entity.getExecutedAt(), ZoneOffset.UTC).getDayOfMonth()));
		} else if(entity.getExpectedDay() > 0) {
			vm.setDayForDisplay(String.valueOf(entity.getExpectedDay()));
		} else {
			vm.setDayForDisplay(LocalizationUtil.getTag(ExpectedDay.getTagStringByDbValue(entity.getExpectedDay())));
		}
		
		if (entity.getDraft() != null) {
			vm.setDraftId(entity.getDraft().getId());
		}
		
		
		vm.setTags(toTagViewModelList(entity.getTags()));
		
		return vm;
	}
	
	public static AccountingYearViewModel toViewModel(AccountingYear entity, List<AccountItemGroupViewModel> itemGroups) {
		AccountingYearViewModel vm = new AccountingYearViewModel();
		vm.setId(entity.getId());
		vm.setYear(entity.getYear());
		vm.setStartValue(entity.getStartValue());
		vm.setTransactionSum(entity.getClosingValue());
		vm.setAccountId(entity.getAccount().getId());
		
		for (AccountingMonth month : entity.getMonths()) {
			vm.getMonths().add(toViewModel(month, itemGroups));
		}
		vm.addDraftMonths();
		
		return vm;
	}
	
	public static AccountingMonthViewModel toViewModel(AccountingMonth entity, List<AccountItemGroupViewModel> groups) {
		AccountingMonthViewModel vm = new AccountingMonthViewModel();
		vm.setId(entity.getId());
		vm.setAccountYearId(entity.getAccountingYear().getId());
		vm.setMonth(entity.getMonth());
		vm.setYear(entity.getAccountingYear().getYear());
		vm.setStartValue(entity.getStartValue());
		
		for (AccountTransaction transaction : entity.getTransactions()) {
			vm.getTransactions().add(toViewModel(transaction,groups));
		}
		
		vm.recalculateTransactions();
		return vm;
	}

	public static List<TagViewModel> toTagViewModelList(List<Tag> tags) {
		List<TagViewModel> vmList = new ArrayList<TagViewModel>(tags.size());
		for (Tag tag : tags) {
			vmList.add(toViewModel(tag));
		}
		return vmList;
	}

	public static TagViewModel toViewModel(Tag tag) {
		TagViewModel vm = new TagViewModel();
		vm.setId(tag.getId());
		vm.setUserId(tag.getClient().getId());
		vm.setTitle(tag.getTitle());
		vm.setDescription(tag.getDescription());
		vm.setBackgroundHexColor(tag.getBackgroundHexColor());
		vm.setTextHexColor(tag.getTextHexColor());
		vm.setDeletedAt(tag.getDeletedAt());
		vm.setCreatedAt(tag.getCreatedAt());
		return vm;
	}
	
	public static DraftViewModel toViewModel(AccountTransactionDraft draft, List<AccountItemGroupViewModel> groups) {
		DraftViewModel vm = new DraftViewModel();
		vm.setAccountId(draft.getAccount().getId());
		vm.setId(draft.getId());
		vm.setTitle(draft.getTitle());
		vm.setDescription(draft.getDescription());
		vm.setExpectedDay(draft.getExpectedDay());
		vm.setInterval(draft.getInterval());
		vm.setPredecessorId(draft.getPredecessor() == null ? null : draft.getPredecessor().getId());
		vm.setStartsAt(draft.getStartsAt());
		vm.setEndsAt(draft.getEndsAt());
		vm.setValue(draft.getValue());
		vm.setCreatedAt(draft.getCreatedAt());
		
		if (draft.getItem() != null) {
			AccountItemGroupViewModel group = groups.stream()
					.filter(gr -> gr.getId().equals(draft.getItem().getGroup().getId())).findFirst().orElse(null);
			if (group != null) {
				vm.setItem(group.getItems().stream()
						.filter(item -> item.getId().equals(draft.getItem().getId())).findFirst()
						.orElse(null));
				vm.setGroup(group);
			}
		}
		
		for (Tag tag : draft.getTags()) {
			vm.getTags().add(toViewModel(tag));
		}
		
		return vm;
	}

	public static List<DraftViewModel> toDraftViewModelList(List<AccountTransactionDraft> drafts, List<AccountItemGroupViewModel> groups) {
		List<DraftViewModel> result = new ArrayList<>(drafts.size());
		for (AccountTransactionDraft draft : drafts) {
			result.add(toViewModel(draft, groups));
		}
		return result;
	}
	
	public static List<AccountItemGroupViewModel> toAccountItemGroupViewModelList(List<AccountItemGroup> groups) {
		List<AccountItemGroupViewModel> list = new ArrayList<AccountItemGroupViewModel>();
		for (AccountItemGroup group : groups) {
			list.add(toViewModel(group));
		}
		
		return list;
	}
		
	public static AccountItemGroupViewModel toViewModel(AccountItemGroup entity) {
		AccountItemGroupViewModel vm = new AccountItemGroupViewModel();
		vm.setAccountId(entity.getAccount().getId());
		vm.setCreatedAt(entity.getCreatedAt());
		vm.setId(entity.getId());
		vm.setTitle(entity.getTitle());
		vm.setDescription(entity.getDescription());
		
		for (AccountItem item : entity.getItems()) {
			vm.getItems().add(toViewModel(item, vm));
		}
		return vm;
	}
	
	public static AccountItemViewModel toViewModel(AccountItem entity, AccountItemGroupViewModel group) {
		AccountItemViewModel vm = new AccountItemViewModel();
		vm.setGroupId(group.getId());
		vm.setCreatedAt(entity.getCreatedAt());
		vm.setId(entity.getId());
		vm.setTitle(entity.getTitle());
		vm.setDescription(entity.getDescription());
		
		return vm;
	}
}
