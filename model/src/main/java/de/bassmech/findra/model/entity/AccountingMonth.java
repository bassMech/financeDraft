package de.bassmech.findra.model.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
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

	@OneToMany(mappedBy = "accountingMonth", cascade = CascadeType.ALL)
	private List<AccountTransaction> transactions = new ArrayList<>();
	
	@Column(name = "start_value", columnDefinition = "FLOAT")
	private BigDecimal startValue = BigDecimal.ZERO;
	
	@Column(name = "closing_value", columnDefinition = "FLOAT")
	private BigDecimal closingValue = BigDecimal.ZERO;

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

	public BigDecimal getClosingValue() {
		return closingValue;
	}

	public void setClosingValue(BigDecimal transactionSum) {
		this.closingValue = transactionSum;
	}

	public BigDecimal getStartValue() {
		return startValue;
	}

	public void setStartValue(BigDecimal startValue) {
		this.startValue = startValue;
	}

}
