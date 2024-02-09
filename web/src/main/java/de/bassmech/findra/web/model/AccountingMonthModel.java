package de.bassmech.findra.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.bassmech.findra.web.view.AccountMonthAllocationView;

public class AccountingMonthModel implements Serializable {
	private int id;
	private int accountYearId;
	private int year;
	private int month;
	private List<AccountMonthAllocationView> allocations = new ArrayList<>();

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

	public List<AccountMonthAllocationView> getAllocations() {
		return allocations;
	}

	public void setAllocations(List<AccountMonthAllocationView> allocations) {
		this.allocations = allocations;
	}

}
