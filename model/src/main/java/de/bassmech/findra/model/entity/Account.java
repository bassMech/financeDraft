package de.bassmech.findra.model.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import de.bassmech.findra.model.converter.NumberToInstantConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "account")
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "title", columnDefinition = "TEXT")
	private String title;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;
	
	@Column(name = "deleted_at", columnDefinition = "Integer")
	@Convert(converter=NumberToInstantConverter.class)
	private Instant deletedAt;
	
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

}
