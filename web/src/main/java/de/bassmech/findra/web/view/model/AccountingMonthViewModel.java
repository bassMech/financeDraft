package de.bassmech.findra.web.view.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.bassmech.findra.web.util.statics.CssReference;

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
	private List<TransactionBaseViewModel> transactions = new ArrayList<>();

	public void recalculateTransactions() {
		transactionValueExpected = BigDecimal.ZERO;
		transactionValueExecuted = BigDecimal.ZERO;
		transactionCountExpected = 0;
		transactionCountExecuted = 0;

		for (TransactionBaseViewModel transactionBase : transactions) {
			if (transactionBase instanceof TransactionViewModel && ((TransactionViewModel) transactionBase).getExecutedAt() != null) {
				transactionValueExecuted = transactionValueExecuted.add(transactionBase.getValue());
				transactionCountExecuted++;
			} else {
				transactionValueExpected = transactionValueExpected.add(transactionBase.getValue());
				transactionCountExpected++;
			}
		}
	}
	
	public BigDecimal getCurrentValue() {
		return startValue.add(transactionValueExecuted);
	}

	public BigDecimal getClosingValue() {
		return startValue.add(getTransactionSum());
	}
	
	public BigDecimal getTransactionSum() {
		return transactionValueExecuted.add(transactionValueExpected);
	}
	
	public String getTransactionSumColorCssClass() {
		int transactionSumEval = getTransactionSum().compareTo(BigDecimal.ZERO);
		if (transactionSumEval == 0) {
			return "";
		} 
		return transactionSumEval > 0 ? CssReference.TRANSCTION_SUM_POSITIVE.getValue() : CssReference.TRANSCTION_SUM_NEGATIVE.getValue();
	}
	
	public List<TransactionBaseViewModel> findByGroupIdAndItemId(Integer groupId, Integer itemId) {		
		return transactions.stream().filter(x -> x.group.getId().equals(groupId)
			&& x.item.getId().equals(itemId)).collect(Collectors.toList());
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

	public List<TransactionBaseViewModel> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<TransactionBaseViewModel> transactions) {
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

	@Override
	public String toString() {
		return "AccountingMonthViewModel [id=" + id + ", accountYearId=" + accountYearId + ", year=" + year + ", month="
				+ month + ", startValue=" + startValue + ", transactionValueExpected=" + transactionValueExpected
				+ ", transactionValueExecuted=" + transactionValueExecuted + ", transactionCountExpected="
				+ transactionCountExpected + ", transactionCountExecuted=" + transactionCountExecuted + "]";
	}

}
