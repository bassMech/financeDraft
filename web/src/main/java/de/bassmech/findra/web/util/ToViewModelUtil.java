package de.bassmech.findra.web.util;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import de.bassmech.findra.model.entity.Account;
import de.bassmech.findra.model.entity.AccountTransaction;
import de.bassmech.findra.model.entity.AccountTransactionDraft;
import de.bassmech.findra.model.entity.AccountingMonth;
import de.bassmech.findra.model.entity.AccountingYear;
import de.bassmech.findra.model.entity.Tag;
import de.bassmech.findra.model.statics.ExpectedDay;
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
		
		return vm;
	}
	
	public static List<TransactionViewModel> toTransactionViewModelList(List<AccountTransaction> entities) {
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
		vm.setCreatedAt(entity.getCreatedAt());
		
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
	
	public static AccountingYearViewModel toViewModel(AccountingYear entity) {
		AccountingYearViewModel vm = new AccountingYearViewModel();
		vm.setId(entity.getId());
		vm.setYear(entity.getYear());
		vm.setStartValue(entity.getStartValue());
		vm.setTransactionSum(entity.getClosingValue());
		vm.setAccountId(entity.getAccount().getId());
		
		for (AccountingMonth month : entity.getMonths()) {
			vm.getMonths().add(toViewModel(month));
		}
		vm.addDraftMonths();
		
		return vm;
	}
	
	public static AccountingMonthViewModel toViewModel(AccountingMonth entity) {
		AccountingMonthViewModel vm = new AccountingMonthViewModel();
		vm.setId(entity.getId());
		vm.setAccountYearId(entity.getAccountingYear().getId());
		vm.setMonth(entity.getMonth());
		vm.setYear(entity.getAccountingYear().getYear());
		vm.setStartValue(entity.getStartValue());
		
		for (AccountTransaction transaction : entity.getTransactions()) {
			vm.getTransactions().add(toViewModel(transaction));
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
	
	public static DraftViewModel toViewModel(AccountTransactionDraft draft) {
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
		
		for (Tag tag : draft.getTags()) {
			vm.getTags().add(toViewModel(tag));
		}
		
		return vm;
	}

	public static List<DraftViewModel> toDraftViewModelList(List<AccountTransactionDraft> drafts) {
		List<DraftViewModel> result = new ArrayList<>(drafts.size());
		for (AccountTransactionDraft draft : drafts) {
			result.add(toViewModel(draft));
		}
		return result;
	}
		
}
