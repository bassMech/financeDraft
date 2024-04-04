package de.bassmech.findra.model.entity;

import java.time.Instant;

import de.bassmech.findra.model.converter.NumberToInstantConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
	private String name;

	@Column(name = "password_hash", columnDefinition = "String")
	private String passwordHash;

	@Column(name = "uuid", columnDefinition = "text")
	private String uuid;
	
	@Column(name = "session", columnDefinition = "text")
	private String session;

	@Column(name = "recovery_code", columnDefinition = "String")
	private String recoveryCode;
	
	@Column(name = "created_at", columnDefinition = "Integer")
	@Convert(converter=NumberToInstantConverter.class)
	private Instant createdAt;
	
	@Column(name = "updated_at", columnDefinition = "Integer")
	@Convert(converter=NumberToInstantConverter.class)
	private Instant updatedAt;
	
	@Column(name = "deleted_at", columnDefinition = "Integer")
	@Convert(converter=NumberToInstantConverter.class)
	private Instant deletedAt;

	@Column(name = "last_login_at", columnDefinition = "Integer")
	@Convert(converter=NumberToInstantConverter.class)
	private Instant lastLogin;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Instant deletedAt) {
		this.deletedAt = deletedAt;
	}

	public Instant getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Instant lastLogin) {
		this.lastLogin = lastLogin;
	}

}
