package de.bassmech.findra.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.bassmech.findra.web.view.AccountMonthAllocationView;

public class AccountingMonthViewModel implements Serializable {
	private int id;
	private int accountYearId;
	private int year;
	private int month;
	private List<AllocationViewModel> allocations = new ArrayList<>();

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

	public List<AllocationViewModel> getAllocations() {
		return allocations;
	}

	public void setAllocations(List<AllocationViewModel> allocations) {
		this.allocations = allocations;
	}

}
