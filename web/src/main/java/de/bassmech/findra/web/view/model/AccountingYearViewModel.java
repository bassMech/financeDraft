package de.bassmech.findra.web.view.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AccountingYearViewModel implements Serializable {
	private Integer id;
	private Integer accountId;
	private int year;
	private BigDecimal startValue;
	private BigDecimal transactionSum;
	private List<AccountingMonthViewModel> months = new ArrayList<>();

	public void addDraftMonths() {
		if (months.size() < 12) {
			for (int iMonth = 1; iMonth <= 12; iMonth++) {
				final int finalMonth = iMonth;
				if (months.stream().noneMatch(month -> month.getMonth() == finalMonth)) {
					AccountingMonthViewModel month = new AccountingMonthViewModel();
					month.setAccountYearId(id);
					// month.setStartValue(BigDecimal.ZERO); TODO add start and final values
					month.setMonth(iMonth);
					months.add(month);
				}
			}
			months.sort(Comparator.comparing(AccountingMonthViewModel::getMonth));
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
