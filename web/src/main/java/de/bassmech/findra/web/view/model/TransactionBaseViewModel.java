package de.bassmech.findra.web.view.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import de.bassmech.findra.model.statics.ExpectedDay;
import de.bassmech.findra.web.util.LocalizedMessageUtil;

public abstract class TransactionBaseViewModel implements Serializable {
	protected Integer id;
	protected String title;
	protected String description;
	protected int expectedDay;
	protected String expectedDayForDisplay;
	protected BigDecimal value;
	protected Instant executedAt;
	
	protected List<TagViewModel> tags = new ArrayList<>();
			
	public TransactionBaseViewModel() {
		expectedDay = 0;
		expectedDayForDisplay = LocalizedMessageUtil.getTag(ExpectedDay.UNKNOWN.getTagString());
	}
	
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

	public String getExpectedDayForDisplay() {
		return expectedDayForDisplay;
	}

	public void setExpectedDayForDisplay(String expectedDayForDisplay) {
		this.expectedDayForDisplay = expectedDayForDisplay;
	}

	public Instant getExecutedAt() {
		return executedAt;
	}

	public void setExecutedAt(Instant executedAt) {
		this.executedAt = executedAt;
	}

}
