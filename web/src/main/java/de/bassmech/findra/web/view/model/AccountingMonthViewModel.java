package de.bassmech.findra.web.view.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountingMonthViewModel implements Serializable {
	private Integer id;
	private Integer accountYearId;
	private int year;
	private int month;
	private BigDecimal startValue = BigDecimal.ZERO;
	private BigDecimal transactionValueExpected = BigDecimal.ZERO;
	private BigDecimal transactionValueExecuted = BigDecimal.ZERO;
	private int transactionCountExpected = 0;
	private int transactionCountExecuted = 0;
	private List<TransactionViewModel> transactions = new ArrayList<>();

	public void recalculateTransactions() {
		startValue = BigDecimal.ZERO;
		transactionValueExpected = BigDecimal.ZERO;
		transactionValueExecuted = BigDecimal.ZERO;
		transactionCountExpected = 0;
		transactionCountExecuted = 0;

		for (TransactionViewModel transaction : transactions) {
			if (transaction.getExecutedAt() == null) {
				transactionValueExpected = transactionValueExecuted.add(transaction.getValue());
				transactionCountExpected++;
			} else {
				transactionValueExecuted = transactionValueExecuted.add(transaction.getValue());
				transactionCountExecuted++;
			}
		}
	}

	public BigDecimal getCurrentValue() {
		return startValue.add(transactionValueExecuted);
	}

	public BigDecimal getClosingValue() {
		return startValue.add(transactionValueExecuted).add(transactionValueExpected);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAccountYearId() {
		return accountYearId;
	}

	public void setAccountYearId(Integer accountYearId) {
		this.accountYearId = accountYearId;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public List<TransactionViewModel> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<TransactionViewModel> transactions) {
		this.transactions = transactions;
	}

	public BigDecimal getStartValue() {
		return startValue;
	}

	public void setStartValue(BigDecimal startValue) {
		this.startValue = startValue;
	}

	public BigDecimal getTransactionValueExpected() {
		return transactionValueExpected;
	}

	public BigDecimal getTransactionValueExecuted() {
		return transactionValueExecuted;
	}

	public int getTransactionCountExpected() {
		return transactionCountExpected;
	}

	public int getTransactionCountExecuted() {
		return transactionCountExecuted;
	}

}
