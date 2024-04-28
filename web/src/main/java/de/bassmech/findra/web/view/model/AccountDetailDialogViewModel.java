package de.bassmech.findra.web.view.model;

import java.time.Year;

public class AccountDetailDialogViewModel {

	private Integer id;
	private String title;
	private String description;
	private int startingYear;
	private int category;

	private String dialogTitle;

	private Integer selectedDisplayOptionTransactionGrouping = 0;
	private Integer selectedDisplayOptionTransactionColumnLayout = 1;

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

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public Integer getSelectedDisplayOptionTransactionGrouping() {
		return selectedDisplayOptionTransactionGrouping;
	}

	public void setSelectedDisplayOptionTransactionGrouping(Integer selectedDisplayOptionTransactionGrouping) {
		this.selectedDisplayOptionTransactionGrouping = selectedDisplayOptionTransactionGrouping;
	}

	public Integer getSelectedDisplayOptionTransactionColumnLayout() {
		return selectedDisplayOptionTransactionColumnLayout;
	}

	public void setSelectedDisplayOptionTransactionColumnLayout(Integer selectedDisplayOptionTransactionColumnLayout) {
		this.selectedDisplayOptionTransactionColumnLayout = selectedDisplayOptionTransactionColumnLayout;
	}

}
