package de.bassmech.findra.model.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import de.bassmech.findra.model.converter.NumberToInstantConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "account")
public class Account{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne(targetEntity = Client.class)
	@JoinColumn(referencedColumnName = "id", name = "client_id")
	private Client client;

	@Column(name = "title", columnDefinition = "TEXT")
	private String title;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;
	
	@Column(name = "starting_year", columnDefinition = "INTEGER")
	private Integer startingYear;
	
	@Column(name = "created_at", columnDefinition = "Integer")
	@Convert(converter=NumberToInstantConverter.class)
	private Instant createdAt;
	
	@Column(name = "deleted_at", columnDefinition = "Integer")
	@Convert(converter=NumberToInstantConverter.class)
	private Instant deletedAt;
	
	@ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
        name = "account_tag", 
        joinColumns = { @JoinColumn(name = "account_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "tag_id") })
	private List<Tag> tags = new ArrayList<>();
	
	@OneToMany(mappedBy = "account", cascade = { CascadeType.ALL })
	private List<AccountTransactionDraft> drafts = new ArrayList<>();
	
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

	public Integer getStartingYear() {
		return startingYear;
	}

	public void setStartingYear(Integer startingYear) {
		this.startingYear = startingYear;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public List<AccountTransactionDraft> getDrafts() {
		return drafts;
	}

	public void setDrafts(List<AccountTransactionDraft> drafts) {
		this.drafts = drafts;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client user) {
		this.client = user;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

}
