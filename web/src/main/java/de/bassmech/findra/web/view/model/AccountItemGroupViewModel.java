package de.bassmech.findra.web.view.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class AccountItemGroupViewModel {

	private Integer id;
	private String title;
	private String description;
	private Instant createdAt;
	private int accountId;
	
	private List<AccountItemViewModel> items = new ArrayList<>();

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

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public List<AccountItemViewModel> getItems() {
		return items;
	}

	public void setItems(List<AccountItemViewModel> items) {
		this.items = items;
	}

}
