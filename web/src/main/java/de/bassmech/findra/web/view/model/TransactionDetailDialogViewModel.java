package de.bassmech.findra.web.view.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class TransactionDetailDialogViewModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer accountId;
	private String title;
	private String description;
	private BigDecimal value = BigDecimal.ZERO;
	private Integer expectedDay;
	private LocalDate executedAt;
	
	private AccountingMonthViewModel accountingMonth;
	private List<TagViewModel> tagsAvailable = new ArrayList<>();
	private List<TagViewModel> tagsAssigned = new ArrayList<>();
	
	private HashMap<Integer, String> selectableExpectedDay = new LinkedHashMap<>();
	
	public void onTagAssign(int tagId) {
		TagViewModel vm = tagsAvailable.stream().filter(tag -> tag.getId().equals(tagId)).findFirst().orElse(null);
		tagsAvailable.remove(vm);
		tagsAssigned.add(vm);
	}
	
	public void onTagRemove(int tagId) {
		TagViewModel vm = tagsAssigned.stream().filter(tag -> tag.getId().equals(tagId)).findFirst().orElse(null);
		tagsAssigned.remove(vm);
		tagsAvailable.add(vm);
	}
	
	public void onAllTagAssign() {
		tagsAssigned.addAll(tagsAvailable);
		tagsAvailable.clear();
	}
	
	public void onAllTagRemove() {
		tagsAvailable.addAll(tagsAssigned);
		tagsAssigned.clear();
	}
	
	public boolean isDeleteButtonRendered() {
		return id != null;
	}
	
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

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Integer getExpectedDay() {
		return expectedDay;
	}

	public void setExpectedDay(Integer expectedDay) {
		this.expectedDay = expectedDay;
	}

	public LocalDate getExecutedAt() {
		return executedAt;
	}

	public void setExecutedAt(LocalDate executedAt) {
		this.executedAt = executedAt;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public AccountingMonthViewModel getAccountingMonth() {
		return accountingMonth;
	}

	public void setAccountingMonth(AccountingMonthViewModel accountingMonth) {
		this.accountingMonth = accountingMonth;
	}

	public List<TagViewModel> getTagsAvailable() {
		return tagsAvailable;
	}

	public void setTagsAvailable(List<TagViewModel> tagsAvailable) {
		this.tagsAvailable = tagsAvailable;
	}

	public List<TagViewModel> getTagsAssigned() {
		return tagsAssigned;
	}

	public void setTagsAssigned(List<TagViewModel> tagsAssigned) {
		this.tagsAssigned = tagsAssigned;
	}

	public HashMap<Integer, String> getSelectableExpectedDay() {
		return selectableExpectedDay;
	}

	public void setSelectableExpectedDay(HashMap<Integer, String> selectableExpectedDay) {
		this.selectableExpectedDay = selectableExpectedDay;
	}

}
