package de.bassmech.findra.web.view.model;

import java.time.LocalDate;

public class TransactionDetailDialogViewModel extends TransactionDetailBaseDialogViewModel {
	private static final long serialVersionUID = 1L;

	private LocalDate executedAt;
		
	public TransactionDetailDialogViewModel(String dialogTitle, boolean isDraft) {
		super(dialogTitle, isDraft);
	}

	public LocalDate getExecutedAt() {
		return executedAt;
	}

	public void setExecutedAt(LocalDate executedAt) {
		this.executedAt = executedAt;
	}

}
