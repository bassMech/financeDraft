package de.bassmech.findra.model.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "client")
public class Client {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name", columnDefinition = "String")
	private String title;

	@Column(name = "password_hash", columnDefinition = "String")
	private String passwordHash;

	@Column(name = "recovery_code", columnDefinition = "String")
	private String recoveryCode;
	
	@Column(name = "updated_at", columnDefinition = "Integer")
	private Instant updatedAt;

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

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getRecoveryCode() {
		return recoveryCode;
	}

	public void setRecoveryCode(String recoveryCode) {
		this.recoveryCode = recoveryCode;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}

}
