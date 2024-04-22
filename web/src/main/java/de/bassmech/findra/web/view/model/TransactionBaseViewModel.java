package de.bassmech.findra.web.view.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.primefaces.PrimeFaces;

import de.bassmech.findra.model.entity.AccountItem;
import de.bassmech.findra.model.statics.ExpectedDay;
import de.bassmech.findra.web.util.LocalizationUtil;
import de.bassmech.findra.web.util.statics.FormIds;
import de.bassmech.findra.web.util.statics.TagName;

public abstract class TransactionBaseViewModel implements Serializable {
	protected Integer id;
	protected String title;
	protected String description;
	protected int expectedDay;
	protected String dayForDisplay;
	protected BigDecimal value;
	protected Instant executedAt;
	protected Instant createdAt;
	protected AccountItemViewModel item;
	protected AccountItemGroupViewModel group;
	
	protected List<TagViewModel> tags = new ArrayList<>();
	
	
			
	public TransactionBaseViewModel() {
		expectedDay = 0;
		dayForDisplay = LocalizationUtil.getTag(ExpectedDay.UNKNOWN.getTagString());
	}
	
	public String getIcon() {
		if (isDraft()) {
			return "pi-sync";
		} else {
			return executedAt == null ? "pi-question-circle" : "pi-check-circle";
		}
	}
	
	public void onAccountDisplayOptionsEdit() {
		
	}
	
	private void prepareAndOpenAccountDisplayOptionsDialog() {
		AccountDisplayOptionsDialogViewModel transactionDialog = new AccountDisplayOptionsDialogViewModel();	

		
		PrimeFaces.current().ajax().update(FormIds.MAIN_FORM.getValue());
		PrimeFaces.current().executeScript("PF('transactionDetailDialog').show()");
	}
		
//	public void closeDialogAndSaveTransaction() {
//		//logger.debug("Saving transaction");
//		
////		if (isTransactionDialogValid()) {
////			transactionDialog.setAccountId(selectedAccountId);
////			
////			if (transactionDialog.isDraft()) {
////				accountService.saveDraft((TransactionDraftDetailDialogViewModel) transactionDialog);
////			} else {
////				TransactionDetailDialogViewModel tempTransactionDialog = (TransactionDetailDialogViewModel) transactionDialog;
////				accountService.saveTransaction(tempTransactionDialog);
////			}
////			
////			updateMonthTransactions();
//			PrimeFaces.current().ajax().update(FormIds.MAIN_FORM.getValue());
//		}
//	}

	
	public abstract boolean isDraft();
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getExpectedDay() {
		return expectedDay;
	}

	public void setExpectedDay(int expectedDay) {
		this.expectedDay = expectedDay;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public List<TagViewModel> getTags() {
		return tags;
	}

	public void setTags(List<TagViewModel> tags) {
		this.tags = tags;
	}

	public String getDayForDisplay() {
		return dayForDisplay;
	}

	public void setDayForDisplay(String expectedDayForDisplay) {
		this.dayForDisplay = expectedDayForDisplay;
	}

	public Instant getExecutedAt() {
		return executedAt;
	}

	public void setExecutedAt(Instant executedAt) {
		this.executedAt = executedAt;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public AccountItemViewModel getItem() {
		return item;
	}

	public void setItem(AccountItemViewModel item) {
		this.item = item;
	}

	public AccountItemGroupViewModel getGroup() {
		return group;
	}

	public void setGroup(AccountItemGroupViewModel group) {
		this.group = group;
	}

}
