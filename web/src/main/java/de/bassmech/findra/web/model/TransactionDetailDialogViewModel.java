package de.bassmech.findra.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

public class TransactionDetailDialogViewModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer accountId;
	private String title;
	private String description;
	private BigDecimal value = BigDecimal.ZERO;
	private Integer expectedDay;
	private Instant executedAt;
	
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

	public Instant getExecutedAt() {
		return executedAt;
	}

	public void setExecutedAt(Instant executedAt) {
		this.executedAt = executedAt;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}


}
