package de.bassmech.findra.model.entity;

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
import jakarta.persistence.Table;

@Entity
@Table(name = "account")
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "code", columnDefinition = "TEXT")
	@Convert(converter = ConfigurationCodeConverter.class)
	private ConfigurationCode code;

	@Column(name = "entry", columnDefinition = "TEXT")
	private String entry;

	@Column(name = "updated_at", columnDefinition = "FLOAT")
	@Convert(converter = NumberToInstantConverter.class)
	private Instant updateAt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ConfigurationCode getCode() {
		return code;
	}

	public void setCode(ConfigurationCode code) {
		this.code = code;
	}

	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

	public Instant getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Instant updateAt) {
		this.updateAt = updateAt;
	}

}
