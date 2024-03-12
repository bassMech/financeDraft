package de.bassmech.findra.web.view.model;

public class TransactionViewModel extends TransactionBaseViewModel {
	private int year;
	private int month;
	private Integer draftId;
			
	public TransactionViewModel() {
		super();
	}
	
	@Override
	public boolean isDraft() {
		return false;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public Integer getDraftId() {
		return draftId;
	}

	public void setDraftId(Integer draftId) {
		this.draftId = draftId;
	}

}
