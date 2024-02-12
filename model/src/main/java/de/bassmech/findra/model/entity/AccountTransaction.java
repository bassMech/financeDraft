package de.bassmech.findra.model.entity;

import java.math.BigDecimal;
import java.time.Instant;

import de.bassmech.findra.model.converter.ConfigurationCodeConverter;
import de.bassmech.findra.model.converter.NumberToInstantConverter;
import de.bassmech.findra.model.statics.ConfigurationCode;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "account_transaction")
public class AccountTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "title", columnDefinition = "TEXT")
	private String title;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@ManyToOne(targetEntity = AccountingMonth.class)
	@JoinColumn(referencedColumnName = "id", name = "accounting_month_id")
	private AccountingMonth accountingMonth;
	
	@Column(name = "value", columnDefinition = "FLOAT")
	private BigDecimal value;
		
	@Column(name = "expected_day", columnDefinition = "INTEGER")
	private int expectedDay;
	
	@Column(name = "executed_at", columnDefinition = "INTEGER")
	@Convert(converter = NumberToInstantConverter.class)
	private Instant executedAt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Instant getExecutedAt() {
		return executedAt;
	}

	public void setExecutedAt(Instant executedAt) {
		this.executedAt = executedAt;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public AccountingMonth getAccountingMonth() {
		return accountingMonth;
	}

	public void setAccountingMonth(AccountingMonth accountingMonth) {
		this.accountingMonth = accountingMonth;
	}

	public int getExpectedDay() {
		return expectedDay;
	}

	public void setExpectedDay(int expectedDay) {
		this.expectedDay = expectedDay;
	}


}
