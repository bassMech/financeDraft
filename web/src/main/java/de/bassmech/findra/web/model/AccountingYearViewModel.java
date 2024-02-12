package de.bassmech.findra.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountingYearViewModel implements Serializable {
	private int id;
	private int accountId;
	private int year;
	private BigDecimal transactionSum;
	private List<AccountingMonthViewModel> months = new ArrayList<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public BigDecimal getTransactionSum() {
		return transactionSum;
	}

	public void setTransactionSum(BigDecimal transactionSum) {
		this.transactionSum = transactionSum;
	}

	public List<AccountingMonthViewModel> getMonths() {
		return months;
	}

	public void setMonths(List<AccountingMonthViewModel> months) {
		this.months = months;
	}

}
