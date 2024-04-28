package de.bassmech.findra.web.view.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AccountingYearViewModel {
	private Integer id;
	private Integer accountId;
	private int year;
	private BigDecimal startValue = BigDecimal.ZERO;
	private BigDecimal transactionSum = BigDecimal.ZERO;
	private List<AccountingMonthViewModel> months = new ArrayList<>();

	public void addDraftMonths() {
		if (months.size() < 12) {
			BigDecimal monthStartValue = startValue;
			for (int iMonth = 1; iMonth <= 12; iMonth++) {
				final int finalMonth = iMonth;
				AccountingMonthViewModel month = months.stream().filter(monthX -> monthX.getMonth() == finalMonth)
						.findFirst().orElse(null);
				if (month == null) {
					month = new AccountingMonthViewModel();
					month.setAccountYearId(id);
					// month.setStartValue(BigDecimal.ZERO); TODO add start and final values
					month.setMonth(iMonth);
					month.setYear(year);
					month.setStartValue(monthStartValue);
					months.add(month);
				}
				monthStartValue = month.getClosingValue();
			}
			months.sort(Comparator.comparing(AccountingMonthViewModel::getMonth));
		}
	}

	public void recalculateTransactionSum() {
		transactionSum = startValue;
		BigDecimal lastMonthClosing = startValue;
		for (AccountingMonthViewModel month : months) {
			month.setStartValue(lastMonthClosing);
			month.recalculateTransactions();
			transactionSum = transactionSum
					.add(month.getTransactionValueExecuted().add(month.getTransactionValueExpected()));
			lastMonthClosing = month.getClosingValue();
		}
	}

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

	public BigDecimal getStartValue() {
		return startValue;
	}

	public void setStartValue(BigDecimal startValue) {
		this.startValue = startValue;
	}

}
