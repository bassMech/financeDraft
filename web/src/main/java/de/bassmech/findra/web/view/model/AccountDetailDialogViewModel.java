package de.bassmech.findra.web.view.model;

import java.io.Serializable;
import java.time.Year;

public class AccountDetailDialogViewModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String title;
	private String description;
	private int startingYear;
	private int type;	
	
	private String dialogTitle;
		
	public AccountDetailDialogViewModel(String dialogTitle) {
		startingYear = Year.now().getValue();
		this.dialogTitle = dialogTitle;
	}

	public boolean isDeleteButtonRendered() {
		return id != null;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public int getStartingYear() {
		return startingYear;
	}

	public void setStartingYear(int startYear) {
		this.startingYear = startYear;
	}

	public String getDialogTitle() {
		return dialogTitle;
	}

	public void setDialogTitle(String dialogTitle) {
		this.dialogTitle = dialogTitle;
	}

}
