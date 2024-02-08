package de.bassmech.findra.model.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

	@OneToMany(mappedBy = "accountingMonth")
	private List<Allocation> allocations = new ArrayList<>();
	
	@Column(name = "allocation_sum", columnDefinition = "FLOAT")
	private BigDecimal allocationSum;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Allocation> getAllocations() {
		return allocations;
	}

	public void setAllocations(List<Allocation> allocations) {
		this.allocations = allocations;
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

	public BigDecimal getAllocationSum() {
		return allocationSum;
	}

	public void setAllocationSum(BigDecimal allocationSum) {
		this.allocationSum = allocationSum;
	}

}
