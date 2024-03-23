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
@Table(name = "accounting_year")
public class AccountingYear {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "year", columnDefinition = "INTEGER")
	private int year;

	@ManyToOne(targetEntity = Account.class)
	@JoinColumn(referencedColumnName = "id", name = "account_id")
	private Account account;

	@OneToMany(mappedBy = "accountingYear", cascade = CascadeType.ALL)
	private List<AccountingMonth> months = new ArrayList<>();
	
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

	public void setMonths(List<AccountingMonth> month) {
		this.months = month;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public BigDecimal getClosingValue() {
		return closingValue;
	}

	public void setClosingValue(BigDecimal transactionSum) {
		this.closingValue = transactionSum;
	}

	public List<AccountingMonth> getMonths() {
		return months;
	}

	public BigDecimal getStartValue() {
		return startValue;
	}

	public void setStartValue(BigDecimal startValue) {
		this.startValue = startValue;
	}

}
