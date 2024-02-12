package de.bassmech.findra.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountingMonthViewModel implements Serializable {
	private int id;
	private int accountYearId;
	private int year;
	private int month;
	private BigDecimal startValue;
	private BigDecimal transactionValueExpected;
	private BigDecimal transactionValueExecuted;
	private int transactionCountExpected;
	private int transactionCountExecuted;
	private List<TransactionViewModel> transactions = new ArrayList<>();
	
	public void recalculateTransactions() {
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
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAccountYearId() {
		return accountYearId;
	}

	public void setAccountYearId(int accountYearId) {
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
