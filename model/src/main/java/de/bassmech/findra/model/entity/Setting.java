package de.bassmech.findra.model.entity;

import java.time.Instant;

import de.bassmech.findra.model.converter.ConfigurationCodeConverter;
import de.bassmech.findra.model.converter.NumberToInstantConverter;
import de.bassmech.findra.model.converter.SettingCodeConverter;
import de.bassmech.findra.model.statics.SettingCode;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "setting")
public class Setting {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne(targetEntity = Client.class)
	@JoinColumn(referencedColumnName = "id", name = "client_id")
	private Client client;

	@Column(name = "code", columnDefinition = "TEXT")
	@Convert(converter = SettingCodeConverter.class)
	private SettingCode code;

	@Column(name = "entry", columnDefinition = "TEXT")
	private String entry;

	@Column(name = "updated_at", columnDefinition = "INTEGER")
	@Convert(converter = NumberToInstantConverter.class)
	private Instant updateAt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SettingCode getCode() {
		return code;
	}

	public void setCode(SettingCode code) {
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

	public Client getClient() {
		return client;
	}

	public void setClient(Client user) {
		this.client = user;
	}

}
