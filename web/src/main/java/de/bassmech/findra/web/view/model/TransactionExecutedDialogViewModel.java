package de.bassmech.findra.web.view.model;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Year;

public class TransactionExecutedDialogViewModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer accountId;
	private LocalDate executedAt;
	private boolean isDraft;
	private AccountingMonthViewModel accountingMonth;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public LocalDate getExecutedAt() {
		return executedAt;
	}

	public void setExecutedAt(LocalDate executedAt) {
		this.executedAt = executedAt;
	}

	public boolean isDraft() {
		return isDraft;
	}

	public void setDraft(boolean isDraft) {
		this.isDraft = isDraft;
	}

	public AccountingMonthViewModel getAccountingMonth() {
		return accountingMonth;
	}

	public void setAccountingMonth(AccountingMonthViewModel accountingMonth) {
		this.accountingMonth = accountingMonth;
	}

}
