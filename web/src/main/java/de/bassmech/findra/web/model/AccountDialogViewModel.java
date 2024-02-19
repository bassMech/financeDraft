package de.bassmech.findra.web.model;

import java.io.Serializable;

public class AccountDialogViewModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String title;
	private String description;
	private int type;

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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}