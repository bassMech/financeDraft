package de.bassmech.findra.web.view.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TransactionViewModel implements Serializable {
	private Integer id;
	private String title;
	private String description;
	private int year;
	private int month;
	private int expectedDay;
	private Instant executedAt;
	private BigDecimal value;
	private List<TagViewModel> tags = new ArrayList<>();

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

	public int getExpectedDay() {
		return expectedDay;
	}

	public void setExpectedDay(int expectedDay) {
		this.expectedDay = expectedDay;
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

	public List<TagViewModel> getTags() {
		return tags;
	}

	public void setTags(List<TagViewModel> tags) {
		this.tags = tags;
	}

}
