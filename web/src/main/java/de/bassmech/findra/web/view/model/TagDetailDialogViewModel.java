package de.bassmech.findra.web.view.model;

import java.io.Serializable;
import java.time.Instant;
import java.time.Year;

public class TagDetailDialogViewModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String title;
	private String description;
	private String backgroundHexColor = "#ffffff";
	private String textHexColor = "#000000";
	private Instant deletedAt;
	
	private String dialogTitle;
		
	public TagDetailDialogViewModel(String dialogTitle) {
		this.dialogTitle = dialogTitle;
	}

	public boolean isDeleteButtonRendered() {
		return id != null && deletedAt == null;
	}
	
	public boolean isUndeleteButtonRendered() {
		return id != null && deletedAt != null;
	}

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

	public String getDialogTitle() {
		return dialogTitle;
	}

	public void setDialogTitle(String dialogTitle) {
		this.dialogTitle = dialogTitle;
	}

	public Instant getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Instant deletedAt) {
		this.deletedAt = deletedAt;
	}



}
