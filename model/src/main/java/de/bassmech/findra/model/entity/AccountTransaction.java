package de.bassmech.findra.model.entity;

import java.time.Instant;

import de.bassmech.findra.model.converter.NumberToInstantConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "account_transaction")
public class AccountTransaction extends TransactionBase {
	
	@ManyToOne(targetEntity = AccountTransactionDraft.class)
	@JoinColumn(referencedColumnName = "id", name = "draft_id")
	private AccountTransactionDraft draft;

	@ManyToOne(targetEntity = AccountingMonth.class)
	@JoinColumn(referencedColumnName = "id", name = "accounting_month_id")
	private AccountingMonth accountingMonth;
		
	@Column(name = "executed_at", columnDefinition = "INTEGER")
	@Convert(converter = NumberToInstantConverter.class)
	private Instant executedAt;
		

	public Instant getExecutedAt() {
		return executedAt;
	}

	public void setExecutedAt(Instant executedAt) {
		this.executedAt = executedAt;
	}

	public AccountingMonth getAccountingMonth() {
		return accountingMonth;
	}

	public void setAccountingMonth(AccountingMonth accountingMonth) {
		this.accountingMonth = accountingMonth;
	}

	public AccountTransactionDraft getDraft() {
		return draft;
	}

	public void setDraft(AccountTransactionDraft draft) {
		this.draft = draft;
	}


}
