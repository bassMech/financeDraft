package de.bassmech.findra.model.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "accounting_month")
public class AccountingMonth {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "month", columnDefinition = "INTEGER")
	private int month;

	@ManyToOne(targetEntity = AccountingYear.class)
	@JoinColumn(referencedColumnName = "id", name = "accounting_year_id")
	private AccountingYear accountingYear;

	@OneToMany(mappedBy = "accountingMonth")
	private List<AccountTransaction> transactions = new ArrayList<>();
	
	@Column(name = "startValue", columnDefinition = "FLOAT")
	private BigDecimal startValue;
	
	@Column(name = "transaction_sum", columnDefinition = "FLOAT")
	private BigDecimal transactionSum;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<AccountTransaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<AccountTransaction> transactions) {
		this.transactions = transactions;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public AccountingYear getAccountingYear() {
		return accountingYear;
	}

	public void setAccountingYear(AccountingYear accountingYear) {
		this.accountingYear = accountingYear;
	}

	public BigDecimal getTransactionSum() {
		return transactionSum;
	}

	public void setTransactionSum(BigDecimal transactionSum) {
		this.transactionSum = transactionSum;
	}

	public BigDecimal getStartValue() {
		return startValue;
	}

	public void setStartValue(BigDecimal startValue) {
		this.startValue = startValue;
	}

}
