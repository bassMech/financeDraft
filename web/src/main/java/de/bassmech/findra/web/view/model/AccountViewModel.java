package de.bassmech.findra.web.view.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import de.bassmech.findra.model.statics.AccountCategory;
import de.bassmech.findra.model.statics.TransactionColumnLayout;
import de.bassmech.findra.model.statics.TransactionGrouping;

public class AccountViewModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer userId;
	private String title;
	private String description;
	private int startingYear;
	private Instant createdAt;
	private Instant deletedAt;
	private AccountCategory category;
	private TransactionGrouping displayOptionTransactionGrouping;
	private TransactionColumnLayout displayOptionTransactionColumnLayout;

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

	public Instant getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Instant deletedAt) {
		this.deletedAt = deletedAt;
	}

	public int getStartingYear() {
		return startingYear;
	}

	public void setStartingYear(int startingYear) {
		this.startingYear = startingYear;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public AccountCategory getCategory() {
		return category;
	}

	public void setCategory(AccountCategory category) {
		this.category = category;
	}

	public TransactionGrouping getDisplayOptionTransactionGrouping() {
		return displayOptionTransactionGrouping;
	}

	public void setDisplayOptionTransactionGrouping(TransactionGrouping displayOptionTransactionGrouping) {
		this.displayOptionTransactionGrouping = displayOptionTransactionGrouping;
	}

	public TransactionColumnLayout getDisplayOptionTransactionColumnLayout() {
		return displayOptionTransactionColumnLayout;
	}

	public void setDisplayOptionTransactionColumnLayout(TransactionColumnLayout displayOptionTransactionColumnLayout) {
		this.displayOptionTransactionColumnLayout = displayOptionTransactionColumnLayout;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccountViewModel other = (AccountViewModel) obj;
		return Objects.equals(id, other.id) && Objects.equals(userId, other.userId);
	}

}
