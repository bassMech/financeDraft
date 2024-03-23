package de.bassmech.findra.model.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class TransactionBase {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "title", columnDefinition = "TEXT")
	private String title;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;
	
	@Column(name = "value", columnDefinition = "FLOAT")
	private BigDecimal value;
		
	@Column(name = "expected_day", columnDefinition = "INTEGER")
	private int expectedDay;
	
	@ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
        name = "transaction_tag", 
        joinColumns = { @JoinColumn(name = "transaction_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "tag_id") })
	private List<Tag> tags = new ArrayList<>();

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

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public int getExpectedDay() {
		return expectedDay;
	}

	public void setExpectedDay(int expectedDay) {
		this.expectedDay = expectedDay;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	
	
}
