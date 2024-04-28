package de.bassmech.findra.model.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tag")
public class Tag {
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

	@Column(name = "background_hex_color", columnDefinition = "TEXT")
	private String backgroundHexColor;

	@Column(name = "text_hex_color", columnDefinition = "TEXT")
	private String textHexColor;

	@Column(name = "created_at", columnDefinition = "INTEGER")
	private Instant createdAt;

	@Column(name = "deleted_at", columnDefinition = "INTEGER")
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

	public String getBackgroundHexColor() {
		return backgroundHexColor;
	}

	public void setBackgroundHexColor(String backgroundHexColor) {
		this.backgroundHexColor = backgroundHexColor;
	}

	public String getTextHexColor() {
		return textHexColor;
	}

	public void setTextHexColor(String textHexColor) {
		this.textHexColor = textHexColor;
	}

	public Instant getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Instant deletedAt) {
		this.deletedAt = deletedAt;
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
